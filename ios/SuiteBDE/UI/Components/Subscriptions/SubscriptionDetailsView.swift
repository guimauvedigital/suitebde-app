//
//  SubscriptionDetailsView.swift
//  BDE
//
//  Created by Nathan Fallet on 25/02/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct SubscriptionDetailsView: View {
    
    let subscription: Suitebde_commonsSubscriptionInAssociation
    
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
}
