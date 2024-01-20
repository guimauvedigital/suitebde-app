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
    
    var body: some View {
        HStack(spacing: 12) {
            Image(systemName: "calendar.circle")
                .resizable()
                .frame(width: 44, height: 44)
            VStack(alignment: .leading) {
                Text(event.name)
                Text(event.renderedDate)
                    .foregroundColor(.secondary)
            }
            Spacer()
        }
        .foregroundColor(.primary)
        .multilineTextAlignment(.leading)
        .cardView()
    }
    
}
