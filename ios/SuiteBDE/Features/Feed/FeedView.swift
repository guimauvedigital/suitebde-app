//
//  FeedView.swift
//  BDE
//
//  Created by Nathan Fallet on 31/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared
import KMMViewModelSwiftUI
import KMPNativeCoroutinesAsync

struct FeedView: View {
    
    @InjectStateViewModel var viewModel: FeedViewModel
    @InjectStateViewModel var searchViewModel: SearchViewModel
    
    @Environment(\.openURL) var openURL
    
    @EnvironmentObject var rootViewModel: OldRootViewModel
    
    var body: some View {
        FeedRootView(
            search: Binding(get: { searchViewModel.search }, set: searchViewModel.updateSearch),
            subscriptions: viewModel.subscriptions ?? [],
            events: viewModel.events ?? [],
            sendNotificationVisible: viewModel.sendNotificationVisible,
            showScannerVisible: viewModel.showScannerVisible,
            onOpenURL: rootViewModel.onOpenURL,
            users: searchViewModel.users ?? [],
            clubs: searchViewModel.clubs ?? [],
            hasMoreUsers: searchViewModel.hasMoreUsers,
            hasMoreClubs: searchViewModel.hasMoreClubs,
            loadMoreUsers: searchViewModel.loadMoreUsers,
            loadMoreClubs: searchViewModel.loadMoreClubs
        )
        .onAppear {
            Task {
                try await asyncFunction(for: viewModel.onAppear())
            }
        }
        .refreshable {
            Task {
                try await asyncFunction(for: viewModel.fetchFeed(reset: true))
            }
        }
    }
    
}
