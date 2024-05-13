//
//  SubscriptionInUserCard.swift
//  BDE
//
//  Created by Nathan Fallet on 13/05/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct SubscriptionInUserCard: View {
    
    let subscription: Suitebde_commonsSubscriptionInUser
    
    var isSubscriptionValid: Bool {
        subscription.endsAt.asDate >= Date()
    }
    
    var body: some View {
        HStack(spacing: 12) {
            VStack(alignment: .leading) {
                Text(subscription.subscription?.name ?? "")
                    .lineLimit(1)
                Text("users_subscriptions_valid_unti".localized().format(subscription.endsAt.renderedDate))
                    .lineLimit(1)
                    .foregroundStyle(.secondary)
            }
            .padding()
            Spacer()
            Image(systemName: isSubscriptionValid ? "checkmark.circle.fill" : "multiply.circle.fill")
                .resizable()
                .frame(width: 32, height: 32)
                .foregroundStyle(isSubscriptionValid ? .green : .red)
                .padding()
        }
        .foregroundColor(.primary)
        .multilineTextAlignment(.leading)
        .modifier(CardStyle())
    }
    
}

#Preview {
    SubscriptionInUserCard(
        subscription: Suitebde_commonsSubscriptionInUser(
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
    )
}
