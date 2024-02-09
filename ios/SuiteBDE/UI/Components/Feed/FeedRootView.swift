//
//  FeedRootView.swift
//  BDE
//
//  Created by Nathan Fallet on 08/02/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct FeedRootView<OldBefore>: View where OldBefore : View {
    
    @State var isMenuShown: Bool = false
    @State var isSuggestEventShown: Bool = false
    @State var isSendNotificationShown: Bool = false
    
    let oldBeforeView: OldBefore
    
    let events: [Suitebde_commonsEvent]
    
    let sendNotificationVisible: Bool
    
    var body: some View {
        ScrollView {
            LazyVStack(alignment: .leading, spacing: 0) {
                oldBeforeView
                
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
            .padding(.vertical)
            
            Group {
                DefaultNavigationLink(
                    isActive: $isSuggestEventShown,
                    destination: EventView(viewModel: KoinApplication.shared.koin.eventViewModel(id: nil))
                )
                DefaultNavigationLink(
                    isActive: $isSendNotificationShown,
                    destination: SendNotificationView()
                )
            }
        }
        .defaultNavigationTitle("feed_title".localized())
        .defaultNavigationToolbar {
            Button(action: { isMenuShown.toggle() }) {
                Image(systemName: "plus")
            }
            .confirmationDialog("feed_actions", isPresented: $isMenuShown) {
                Button("feed_events_suggest") {
                    isSuggestEventShown.toggle()
                }
                if sendNotificationVisible {
                    Button("feed_notifications_send") {
                        isSendNotificationShown.toggle()
                    }
                }
            }
            DefaultNavigationLink(destination: SettingsView()) {
                Image(systemName: "gearshape")
            }
        }
    }
    
}

#Preview {
    DefaultNavigationView {
        FeedRootView(
            oldBeforeView: EmptyView(),
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
            ],
            sendNotificationVisible: true
        )
    }
}
