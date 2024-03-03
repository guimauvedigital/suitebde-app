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
    
    @Environment(\.openURL) var openURL
    
    @EnvironmentObject var rootViewModel: OldRootViewModel
    @StateObject var oldViewModel = OldFeedViewModel()
    
    var oldBeforeView: some View {
        VStack(alignment: .leading) {
            if rootViewModel.user != nil && !oldViewModel.ticketConfigurations.isEmpty {
                Group {
                    HStack {
                        Text("Tickets")
                            .font(.title2)
                        Spacer()
                    }
                    LazyVGrid(
                        columns: [GridItem(.adaptive(minimum: 300, maximum: 400))],
                        alignment: .leading
                    ) {
                        ForEach(oldViewModel.ticketConfigurations, id: \.id) { configuration in
                            ShopCard(
                                item: configuration,
                                detailsEnabled: true,
                                cotisant: rootViewModel.user?.cotisant != nil
                            )
                        }
                    }
                }
                .padding()
            }
        }
    }
    
    var body: some View {
        FeedRootView(
            oldBeforeView: oldBeforeView,
            subscriptions: viewModel.subscriptions ?? [],
            events: viewModel.events ?? [],
            sendNotificationVisible: rootViewModel.user?.hasPermission(permission: "admin.notifications") ?? false
        )
        .onAppear(perform: oldViewModel.onAppear)
        .onAppear {
            Task {
                try await asyncFunction(for: viewModel.onAppear())
            }
        }
        .refreshable {
            oldViewModel.fetchData()
            Task {
                try await asyncFunction(for: viewModel.fetchFeed(reset: true))
            }
        }
    }
    
}
