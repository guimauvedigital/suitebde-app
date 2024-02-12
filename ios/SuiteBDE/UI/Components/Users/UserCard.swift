//
//  UserCard.swift
//  BDE
//
//  Created by Nathan Fallet on 12/02/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct UserCard: View {
    
    let user: Suitebde_commonsUser
    let customDescription: String?
    
    init(user: Suitebde_commonsUser, customDescription: String? = nil) {
        self.user = user
        self.customDescription = customDescription
    }
    
    var fullName: String {
        "\(user.firstName) \(user.lastName)"
    }
    
    var body: some View {
        HStack(spacing: 12) {
            AsyncImage(
                url: URL(string: ""),
                content: { image in
                    image
                        .resizable()
                        .aspectRatio(contentMode: .fill)
                },
                placeholder: {
                    Color(UIColor.systemGray3)
                        .overlay {
                            Text(fullName.initials)
                                .font(.title)
                        }
                }
            )
            .frame(width: 76, height: 76)
            .clipped()
            VStack(alignment: .leading) {
                Text(fullName)
                    .lineLimit(1)
                Text(customDescription ?? "")
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
        UserCard(
            user: Suitebde_commonsUser(
                id: "userId",
                associationId: "associationId",
                email: "",
                password: nil,
                firstName: "Nathan",
                lastName: "Fallet",
                superuser: true
            )
        )
        UserCard(
            user: Suitebde_commonsUser(
                id: "userId",
                associationId: "associationId",
                email: "",
                password: nil,
                firstName: "Nathan",
                lastName: "Fallet",
                superuser: true
            ),
            customDescription: "Some custom text"
        )
    }
}
