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
    
    let user: CommonsUser
    let customDescription: String?
    
    init(user: CommonsUser, customDescription: String? = nil) {
        self.user = user
        self.customDescription = customDescription
    }
    
    var fullName: String {
        "\(user.firstName) \(user.lastName)"
    }
    
    var body: some View {
        DefaultCard(
            image: {
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
            },
            title: fullName,
            description: customDescription ?? ""
        )
    }
    
}

#Preview {
    Group {
        UserCard(
            user: CommonsUser(
                id: CoreUUID(),
                associationId: CoreUUID(),
                email: "",
                password: nil,
                firstName: "Nathan",
                lastName: "Fallet",
                superuser: true,
                lastLoginAt: Date().asKotlinxInstant
            )
        )
        UserCard(
            user: CommonsUser(
                id: CoreUUID(),
                associationId: CoreUUID(),
                email: "",
                password: nil,
                firstName: "Nathan",
                lastName: "Fallet",
                superuser: true,
                lastLoginAt: Date().asKotlinxInstant
            ),
            customDescription: "Some custom text"
        )
    }
}
