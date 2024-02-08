//
//  AuthRootView.swift
//  BDE
//
//  Created by Nathan Fallet on 07/02/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct AuthRootView: View {
    
    let loginClicked: () -> Void
    let registerClicked: () -> Void
    
    var body: some View {
        VStack(spacing: 16) {
            Spacer()
            
            LottieView(lottieFile: "school")
                .frame(maxHeight: 300)
            
            Spacer()
            
            Text("auth_title")
                .font(.largeTitle)
            Text("auth_description")
            
            Spacer()
            
            Button("auth_button_login", action: loginClicked)
                .buttonStyle(DefaultButtonStyle(large: true))
            Button("auth_button_register", action: registerClicked)
                .buttonStyle(DefaultButtonStyle(filled: false, large: true))
        }
        .multilineTextAlignment(.center)
        .padding()
        .modifier(BackgroundColorStyle())
    }
    
}

#Preview {
    AuthRootView(
        loginClicked: {},
        registerClicked: {}
    )
}
