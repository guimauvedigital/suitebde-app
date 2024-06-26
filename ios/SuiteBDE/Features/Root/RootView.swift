//
//  RootView.swift
//  BDE
//
//  Created by Nathan Fallet on 31/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import GuimauveUI
import shared
import KMPObservableViewModelSwiftUI
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
        .onOpenURL { url in
            viewModel.onOpenURL(url: Url(scheme: url.scheme, host: url.host, path: url.path))
        }
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
            DefaultNavigationView {
                UserView(
                    viewModel: KoinApplication.shared.koin.userViewModel(
                        associationId: viewModel.user!.associationId,
                        userId: viewModel.user!.id
                    ),
                    navigationBackButtonHidden: true
                )
            }
            .tabItem { Label("account_title", systemImage: "person") }
        }
        .sheet(item: Binding(get: { viewModel.scannedUser }, set: { _ in viewModel.closeItem() })) { user in
            DefaultNavigationView {
                UserView(viewModel: KoinApplication.shared.koin.userViewModel(
                    associationId: user.associationId,
                    userId: user.userId
                ))
            }
        }
    }
    
}
