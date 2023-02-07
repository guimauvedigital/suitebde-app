//
//  UserViewModel.swift
//  BDE
//
//  Created by Nathan Fallet on 03/02/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

class UserViewModel: ObservableObject {
    
    @Published var user: User
    @Published var editable: Bool
    @Published var editing = false
    
    @Published var firstName: String
    @Published var lastName: String
    @Published var year: String
    @Published var option: String
    
    @Published var expiration: Date
    
    init(user: User, editable: Bool) {
        self.user = user
        self.editable = editable
        
        self.firstName = user.firstName ?? ""
        self.lastName = user.lastName ?? ""
        self.year = user.year ?? ""
        self.option = user.option ?? ""
        
        self.expiration = user.cotisant?.expiration.asDate ?? Date()
    }
    
    func onAppear() {
        AnalyticsService.shared.log(.screenView(screenName: "user", screenClass: "UserView"))
    }
    
    func toggleEdit() {
        if editable {
            editing.toggle()
        }
    }
    
    func updateInfo(token: String?) {
        guard let token else {
            return
        }
        Task {
            let user = try await APIService.shared.updateUser(
                token: token,
                id: self.user.id,
                firstName: self.firstName,
                lastName: self.lastName,
                year: self.year,
                option: self.option
            )
            DispatchQueue.main.async {
                self.user = user
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
