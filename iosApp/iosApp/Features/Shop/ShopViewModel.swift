//
//  ShopViewModel.swift
//  BDE
//
//  Created by Nathan Fallet on 22/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

class ShopViewModel: ObservableObject {
    
    @Published var ticketConfigurations = [TicketConfiguration]()
    
    func onAppear() {
        AnalyticsService.shared.log(.screenView(screenName: "shop", screenClass: "ShopView"))
        
        fetchData()
    }
    
    func fetchData() {
        Task {
            let ticketConfigurations = try await CacheService.shared.apiService().getTicketConfigurations()
            DispatchQueue.main.async {
                self.ticketConfigurations = ticketConfigurations
            }
        }
    }
    
}
