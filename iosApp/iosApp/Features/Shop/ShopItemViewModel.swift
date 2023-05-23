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
    @Published var error: String?
    @Published var success: String?
    @Published var url: String?
    
    init(item: ShopItem) {
        self.item = item
    }
    
    func onAppear() {
        AnalyticsService.shared.log(.screenView(screenName: "shop_item", screenClass: "ShopItemView"))
    }
    
    func launchBuy(token: String?) {
        guard let token else {
            self.error = "Vous devez vous connecter à votre compte avant d'effectuer un achat."
            return
        }
        loading = true
        Task {
            do {
                let response = try await CacheService.shared.apiService().createShopItem(
                    token: token,
                    type: item.type,
                    id: item.id
                )
                DispatchQueue.main.async {
                    self.loading = false
                    if self.payNow, let url = response.url {
                        self.url = url
                    } else {
                        self.success = "Votre commande a bien été enregistrée, merci de bien vouloir vous présenter à un membre du BDE pour la régler."
                    }
                }
            } catch {
                DispatchQueue.main.async {
                    self.error = "Vérifiez que vous êtes bien connecté à internet, que cet élément est encore disponible et que vous ne l'avez pas déjà acheté."
                    self.loading = false
                }
            }
        }
    }
    
}
