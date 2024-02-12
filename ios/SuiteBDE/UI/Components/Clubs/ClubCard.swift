//
//  ClubCard.swift
//  BDE
//
//  Created by Nathan Fallet on 13/02/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import Kingfisher
import shared

struct ClubCard: View {
    
    let club: Suitebde_commonsClub
    
    var body: some View {
        HStack(spacing: 12) {
            AsyncImage(
                url: URL(string: club.logo ?? ""),
                content: { image in
                    image
                        .resizable()
                        .aspectRatio(contentMode: .fill)
                },
                placeholder: {
                    Image(.defaultEvent)
                        .resizable()
                        .aspectRatio(contentMode: .fill)
                }
            )
            .frame(width: 76, height: 76)
            .clipped()
            VStack(alignment: .leading) {
                Text(club.name)
                    .lineLimit(1)
                Text((club.usersCount != 1 ? "clubs_members" : "clubs_member").localized().format(club.usersCount))
                    .foregroundStyle(.secondary)
            }
            .padding(.vertical)
            Spacer()
            Image(systemName: "chevron.right")
                .foregroundStyle(.secondary)
                .padding()
        }
        .foregroundColor(.primary)
        .multilineTextAlignment(.leading)
        .modifier(CardStyle())
    }
    
}

#Preview {
    Group {
        ClubCard(
            club: Suitebde_commonsClub(
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
        )
        ClubCard(
            club: Suitebde_commonsClub(
                id: "id",
                associationId: "associationId",
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
