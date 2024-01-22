//
//  RootView.swift
//  BDE
//
//  Created by Nathan Fallet on 31/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared
import KMMViewModelSwiftUI
import KMPNativeCoroutinesAsync

struct RootView: View {
    
    @Environment(\.scenePhase) var scenePhase
    
    @StateObject var oldViewModel = OldRootViewModel()
    
    @InjectStateViewModel private var viewModel: RootViewModel
    
    var body: some View {
        Group {
            if (Bundle.main.bundleIdentifier?.hasSuffix(".bdeensisa") == true) {
                tabView
            } else {
                if (viewModel.user != nil) {
                    tabView
                } else {
                    AuthView {
                        Task {
                            try await asyncFunction(for: viewModel.fetchUser())
                        }
                    }
                }
            }
        }
        .onAppear {
            Task {
                try await asyncFunction(for: viewModel.fetchUser())
            }
        }
        .onAppear(perform: oldViewModel.onAppear)
        .onOpenURL(perform: oldViewModel.onOpenURL)
        .onChange(of: scenePhase) { newPhase in
            WebSocketService.shared.disconnectWebSocket()
            if newPhase == .active {
                WebSocketService.shared.createWebSocket()
            }
        }
        .environmentObject(oldViewModel)
    }
    
    var tabView: some View {
        TabView {
            NavigationView {
                FeedView()
            }
            .navigationViewStyle(StackNavigationViewStyle())
            .tabItem { Label("feed_title", systemImage: "newspaper") }
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
                saveToken: oldViewModel.saveToken
            ))
            .tabItem {
                Label("account_title", systemImage: "person")
            }
        }
        .sheet(item: $oldViewModel.sheet) { sheet in
            NavigationView {
                switch sheet {
                case .user(let user):
                    UserView(viewModel: UserViewModel(
                        user: user,
                        editable: oldViewModel.user?.hasPermission(permission: "admin.users.edit") ?? false
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
