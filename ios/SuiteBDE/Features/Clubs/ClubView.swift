//
//  ClubView.swift
//  BDE
//
//  Created by Nathan Fallet on 13/02/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared
import KMMViewModelSwiftUI
import KMPNativeCoroutinesAsync

struct ClubView: View {
    
    @Environment(\.openURL) var openURL
    @EnvironmentObject var rootViewModel: OldRootViewModel
    
    @StateViewModel var viewModel: ClubViewModel
    
    var body: some View {
        Group {
            if let club = viewModel.club {
                ClubDetailsView(
                    club: club,
                    users: viewModel.users ?? [],
                    loadMore: {
                        viewModel.loadMoreIfNeeded(userId: $0)
                    },
                    onJoinLeaveClicked: {
                        Task {
                            try await asyncFunction(for: viewModel.onJoinLeaveClicked())
                        }
                    }
                )
            } else {
                Text("Nothing here!")
                    .padding()
            }
        }
        .onAppear {
            Task {
                try await asyncFunction(for: viewModel.onAppear())
            }
        }
        .refreshable {
            Task {
                try await asyncFunction(for: viewModel.fetchClub(reset: true))
            }
        }
    }
    
}
