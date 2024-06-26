//
//  FeedRootView.swift
//  BDE
//
//  Created by Nathan Fallet on 08/02/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import GuimauveUI
import shared

struct FeedRootView: View {
    
    @State var isMenuShown: Bool = false
    @State var isSuggestEventShown: Bool = false
    @State var isSendNotificationShown: Bool = false
    
    @Binding var search: String
    
    let subscriptions: [CommonsSubscriptionInAssociation]
    let events: [CommonsEvent]
    
    let sendNotificationVisible: Bool
    let showScannerVisible: Bool
    
    let onOpenURL: (URL) -> Void
    
    let users: [CommonsUser]
    let clubs: [CommonsClub]
    
    let hasMoreUsers: Bool
    let hasMoreClubs: Bool
    
    let loadMoreUsers: () -> Void
    let loadMoreClubs: () -> Void
    
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 0) {
                TextField("app_search", text: $search)
                    .textFieldStyle(DefaultInputStyle(icon: "magnifyingglass"))
                    .submitLabel(.search)
                    .padding(.horizontal)
                
                if !search.trim().isEmpty {
                    FeedSearchView(
                        users: users,
                        clubs: clubs,
                        hasMoreUsers: hasMoreUsers,
                        hasMoreClubs: hasMoreClubs,
                        loadMoreUsers: loadMoreUsers,
                        loadMoreClubs: loadMoreClubs
                    )
                } else {
                    FeedHomeView(
                        subscriptions: subscriptions,
                        events: events
                    )
                    .padding(.top)
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
            if showScannerVisible {
                DefaultNavigationLink(
                    destination:ScannerView(viewModel: ScannerViewModel(
                        onURLFound: onOpenURL
                    ))
                ) {
                    Image(systemName: "qrcode.viewfinder")
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
            search: .constant(""),
            subscriptions: [
                CommonsSubscriptionInAssociation(
                    id: CoreUUID(),
                    associationId: CoreUUID(),
                    name: "Cotisation pour la scolarité",
                    description: "Cool",
                    price: 85,
                    duration: "1y",
                    autoRenewable: false
                ),
                CommonsSubscriptionInAssociation(
                    id: CoreUUID(),
                    associationId: CoreUUID(),
                    name: "Cotisation pour l'année",
                    description: "Cool",
                    price: 35,
                    duration: "1y",
                    autoRenewable: false
                )
            ],
            events: [
                CommonsEvent(
                    id: CoreUUID(),
                    associationId: CoreUUID(),
                    name: "Vente de crèpes",
                    description: "A cool event",
                    image: "https://images.unsplash.com/photo-1637036124732-cb0fab13bb15?q=80&w=1887&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    startsAt: Date().asKotlinxInstant,
                    endsAt: Date().asKotlinxInstant,
                    validated: true
                ),
                CommonsEvent(
                    id: CoreUUID(),
                    associationId: CoreUUID(),
                    name: "Assemblée générale",
                    description: "A cool event",
                    image: "https://images.unsplash.com/photo-1492538368677-f6e0afe31dcc?q=80&w=1740&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    startsAt: Date().asKotlinxInstant,
                    endsAt: Date().asKotlinxInstant,
                    validated: true
                )
            ],
            sendNotificationVisible: true,
            showScannerVisible: true,
            onOpenURL: { _ in },
            users: [],
            clubs: [
                CommonsClub(
                    id: CoreUUID(),
                    associationId: CoreUUID(),
                    name: "Club running",
                    description: "",
                    logo: "https://bdensisa.org/clubs/rev4fkzzd79u7glwk0l1agdoovm3s7yo/uploads/logo%20club%20run.jpeg",
                    createdAt: Date().asKotlinxInstant,
                    validated: true,
                    usersCount: 12,
                    isMember: true
                )
            ],
            hasMoreUsers: true,
            hasMoreClubs: true,
            loadMoreUsers: {},
            loadMoreClubs: {}
        )
    }
}
