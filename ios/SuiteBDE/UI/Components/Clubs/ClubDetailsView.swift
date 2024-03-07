//
//  ClubDetailsView.swift
//  BDE
//
//  Created by Nathan Fallet on 12/02/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct ClubDetailsView: View {
    
    let club: Suitebde_commonsClub
    let users: [Suitebde_commonsUserInClub]
    let loadMore: (String) -> Void
    let onJoinLeaveClicked: () -> Void
    
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 16) {
                VStack(alignment: .leading) {
                    HStack {
                        Text("clubs_information")
                            .font(.title2)
                        Spacer()
                    }
                    Text((club.usersCount != 1 ? "clubs_members" : "clubs_member").localized().format(club.usersCount))
                        .foregroundStyle(.secondary)
                }
                Text(club.description_)
                    .multilineTextAlignment(.leading)
                
                Button(
                    club.isMember?.boolValue ?? false ? "clubs_button_leave" : "clubs_button_join",
                    action: onJoinLeaveClicked
                )
                .buttonStyle(DefaultButtonStyle(
                    filled: club.isMember?.boolValue != true
                ))
                
                HStack {
                    Text("clubs_information_members")
                        .font(.title2)
                    Spacer()
                }
                LazyVGrid(
                    columns: [GridItem(.adaptive(minimum: 300, maximum: 400))],
                    alignment: .leading,
                    spacing: 16
                ) {
                    ForEach(users, id: \.userId) { userInClub in
                        if let user = userInClub.user {
                            UserCard(
                                user: user,
                                customDescription: userInClub.role.name
                            )
                            .onAppear {
                                loadMore(userInClub.id)
                            }
                        }
                    }
                }
            }
            .padding()
        }
        .defaultNavigationTitle(club.name)
        .defaultNavigationBackButtonHidden(false)
        .defaultNavigationImage {
            AsyncImage(
                url: URL(string: club.logo ?? ""),
                content: { image in
                    image
                        .resizable()
                        .aspectRatio(contentMode: .fill)
                },
                placeholder: {
                    Color(UIColor.systemGray3)
                        .overlay {
                            Text(club.name.initials)
                                .font(.title)
                        }
                }
            )
        }
    }
    
}

#Preview {
    DefaultNavigationView {
        ClubDetailsView(
            club: Suitebde_commonsClub(
                id: "id",
                associationId: "associationId",
                name: "Club running",
                description: "Club running de l'ENSISA ! RDV tous les jeudis à la barrière du parking Werner",
                logo: "https://bdensisa.org/clubs/rev4fkzzd79u7glwk0l1agdoovm3s7yo/uploads/logo%20club%20run.jpeg",
                createdAt: Date().asKotlinxInstant,
                validated: true,
                usersCount: 12,
                isMember: true
            ),
            users: [
                Suitebde_commonsUserInClub(
                    userId: "userId",
                    clubId: "id",
                    roleId: "roleId",
                    user: Suitebde_commonsUser(
                        id: "userId",
                        associationId: "associationId",
                        email: "",
                        password: nil,
                        firstName: "Nathan",
                        lastName: "Fallet",
                        superuser: true
                    ),
                    club: nil,
                    role: Suitebde_commonsRoleInClub(
                        id: "roleId",
                        clubId: "id",
                        name: "Admin",
                        admin: true,
                        default: false
                    )
                ),
                Suitebde_commonsUserInClub(
                    userId: "userId2",
                    clubId: "id",
                    roleId: "roleId",
                    user: Suitebde_commonsUser(
                        id: "userId2",
                        associationId: "associationId",
                        email: "",
                        password: nil,
                        firstName: "Maxime",
                        lastName: "Sanciaume",
                        superuser: true
                    ),
                    club: nil,
                    role: Suitebde_commonsRoleInClub(
                        id: "roleId",
                        clubId: "id",
                        name: "Membre",
                        admin: false,
                        default: true
                    )
                )
            ],
            loadMore: { _ in },
            onJoinLeaveClicked: {}
        )
    }
}
