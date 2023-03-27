//
//  ShopCard.swift
//  BDE
//
//  Created by Nathan Fallet on 22/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct ShopCard: View {
    
    let item: ShopItem
    let detailsEnabled: Bool
    let cotisant: Bool
    
    @State var showDetails = false
    
    var body: some View {
        NavigationLink(
            destination: ShopItemView(viewModel: ShopItemViewModel(
                item: item
            )),
            isActive: $showDetails
        ) {
            VStack(alignment: .leading, spacing: 8) {
                Text(item.title ?? "")
                    .fontWeight(.bold)
                if detailsEnabled {
                    if cotisant {
                        HStack {
                            Text("\(item.price ?? 0)€")
                                .strikethrough()
                            Text("\(item.priceReduced ?? 0)€")
                                .fontWeight(.bold)
                        }
                    } else {
                        Text("\(item.price ?? 0)€")
                    }
                }
                Text(item.content ?? "")
                    .lineLimit(detailsEnabled ? 5 : nil)
            }
            .foregroundColor(.primary)
            .multilineTextAlignment(.leading)
            .cardView()
            .onTapGesture {
                if detailsEnabled {
                    showDetails = true
                }
            }
        }
    }
    
}
