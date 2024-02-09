//
//  AuthErrorView.swift
//  BDE
//
//  Created by Nathan Fallet on 09/02/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct AuthErrorView: View {
    
    let error: String
    let tryAgainClicked: () -> Void
    
    var body: some View {
        VStack(spacing: 16) {
            Spacer()
            
            LottieView(lottieFile: "error")
                .frame(maxHeight: 300)
            
            Text(error.localized())
            
            Spacer()
            
            Button("auth_button_try_again", action: tryAgainClicked)
                .buttonStyle(DefaultButtonStyle(large: true))
        }
        .multilineTextAlignment(.center)
        .padding()
        .modifier(BackgroundColorStyle())
    }
    
}

#Preview {
    AuthErrorView(
        error: "auth_error_generic",
        tryAgainClicked: {}
    )
}
