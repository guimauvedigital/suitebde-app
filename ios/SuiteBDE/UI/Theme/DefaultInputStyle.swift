//
//  DefaultInputStyle.swift
//  BDE
//
//  Created by Nathan Fallet on 11/02/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct DefaultInputStyle: TextFieldStyle {
    
    let icon: String?
    
    init(icon: String? = nil) {
        self.icon = icon
    }
    
    func _body(configuration: TextField<Self._Label>) -> some View {
        HStack(spacing: 12) {
            if let icon {
                Image(systemName: icon)
                    .resizable()
                    .frame(width: 16, height: 16)
                    .foregroundStyle(Color.secondary)
            }
            configuration
        }
        .padding(.horizontal)
        .padding(.vertical, 12)
        .modifier(CardStyle())
    }
    
}
