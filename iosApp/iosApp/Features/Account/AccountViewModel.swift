//
//  AccountViewModel.swift
//  BDE
//
//  Created by Nathan Fallet on 31/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared

class AccountViewModel: ObservableObject {
    
    let saveToken: (UserToken) -> Void
    
    @Published var url: String?
    @Published var error: String?
    @Published var qrcode: UIImage?
    
    init(saveToken: @escaping (UserToken) -> Void) {
        self.saveToken = saveToken
    }
    
    func launchLogin() {
        url = APIService.shared.authenticationUrl
    }
    
    func onOpenUrl(url: URL) {
        guard url.host == "authorize" else { return }
        self.url = nil
        APIService.shared.authenticate(code: url.absoluteString) { userToken, error in
            DispatchQueue.main.async {
                if let error {
                    self.error = error.localizedDescription
                }
                if let userToken {
                    self.saveToken(userToken)
                }
            }
        }
    }
    
    func generateQRCode(user: User) {
        Task {
            let code = "bdeensisa://users/\(user.id)".generateQRCode()
            DispatchQueue.main.async {
                self.qrcode = code
            }
        }
    }
    
}
