//
//  AuthSheet.swift
//  BDE
//
//  Created by Nathan Fallet on 22/01/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import shared

enum AuthSheet: Identifiable {
    
    case safari(url: URL)
    
    var id: String {
        switch (self) {
        case .safari(let url):
            return url.absoluteString
        }
    }
    
}
