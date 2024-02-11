//
//  ClubsListRootView.swift
//  BDE
//
//  Created by Nathan Fallet on 11/02/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct ClubsListRootView: View {
    
    let myClubs: [Suitebde_commonsClub]
    let moreClubs: [Suitebde_commonsClub]
    
    let loadMore: (String) -> Void
    
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 16) {
                TextField("app_search", text: .constant(""))
                    .textFieldStyle(DefaultInputStyle(icon: "magnifyingglass"))
                    .padding(.horizontal)
                
                if !myClubs.isEmpty {
                    Text("clubs_my")
                        .font(.title2)
                        .padding(.horizontal)
                    
                    LazyVGrid(
                        columns: [GridItem(.adaptive(minimum: 300, maximum: 400))],
                        alignment: .leading,
                        spacing: 16
                    ) {
                        ForEach(myClubs, id: \.id) { club in
                            //DefaultNavigationLink(
                            //    destination: ClubView(viewModel: ClubViewModel(club: club))
                            //) {
                                ClubCard(club: club)
                                    .padding(.horizontal)
                            //}
                        }
                    }
                    
                    Text("clubs_more")
                        .font(.title2)
                        .padding(.horizontal)
                }
                
                LazyVGrid(
                    columns: [GridItem(.adaptive(minimum: 300, maximum: 400))],
                    alignment: .leading,
                    spacing: 16
                ) {
                    ForEach(moreClubs, id: \.id) { club in
                        //DefaultNavigationLink(
                        //    destination: ClubView(viewModel: ClubViewModel(club: club))
                        //) {
                            ClubCard(club: club)
                                .padding(.horizontal)
                                .onAppear {
                                    loadMore(club.id)
                                }
                        //}
                    }
                }
            }
            .padding(.vertical)
        }
        .defaultNavigationTitle("clubs_title".localized())
    }
    
}

#Preview {
    DefaultNavigationView {
        ClubsListRootView(
            myClubs: [
                Suitebde_commonsClub(
                    id: "id",
                    associationId: "associationId",
                    name: "Club running",
                    description: "",
                    icon: "https://bdensisa.org/clubs/rev4fkzzd79u7glwk0l1agdoovm3s7yo/uploads/logo%20club%20run.jpeg",
                    createdAt: Date().asKotlinxInstant,
                    validated: true
                )
            ],
            moreClubs: [
                Suitebde_commonsClub(
                    id: "id",
                    associationId: "associationId",
                    name: "Club Chess",
                    description: "",
                    icon: "https://bdensisa.org/clubs/rev4fkzzd79u7glwk0l1agdoovm3s7yo/uploads/logo%20club%20run.jpeg",
                    createdAt: Date().asKotlinxInstant,
                    validated: true
                )
            ],
            loadMore: { _ in }
        )
    }
}
