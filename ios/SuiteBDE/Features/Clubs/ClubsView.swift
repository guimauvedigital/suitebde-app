//
//  ClubsView.swift
//  BDE
//
//  Created by Nathan Fallet on 12/02/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct ClubsView: View {
    
    @EnvironmentObject var rootViewModel: OldRootViewModel
    
    //@InjectStateViewModel var viewModel: ClubsViewModel
    @StateObject var viewModel = ClubsViewModel()
    
    var body: some View {
        ClubsListRootView(
            myClubs: viewModel.mine.map { $0.club!.suiteBde },
            moreClubs: viewModel.clubs.filter { club in
                !viewModel.mine.contains(where: { $0.clubId == club.id })
            }.map { $0.suiteBde },
            loadMore: {
                viewModel.loadMore(id: $0)
            }
        )
        .onAppear {
            viewModel.onAppear(token: rootViewModel.token)
        }
        .refreshable {
            viewModel.fetchClubs(reset: true)
        }
    }
    
}
