//
//  FeedSearchView.swift
//  BDE
//
//  Created by Nathan Fallet on 08/03/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import GuimauveUI
import shared

struct FeedSearchView: View {
    
    let users: [CommonsUser]
    let clubs: [CommonsClub]
    
    let hasMoreUsers: Bool
    let hasMoreClubs: Bool
    
    let loadMoreUsers: () -> Void
    let loadMoreClubs: () -> Void
    
    var body: some View {
        VStack(alignment: .leading, spacing: 16) {
            if !users.isEmpty {
                Text("search_users")
                    .font(.title2)
                LazyVGrid(
                    columns: [GridItem(.adaptive(minimum: 300, maximum: 400))],
                    alignment: .leading,
                    spacing: 16
                ) {
                    ForEach(users, id: \.id) { user in
                        DefaultNavigationLink(destination: UserView(viewModel:
                            KoinApplication.shared.koin.userViewModel(associationId: user.associationId, userId: user.id)
                        )) {
                            UserCard(user: user)
                        }
                    }
                }
                if hasMoreUsers {
                    Button(action: loadMoreUsers) {
                        Text("search_more")
                    }
                    .frame(maxWidth: .infinity, alignment: .trailing)
                }
            }
            
            if !clubs.isEmpty {
                Text("search_clubs")
                    .font(.title2)
                LazyVGrid(
                    columns: [GridItem(.adaptive(minimum: 300, maximum: 400))],
                    alignment: .leading,
                    spacing: 16
                ) {
                    ForEach(clubs, id: \.id) { club in
                        DefaultNavigationLink(destination: ClubView(viewModel:
                            KoinApplication.shared.koin.clubViewModel(id: club.id)
                        )) {
                            ClubCard(club: club)
                        }
                    }
                }
                if hasMoreClubs {
                    Button(action: loadMoreClubs) {
                        Text("search_more")
                    }
                    .frame(maxWidth: .infinity, alignment: .trailing)
                }
            }
        }
        .padding()
    }
    
}

#Preview {
    ScrollView {
        FeedSearchView(
            users: [],
            clubs: [
                CommonsClub(
                    id: CoreUUID(),
                    associationId: CoreUUID(),
                    name: "Club running",
                    description: "",
                    logo: "https://bdensisa.org/clubs/rev4fkzzd79u7glwk0l1agdoovm3s7yo/uploads/logo%20club%20run.jpeg",
                    createdAt: Date().asKotlinxInstant,
                    validated: true,
                    usersCount: 12,
                    isMember: true
                )
            ],
            hasMoreUsers: true,
            hasMoreClubs: true,
            loadMoreUsers: {},
            loadMoreClubs: {}
        )
    }
}
