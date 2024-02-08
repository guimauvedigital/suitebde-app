//
//  AuthView.swift
//  BDE
//
//  Created by Nathan Fallet on 22/01/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared
import KMMViewModelSwiftUI
import KMPNativeCoroutinesAsync

struct AuthView: View {
    
    @InjectStateViewModel private var viewModel: AuthViewModel
    
    @State var sheet: AuthSheet?
    
    let onUserLogged: () -> Void
    
    func loginRegisterClicked() {
        if let url = URL(string: viewModel.url) {
            sheet = .safari(url: url)
        }
    }
    
    var body: some View {
        AuthRootView(
            loginClicked: loginRegisterClicked,
            registerClicked: loginRegisterClicked
        )
        .sheet(item: $sheet) { sheet in
            switch (sheet) {
            case .safari(let url):
                SafariView(url: url)
            }
        }
        .onOpenURL { url in
            Task {
                guard let code = URLComponents(url: url, resolvingAgainstBaseURL: true)?.queryItems?.first(where: {
                    $0.name == "code"
                })?.value else { return }
                
                try await asyncFunction(for: viewModel.authenticate(
                    code: code,
                    onUserLogged: onUserLogged
                ))
            }
        }
    }
    
}
