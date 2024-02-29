//
//  SubscriptionView.swift
//  BDE
//
//  Created by Nathan Fallet on 25/02/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import shared
import SwiftUI
import KMMViewModelSwiftUI
import KMPNativeCoroutinesAsync

struct SubscriptionView: View {
    
    @StateViewModel var viewModel: SubscriptionViewModel
    
    var body: some View {
        Group {
            if let subscription = viewModel.subscription {
                SubscriptionDetailsView(
                    subscription: subscription,
                    buy: {
                        Task {
                            try await asyncFunction(for: viewModel.checkoutSubscription())
                        }
                    }
                )
            } else {
                ProgressView()
            }
        }
        .onAppear {
            Task {
                try await asyncFunction(for: viewModel.onAppear())
            }
        }
        .refreshable {
            Task {
                try await asyncFunction(for: viewModel.fetchSubscription(reset: true))
            }
        }
        .sheet(item: Binding(
            get: { viewModel.url },
            set: { _, _ in viewModel.close() }
        )) { url in
            SafariView(url: URL(string: url)!)
        }
    }
    
}
