//
//  UserDetailsView.swift
//  BDE
//
//  Created by Nathan Fallet on 20/03/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import GuimauveUI
import shared

struct UserDetailsView: View {
    
    let user: CommonsUser
    let isCurrentUser: Bool
    let subscriptions: [CommonsSubscriptionInUser]
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
            isCurrentUser: true,
            subscriptions: [
                CommonsSubscriptionInUser(
                    id: CoreUUID(),
                    userId: CoreUUID(),
                    subscriptionId: CoreUUID(),
                    startsAt: Date().asKotlinxInstant,
                    endsAt: Date().asKotlinxInstant,
                    subscription: CommonsSubscriptionInAssociation(
                        id: CoreUUID(),
                        associationId: CoreUUID(),
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
