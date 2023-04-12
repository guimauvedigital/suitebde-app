//
//  ShopItemPrice.swift
//  BDE
//
//  Created by Nathan Fallet on 12/04/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct ShopItemPrice: View {
    
    let item: ShopItem
    let cotisant: Bool
    
    var body: some View {
        if cotisant && item.priceReduced != item.price {
            HStack {
                Text(item.price?.localizedPrice ?? "?")
                    .strikethrough()
                Text(item.priceReduced?.localizedPrice ?? "?")
                    .fontWeight(.bold)
            }
        } else {
            Text(item.price?.localizedPrice ?? "?")
        }
    }
    
}
