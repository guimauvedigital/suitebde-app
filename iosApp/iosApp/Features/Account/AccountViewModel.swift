//
//  AccountViewModel.swift
//  BDE
//
//  Created by Nathan Fallet on 31/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

class AccountViewModel: ObservableObject {
    
    @Published var url: String?
    
    func launchLogin() {
        url = APIService.shared.authenticationUrl
    }
    
    func onOpenUrl(url: URL) {
        self.url = nil
        print(url.absoluteString)
        APIService.shared.authenticate(code: url.absoluteString) { _, _ in

            DispatchQueue.main.async {
                
            }
        }
    }
    
}
