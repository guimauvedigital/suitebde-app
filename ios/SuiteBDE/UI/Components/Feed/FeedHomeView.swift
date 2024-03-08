//
//  FeedHomeView.swift
//  BDE
//
//  Created by Nathan Fallet on 08/03/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct FeedHomeView: View {
    
    let subscriptions: [Suitebde_commonsSubscriptionInAssociation]
    let events: [Suitebde_commonsEvent]
    
    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            Text("qrcode_title")
                .font(.title2)
                .padding(.horizontal)
            
            DefaultNavigationLink(
                destination: QRCodeView()
            ) {
                QRCodeCard()
                    .padding()
            }
            
            if !subscriptions.isEmpty {
                Text("feed_subscriptions")
                    .font(.title2)
                    .padding(.horizontal)
                ScrollView(.horizontal, showsIndicators: false) {
                    LazyHStack(spacing: 16) {
                        ForEach(subscriptions, id: \.id) { subscription in
                            DefaultNavigationLink(destination: SubscriptionView(
                                viewModel: KoinApplication.shared.koin.subscriptionViewModel(id: subscription.id)
                            )) {
                                SubscriptionCard(subscription: subscription)
                            }
                        }
                    }
                    .padding()
                }
            }
            
            if !events.isEmpty {
                Text("feed_events")
                    .font(.title2)
                    .padding(.horizontal)
                ScrollView(.horizontal, showsIndicators: false) {
                    LazyHStack(spacing: 16) {
                        ForEach(events, id: \.id) { event in
                            DefaultNavigationLink(destination: EventView(
                                viewModel: KoinApplication.shared.koin.eventViewModel(id: event.id)
                            )) {
                                EventCard(event: event)
                            }
                        }
                    }
                    .padding()
                }
            }
        }
    }
    
}

#Preview {
    ScrollView {
        FeedHomeView(
            subscriptions: [
                Suitebde_commonsSubscriptionInAssociation(
                    id: "id",
                    associationId: "associationId",
                    name: "Cotisation pour la scolarité",
                    description: "Cool",
                    price: 85,
                    duration: "1y",
                    autoRenewable: false
                ),
                Suitebde_commonsSubscriptionInAssociation(
                    id: "id2",
                    associationId: "associationId",
                    name: "Cotisation pour l'année",
                    description: "Cool",
                    price: 35,
                    duration: "1y",
                    autoRenewable: false
                )
            ],
            events: [
                Suitebde_commonsEvent(
                    id: "id",
                    associationId: "associationId",
                    name: "Vente de crèpes",
                    description: "A cool event",
                    image: "https://images.unsplash.com/photo-1637036124732-cb0fab13bb15?q=80&w=1887&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    startsAt: Date().asKotlinxInstant,
                    endsAt: Date().asKotlinxInstant,
                    validated: true
                ),
                Suitebde_commonsEvent(
                    id: "id2",
                    associationId: "associationId",
                    name: "Assemblée générale",
                    description: "A cool event",
                    image: "https://images.unsplash.com/photo-1492538368677-f6e0afe31dcc?q=80&w=1740&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    startsAt: Date().asKotlinxInstant,
                    endsAt: Date().asKotlinxInstant,
                    validated: true
                )
            ]
        )
    }
}
