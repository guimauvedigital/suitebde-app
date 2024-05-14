//
//  ClubsView.swift
//  BDE
//
//  Created by Nathan Fallet on 12/02/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared
import KMPObservableViewModelSwiftUI
import KMPNativeCoroutinesAsync

struct ClubsView: View {
    
    @EnvironmentObject var rootViewModel: OldRootViewModel
    
    @InjectStateViewModel var viewModel: ClubsViewModel
    
    var body: some View {
        ClubsListView(
            myClubs: viewModel.myClubs ?? [],
            moreClubs: viewModel.moreClubs ?? [],
            loadMore: {
                viewModel.loadMoreIfNeeded(clubId: $0)
            }
        )
        .onAppear {
            Task {
                try await asyncFunction(for: viewModel.onAppear())
            }
        }
        .refreshable {
            Task {
                try await asyncFunction(for: viewModel.fetchClubs(reset: true))
            }
        }
    }
    
}
