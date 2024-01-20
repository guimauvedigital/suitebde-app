//
//  RootView.swift
//  BDE
//
//  Created by Nathan Fallet on 31/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct RootView: View {
    
    @Environment(\.scenePhase) var scenePhase
    
    @StateObject var viewModel = RootViewModel()
    
    var body: some View {
        Group {
            tabView
        }
        .onAppear(perform: viewModel.onAppear)
        .onChange(of: scenePhase) { newPhase in
            WebSocketService.shared.disconnectWebSocket()
            if newPhase == .active {
                WebSocketService.shared.createWebSocket()
            }
        }
        .onOpenURL(perform: viewModel.onOpenURL)
        .environmentObject(viewModel)
    }
    
    var tabView: some View {
        TabView {
            FeedView()
                .tabItem {
                    Label("feed_title", systemImage: "newspaper")
                }
            CalendarView()
                .tabItem {
                    Label("calendar_title", systemImage: "calendar")
                }
            ClubsView()
                .tabItem {
                    Label("clubs_title", systemImage: "bicycle")
                }
            ChatView()
                .tabItem {
                    Label("chat_title", systemImage: "message")
                }
            AccountView(viewModel: AccountViewModel(
                saveToken: viewModel.saveToken
            ))
            .tabItem {
                Label("account_title", systemImage: "person")
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
    }
    
}

struct RootView_Previews: PreviewProvider {
    
    static var previews: some View {
        RootView()
    }
    
}
