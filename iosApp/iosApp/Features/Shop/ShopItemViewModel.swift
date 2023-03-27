//
//  ShopItemViewModel.swift
//  BDE
//
//  Created by Nathan Fallet on 27/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

class ShopItemViewModel: ObservableObject {
    
    @Published var item: ShopItem
    @Published var payNow = true
    
    init(item: ShopItem) {
        self.item = item
    }
    
    func onAppear() {
        AnalyticsService.shared.log(.screenView(screenName: "shop_item", screenClass: "ShopItemView"))
    }
    
    func launchBuy() {
        // TODO: Connect to API
    }
    
}
