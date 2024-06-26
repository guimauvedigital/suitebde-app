//
//  ClubCard.swift
//  BDE
//
//  Created by Nathan Fallet on 13/02/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import GuimauveUI
import shared

struct ClubCard: View {
    
    let club: CommonsClub
    
    var body: some View {
        DefaultCard(
            image: {
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
            },
            title: club.name,
            description: (club.usersCount != 1 ? "clubs_members" : "clubs_member").localized().format(club.usersCount)
        )
    }
    
}

#Preview {
    Group {
        ClubCard(
            club: CommonsClub(
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
        )
        ClubCard(
            club: CommonsClub(
                id: CoreUUID(),
                associationId: CoreUUID(),
                name: "Club sans logo",
                description: "",
                logo: nil,
                createdAt: Date().asKotlinxInstant,
                validated: true,
                usersCount: 12,
                isMember: true
            )
        )
    }
}
