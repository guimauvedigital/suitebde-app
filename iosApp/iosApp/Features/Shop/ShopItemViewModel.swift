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
    @Published var loading = false
    @Published var error = false
    @Published var success: String?
    
    init(item: ShopItem) {
        self.item = item
    }
    
    func onAppear() {
        AnalyticsService.shared.log(.screenView(screenName: "shop_item", screenClass: "ShopItemView"))
    }
    
    func launchBuy(token: String?) {
        guard let token else {
            return
        }
        loading = true
        Task {
            do {
                try await APIService.shared.createShopItem(
                    token: token,
                    type: item.type,
                    id: item.id
                )
                DispatchQueue.main.async {
                    self.loading = false
                    self.success = "Votre ticket a bien été réservé. Merci de bien vouloir vous présenter à un membre du BDE pour le régler."
                    // TODO: If `payNow`, redirect to payement
                }
            } catch {
                DispatchQueue.main.async {
                    self.error = true
                    self.loading = false
                }
            }
        }
    }
    
}
