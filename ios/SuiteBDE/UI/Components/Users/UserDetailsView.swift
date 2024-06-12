//
//  UserDetailsView.swift
//  BDE
//
//  Created by Nathan Fallet on 20/03/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct UserDetailsView: View {
    
    let user: Suitebde_commonsUser
    let isCurrentUser: Bool
    let subscriptions: [Suitebde_commonsSubscriptionInUser]
    let toggleEditing: () -> Void
    let navigationBackButtonHidden: Bool
    
    var fullName: String {
        "\(user.firstName) \(user.lastName)"
    }
    
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 16) {
                if isCurrentUser {
                    Text("qrcode_title")
                        .font(.title2)
                    DefaultNavigationLink(destination: QRCodeView()) {
                        QRCodeCard()
                    }
                }
                
                HStack {
                    Text("users_information")
                        .font(.title2)
                    Spacer()
                }
                // TODO
                HStack {
                    Text("users_subscriptions")
                        .font(.title2)
                    Spacer()
                }
                LazyVGrid(
                    columns: [GridItem(.adaptive(minimum: 300, maximum: 400))],
                    alignment: .leading,
                    spacing: 16
                ) {
                    ForEach(subscriptions, id: \.id) { subscriptionInUser in
                        SubscriptionInUserCard(subscription: subscriptionInUser)
                    }
                }
            }
            .padding()
        }
        .defaultNavigationTitle(fullName)
        .defaultNavigationBackButtonHidden(navigationBackButtonHidden)
        .defaultNavigationImage {
            AsyncImage(
                url: URL(string: ""),
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
        }
        .defaultNavigationToolbar {
            DefaultNavigationBarButton(action: toggleEditing, hasImage: true) {
                Image(systemName: "pencil")
                    .font(.title2)
            }
        }
    }
    
}

#Preview {
    DefaultNavigationView {
        UserDetailsView(
            user: Suitebde_commonsUser(
                id: "userId",
                associationId: "associationId",
                email: "",
                password: nil,
                firstName: "Nathan",
                lastName: "Fallet",
                superuser: true,
                lastLoginAt: Date().asKotlinxInstant
            ),
            isCurrentUser: true,
            subscriptions: [
                Suitebde_commonsSubscriptionInUser(
                    id: "subscriptionId",
                    userId: "userId",
                    subscriptionId: "subscriptionId",
                    startsAt: Date().asKotlinxInstant,
                    endsAt: Date().asKotlinxInstant,
                    subscription: Suitebde_commonsSubscriptionInAssociation(
                        id: "subscriptionId",
                        associationId: "associationId",
                        name: "Subscription name",
                        description: "",
                        price: 10.0,
                        duration: "1y",
                        autoRenewable: false
                    )
                )
            ],
            toggleEditing: {},
            navigationBackButtonHidden: false
        )
    }
}
