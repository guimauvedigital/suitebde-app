//
//  EventDetailsView.swift
//  BDE
//
//  Created by Nathan Fallet on 08/02/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import GuimauveUI
import shared

struct EventDetailsView: View {
    
    let event: CommonsEvent
    let toggleEditing: () -> Void
    
    var startDay: String {
        let formatter = DateFormatter()
        formatter.dateStyle = .long
        return formatter.string(from: event.startsAt.asDate)
    }
    
    var endDay: String {
        let formatter = DateFormatter()
        formatter.dateStyle = .long
        return formatter.string(from: event.endsAt.asDate)
    }
    
    var startTime: String {
        let formatter = DateFormatter()
        formatter.timeStyle = .short
        return formatter.string(from: event.startsAt.asDate)
    }
    
    var endTime: String {
        let formatter = DateFormatter()
        formatter.timeStyle = .short
        return formatter.string(from: event.endsAt.asDate)
    }
    
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 16) {
                HStack {
                    Text("events_date")
                        .font(.title2)
                    Spacer()
                }
                
                VStack(alignment: .leading) {
                    Text(startDay != endDay ? "\(startDay) - \(endDay)" : startDay)
                    Text(
                        startTime != endTime || startDay != endDay ?
                         "\(startTime) - \(endTime)" : startTime
                    )
                        .foregroundStyle(.secondary)
                }
                
                Text(event.description_)
                    .multilineTextAlignment(.leading)
            }
            .padding()
        }
        .defaultNavigationTitle(event.name)
        .defaultNavigationBackButtonHidden(false)
        .defaultNavigationImage {
            AsyncImage(
                url: URL(string: event.image ?? ""),
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
        EventDetailsView(
            event: CommonsEvent(
                id: CoreUUID(),
                associationId: CoreUUID(),
                name: "Vente de crèpes",
                description: "A cool event",
                image: "https://images.unsplash.com/photo-1637036124732-cb0fab13bb15?q=80&w=1887&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                startsAt: Date().asKotlinxInstant,
                endsAt: Date().asKotlinxInstant,
                validated: true
            ),
            toggleEditing: {}
        )
    }
}
