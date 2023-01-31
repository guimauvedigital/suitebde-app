//
//  RootViewModel.swift
//  BDE
//
//  Created by Nathan Fallet on 31/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

class RootViewModel: ObservableObject {
    
    @Published var user: User? {
        didSet {
            // Save user
            
        }
    }
    @Published var token: String? {
        didSet {
            // Save token
            
        }
    }
    @Published var isManageShown = false
    
    func onAppear() {
        // Load user and token, if connected
        
    }
    
}
