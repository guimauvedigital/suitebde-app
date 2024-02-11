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
    
    let ensisaClub: Club
    let club: Suitebde_commonsClub
    
    var body: some View {
        HStack(spacing: 12) {
            AsyncImage(
                url: URL(string: club.icon ?? ""),
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
                Text("\(ensisaClub.membersCount ?? 0) membre\(ensisaClub.membersCount ?? 0 != 1 ? "s" : "")")
                    .foregroundColor(.secondary)
            }
            .padding(.vertical)
            Spacer()
            Image(systemName: "chevron.right")
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
            ensisaClub: Club(id: "id", name: "", description: "", information: "", createdAt: Date().asKotlinxInstant, validated: true, email: nil, logo: nil, membersCount: 12),
            club: Suitebde_commonsClub(
                id: "id",
                associationId: "associationId",
                name: "Club running",
                description: "",
                icon: "https://bdensisa.org/clubs/rev4fkzzd79u7glwk0l1agdoovm3s7yo/uploads/logo%20club%20run.jpeg",
                createdAt: Date().asKotlinxInstant,
                validated: true
            )
        )
        ClubCard(
            ensisaClub: Club(id: "id", name: "", description: "", information: "", createdAt: Date().asKotlinxInstant, validated: true, email: nil, logo: nil, membersCount: 12),
            club: Suitebde_commonsClub(
                id: "id",
                associationId: "associationId",
                name: "Club sans logo",
                description: "",
                icon: nil,
                createdAt: Date().asKotlinxInstant,
                validated: true
            )
        )
    }
}
