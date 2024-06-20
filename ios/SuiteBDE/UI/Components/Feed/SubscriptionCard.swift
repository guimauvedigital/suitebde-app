//
//  SubscriptionCard.swift
//  BDE
//
//  Created by Nathan Fallet on 25/02/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct SubscriptionCard: View {
    
    let subscription: CommonsSubscriptionInAssociation
    
    var body: some View {
        VStack(alignment: .leading) {
            Text(subscription.name)
                .font(.title3)
            Spacer()
            HStack(alignment: .bottom) {
                Text(subscription.price.localizedPrice ?? "")
                    .font(.title3)
                Spacer()
                Button(action: {}) {
                    Text("feed_subscriptions_more")
                }
                .buttonStyle(DefaultButtonStyle(tint: .card, textColor: .primary, expands: false))
                .allowsHitTesting(false)
            }
        }
        .padding(12)
        .foregroundColor(.white)
        .multilineTextAlignment(.leading)
        .frame(width: 256, height: 128)
        .background(Color.accentColor)
        .modifier(CardStyle())
    }
    
}

#Preview {
    SubscriptionCard(
        subscription: CommonsSubscriptionInAssociation(
            id: CoreUUID(),
            associationId: CoreUUID(),
            name: "Cotisation pour la scolarité",
            description: "Cool",
            price: 85,
            duration: "1y",
            autoRenewable: false
        )
    )
}
