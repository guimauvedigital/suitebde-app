//
//  OldUserViewModel.swift
//  BDE
//
//  Created by Nathan Fallet on 03/02/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import UIKit
import shared

class OldUserViewModel: ObservableObject {
    
    let isMyAccount: Bool
    
    @Published var user: User
    @Published var editable: Bool
    @Published var editing: Bool
    
    @Published var hasUnsavedChanges = false
    @Published var alert: AlertCase? {
        willSet {
            if alert == .saved && newValue == nil {
                self.hasUnsavedChanges = false
            }
        }
    }
    
    @Published var imagePickerShown = false
    @Published var image: UIImage?
    
    @Published var firstName = "" { didSet { hasUnsavedChanges = true }}
    @Published var lastName = "" { didSet { hasUnsavedChanges = true }}
    @Published var year = "" { didSet { hasUnsavedChanges = true }}
    @Published var option = "" { didSet { hasUnsavedChanges = true }}
    
    @Published var expiration = Date() { didSet { hasUnsavedChanges = true }}
    
    @Published var tickets = [Ticket]()
    @Published var paid = [String: Bool]()
    
    init(user: User, editable: Bool, isMyAccount: Bool = false) {
        self.user = user
        self.editable = editable
        self.editing = isMyAccount
        self.isMyAccount = isMyAccount
        
        resetChanges()
    }
    
    func resetChanges() {
        self.firstName = user.firstName ?? ""
        self.lastName = user.lastName ?? ""
        self.year = user.year ?? ""
        self.option = user.option ?? ""
        
        self.expiration = user.cotisant?.expiration.asDate ?? Date()
        
        self.hasUnsavedChanges = false
    }
    
    func onAppear(token: String?, viewedBy: User?) {
        AnalyticsService.shared.log(.screenView(screenName: "user", screenClass: "UserView"))
        
        fetchImage(token: token)
        fetchTickets(token: token, viewedBy: viewedBy)
    }
    
    func toggleEdit() {
        if editable {
            if editing && hasUnsavedChanges {
                alert = .cancelling
                return
            }
            editing.toggle()
            if editing {
                resetChanges()
            }
        }
    }
    
    func discardEdit() {
        editing = false
    }
    
    func showImagePicker() {
        imagePickerShown = true
    }
    
    func fetchImage(token: String?) {
        guard let token else {
            return
        }
        Task {
            let data = try await CacheService.shared.apiService().getUserPicture(
                token: token,
                id: user.id
            )
            DispatchQueue.main.async {
                self.image = UIImage(data: data.toNSData())
            }
        }
    }
    
    func updateImage(token: String?, image: UIImage?) {
        guard let token, let image, let data = image.jpegData(compressionQuality: 0.0) else {
            return
        }
        Task {
            try await CacheService.shared.apiService().updateUserPicture(
                token: token,
                id: user.id,
                picture: ByteArrayExtensionKt.toByteArray(data)
            )
            DispatchQueue.main.async {
                self.image = image
                self.alert = .saved
            }
        }
    }
    
    func updateInfo(token: String?, onUpdate: @escaping (User) -> Void) {
        guard let token else {
            return
        }
        Task {
            let user = try await CacheService.shared.apiService().updateUser(
                token: token,
                id: self.isMyAccount ? "me" : self.user.id,
                upload: UserUpload(
                    firstName: self.firstName,
                    lastName: self.lastName,
                    year: self.year,
                    option: self.option,
                    expiration: nil
                )
            )
            DispatchQueue.main.async {
                self.user = user
                self.alert = .saved
                if self.isMyAccount {
                    onUpdate(user)
                }
            }
        }
    }
    
    func updateExpiration(token: String?) {
        guard let token else {
            return
        }
        Task {
            let user = try await CacheService.shared.apiService().updateUser(
                token: token,
                id: self.user.id,
                upload: UserUpload(
                    firstName: nil,
                    lastName: nil,
                    year: nil,
                    option: nil,
                    expiration: self.expiration.asString
                )
            )
            DispatchQueue.main.async {
                self.user = user
                self.alert = .saved
            }
        }
    }
    
    func fetchTickets(token: String?, viewedBy: User?) {
        guard let token else {
            return
        }
        Task {
            let tickets = try await CacheService.shared.apiService().getUserTickets(
                token: token,
                id: self.user.id
            )
            DispatchQueue.main.async {
                self.tickets = tickets
                tickets.forEach { ticket in
                    self.paid[ticket.id] = ticket.paid != nil
                }
            }
        }
    }
    
    func updateTicket(token: String?, id: String) {
        guard let token else {
            return
        }
        Task {
            try await CacheService.shared.apiService().updateUserTicket(
                token: token,
                id: user.id,
                ticketId: id,
                paid: paid[id] ?? false
            )
            DispatchQueue.main.async {
                self.alert = .saved
            }
        }
    }
    
}
