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
            if viewModel.loading {
                ProgressView()
            } else if let error = viewModel.error {
                AuthErrorView(
                    error: error,
                    tryAgainClicked: {
                        Task {
                            try await asyncFunction(for: viewModel.fetchUser())
                        }
                    }
                )
            } else if viewModel.user != nil {
                tabView
            } else {
                AuthView {
                    Task {
                        try await asyncFunction(for: viewModel.fetchUser())
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
        .environmentObject(viewModel)
    }
    
    var tabView: some View {
        TabView {
            DefaultNavigationView { FeedView() }
                .tabItem { Label("feed_title", systemImage: "newspaper") }
            DefaultNavigationView { ClubsView() }
                .tabItem { Label("clubs_title", systemImage: "bicycle") }
        }
        .sheet(item: $oldViewModel.sheet) { sheet in
            NavigationView {
                switch sheet {
                case .user(let user):
                    UserView(
                        oldViewModel: OldUserViewModel(
                            user: user,
                            editable: oldViewModel.user?.hasPermission(permission: "admin.users.edit") ?? false
                        ),
                        viewModel: KoinApplication.shared.koin.userViewModel(associationId: "", userId: user.id)
                    )
                }
            }
        }
    }
    
}
