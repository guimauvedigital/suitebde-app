//
//  UserViewModel.swift
//  BDE
//
//  Created by Nathan Fallet on 03/02/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import UIKit
import shared

class UserViewModel: ObservableObject {
    
    let isMyAccount: Bool
    
    @Published var user: User
    @Published var editable: Bool
    @Published var editing: Bool
    
    @Published var imagePickerShown = false
    @Published var image: UIImage?
    
    @Published var firstName: String
    @Published var lastName: String
    @Published var year: String
    @Published var option: String
    
    @Published var expiration: Date
    
    init(user: User, editable: Bool, isMyAccount: Bool = false) {
        self.user = user
        self.editable = editable
        self.editing = isMyAccount
        self.isMyAccount = isMyAccount
        
        self.firstName = user.firstName ?? ""
        self.lastName = user.lastName ?? ""
        self.year = user.year ?? ""
        self.option = user.option ?? ""
        
        self.expiration = user.cotisant?.expiration.asDate ?? Date()
    }
    
    func onAppear(token: String?) {
        AnalyticsService.shared.log(.screenView(screenName: "user", screenClass: "UserView"))
        
        fetchImage(token: token)
    }
    
    func toggleEdit() {
        if editable {
            editing.toggle()
        }
    }
    
    func showImagePicker() {
        imagePickerShown = true
    }
    
    func fetchImage(token: String?) {
        guard let token else {
            return
        }
        Task {
            let data = try await APIService.shared.getUserPicture(
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
            try await APIService.shared.updateUserPicture(
                token: token,
                id: user.id,
                picture: ByteArrayExtensionKt.toByteArray(data)
            )
            DispatchQueue.main.async {
                self.image = image
            }
        }
    }
    
    func updateInfo(token: String?, onUpdate: @escaping (User) -> Void) {
        guard let token else {
            return
        }
        Task {
            let user = try await APIService.shared.updateUser(
                token: token,
                id: self.isMyAccount ? "me" : self.user.id,
                firstName: self.firstName,
                lastName: self.lastName,
                year: self.year,
                option: self.option
            )
            DispatchQueue.main.async {
                self.user = user
                onUpdate(user)
            }
        }
    }
    
    func updateExpiration(token: String?) {
        guard let token else {
            return
        }
        Task {
            let user = try await APIService.shared.updateUser(
                token: token,
                id: self.user.id,
                expiration: self.expiration.asString
            )
            DispatchQueue.main.async {
                self.user = user
            }
        }
    }
    
}
