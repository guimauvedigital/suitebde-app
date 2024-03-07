//
//  ClubsListView.swift
//  BDE
//
//  Created by Nathan Fallet on 11/02/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct ClubsListView: View {
    
    let myClubs: [Suitebde_commonsClub]
    let moreClubs: [Suitebde_commonsClub]
    
    let loadMore: (String) -> Void
    
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 16) {
                if !myClubs.isEmpty {
                    Text("clubs_my")
                        .font(.title2)
                    
                    LazyVGrid(
                        columns: [GridItem(.adaptive(minimum: 300, maximum: 400))],
                        alignment: .leading,
                        spacing: 16
                    ) {
                        ForEach(myClubs, id: \.id) { club in
                            DefaultNavigationLink(destination: ClubView(viewModel:
                                KoinApplication.shared.koin.clubViewModel(id: club.id)
                            )) {
                                ClubCard(club: club)
                                    .onAppear {
                                        loadMore(club.id)
                                    }
                            }
                        }
                    }
                    
                    if !myClubs.isEmpty && !moreClubs.isEmpty {
                        Text("clubs_more")
                            .font(.title2)
                    }
                }
                
                LazyVGrid(
                    columns: [GridItem(.adaptive(minimum: 300, maximum: 400))],
                    alignment: .leading,
                    spacing: 16
                ) {
                    ForEach(moreClubs, id: \.id) { club in
                        DefaultNavigationLink(destination: ClubView(viewModel:
                            KoinApplication.shared.koin.clubViewModel(id: club.id)
                        )) {
                            ClubCard(club: club)
                                .onAppear {
                                    loadMore(club.id)
                                }
                        }
                    }
                }
            }
            .padding()
        }
        .defaultNavigationTitle("clubs_title".localized())
    }
    
}

#Preview {
    DefaultNavigationView {
        ClubsListView(
            myClubs: [
                Suitebde_commonsClub(
                    id: "id",
                    associationId: "associationId",
                    name: "Club running",
                    description: "",
                    logo: "https://bdensisa.org/clubs/rev4fkzzd79u7glwk0l1agdoovm3s7yo/uploads/logo%20club%20run.jpeg",
                    createdAt: Date().asKotlinxInstant,
                    validated: true,
                    usersCount: 12,
                    isMember: true
                )
            ],
            moreClubs: [
                Suitebde_commonsClub(
                    id: "id",
                    associationId: "associationId",
                    name: "Club Chess",
                    description: "",
                    logo: "https://bdensisa.org/clubs/rev4fkzzd79u7glwk0l1agdoovm3s7yo/uploads/logo%20club%20run.jpeg",
                    createdAt: Date().asKotlinxInstant,
                    validated: true,
                    usersCount: 12,
                    isMember: false
                )
            ],
            loadMore: { _ in }
        )
    }
}
