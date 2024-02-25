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
    
    let subscription: Suitebde_commonsSubscriptionInAssociation
    
    var body: some View {
        VStack(alignment: .leading) {
            Text(subscription.name)
                .lineLimit(1)
            Text(subscription.price.localizedPrice ?? "")
                .foregroundStyle(.secondary)
        }
        .padding(12)
        .foregroundColor(.primary)
        .multilineTextAlignment(.leading)
        .modifier(CardStyle())
    }
    
}

#Preview {
    SubscriptionCard(
        subscription: Suitebde_commonsSubscriptionInAssociation(
            id: "id",
            associationId: "associationId",
            name: "Cotisation pour la scolarité",
            description: "Cool",
            price: 85,
            duration: "1y",
            autoRenewable: false
        )
    )
}
