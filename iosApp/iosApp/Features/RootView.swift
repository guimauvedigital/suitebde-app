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
            AccountView()
                .tabItem {
                    Label("Mon compte", systemImage: "person")
                }
            if viewModel.isManageShown {
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
