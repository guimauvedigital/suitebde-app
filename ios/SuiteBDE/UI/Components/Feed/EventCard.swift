//
//  EventCard.swift
//  BDE
//
//  Created by Nathan Fallet on 20/01/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct EventCard: View {
    
    let event: Suitebde_commonsEvent
    
    var day: String {
        let formatter = DateFormatter()
        formatter.dateFormat = "d"
        return formatter.string(from: event.startsAt.asDate)
    }
    
    var month: String {
        let formatter = DateFormatter()
        formatter.dateFormat = "MMM"
        return formatter.string(from: event.startsAt.asDate)
    }
    
    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            ZStack(alignment: .bottomLeading) {
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
                .frame(width: 256, height: 128)
                .clipped()
                VStack {
                    Text(day)
                    Text(month)
                        .foregroundStyle(.secondary)
                }
                .font(.callout)
                .padding(.horizontal, 8)
                .padding(.vertical, 4)
                .background(Color.card)
                .cornerRadius(8)
                .padding(12)
            }
            VStack(alignment: .leading) {
                Text(event.name)
                    .lineLimit(1)
                Text(event.renderedDate)
                    .foregroundStyle(.secondary)
                    .lineLimit(1)
            }
            .padding(12)
        }
        .foregroundColor(.primary)
        .multilineTextAlignment(.leading)
        .modifier(CardStyle())
        .frame(width: 256)
    }
    
}

#Preview {
    EventCard(event: Suitebde_commonsEvent(
        id: "id",
        associationId: "associationId",
        name: "Vente de crèpes",
        description: "A cool event",
        image: "https://images.unsplash.com/photo-1637036124732-cb0fab13bb15?q=80&w=1887&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        startsAt: Date().asKotlinxInstant,
        endsAt: Date().asKotlinxInstant,
        validated: true
    ))
}
