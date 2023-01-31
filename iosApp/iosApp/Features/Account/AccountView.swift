//
//  AccountView.swift
//  BDE
//
//  Created by Nathan Fallet on 31/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct AccountView: View {
    
    @EnvironmentObject var rootViewModel: RootViewModel
    @StateObject var viewModel = AccountViewModel()
    
    var body: some View {
        if let user = rootViewModel.user {
            Text("Hello, World!")
        } else {
            Button("Connexion", action: viewModel.launchLogin)
                .onOpenURL(perform: viewModel.onOpenUrl)
                .sheet(item: $viewModel.url) { url in
                    SafariView(url: URL(string: url)!)
                }
        }
    }
    
}

struct AccountView_Previews: PreviewProvider {
    
    static var previews: some View {
        AccountView()
            .environmentObject(RootViewModel())
    }
    
}
