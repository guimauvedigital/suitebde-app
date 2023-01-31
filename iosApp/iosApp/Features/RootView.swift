//
//  RootView.swift
//  BDE
//
//  Created by Nathan Fallet on 31/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct RootView: View {
    
    @StateObject var viewModel = RootViewModel()
    
    var body: some View {
        TabView {
            FeedView()
                .tabItem {
                    Label("Actualité", systemImage: "newspaper")
                }
            AccountView(viewModel: AccountViewModel(
                saveToken: viewModel.saveToken
            ))
            .tabItem {
                Label("Mon compte", systemImage: "person")
            }
            if viewModel.user?.hasPermission(permission: "admin.users.view") ?? false {
                ManageView()
                    .tabItem {
                        Label("Gestion", systemImage: "doc.badge.gearshape")
                    }
            }
        }
        .onAppear(perform: viewModel.onAppear)
        .environmentObject(viewModel)
    }
    
}

struct RootView_Previews: PreviewProvider {
    
    static var previews: some View {
        RootView()
    }
    
}
