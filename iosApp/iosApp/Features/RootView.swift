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
            CalendarView()
                .tabItem {
                    Label("Calendrier", systemImage: "calendar")
                }
            ClubsView()
                .tabItem {
                    Label("Clubs", systemImage: "bicycle")
                }
            AccountView(viewModel: AccountViewModel(
                saveToken: viewModel.saveToken
            ))
            .tabItem {
                Label("Mon compte", systemImage: "person")
            }
        }
        .sheet(item: $viewModel.sheet) { sheet in
            NavigationView {
                switch sheet {
                case .user(let user):
                    UserView(viewModel: UserViewModel(
                        user: user,
                        editable: viewModel.user?.hasPermission(permission: "admin.users.edit") ?? false
                    ))
                }
            }
        }
        .onAppear(perform: viewModel.onAppear)
        .onOpenURL(perform: viewModel.onOpenURL)
        .environmentObject(viewModel)
    }
    
}

struct RootView_Previews: PreviewProvider {
    
    static var previews: some View {
        RootView()
    }
    
}
