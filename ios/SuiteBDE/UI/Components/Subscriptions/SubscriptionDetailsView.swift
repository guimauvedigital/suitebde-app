//
//  SubscriptionDetailsView.swift
//  BDE
//
//  Created by Nathan Fallet on 25/02/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import GuimauveUI
import shared

struct SubscriptionDetailsView: View {
    
    let subscription: CommonsSubscriptionInAssociation
    let buy: () -> Void
    
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 16) {
                VStack(alignment: .leading) {
                    HStack {
                        Text(subscription.name)
                            .font(.title2)
                        Spacer()
                    }
                    Text(subscription.price.localizedPrice ?? "")
                        .foregroundStyle(.secondary)
                }
                
                Text(subscription.description_)
                    .multilineTextAlignment(.leading)
                
                Button("subscriptions_buy", action: buy)
                    .buttonStyle(DefaultButtonStyle(filled: true))
            }
            .padding()
        }
        .defaultNavigationTitle("subscriptions_title".localized())
        .defaultNavigationBackButtonHidden(false)
    }
    
}

#Preview {
    DefaultNavigationView {
        SubscriptionDetailsView(
            subscription: CommonsSubscriptionInAssociation(
                id: CoreUUID(),
                associationId: CoreUUID(),
                name: "Cotisation pour la scolarité",
                description: "Cool",
                price: 85,
                duration: "1y",
                autoRenewable: false
            ),
            buy: {}
        )
    }
}
