//
//  FeedViewModel.swift
//  BDE
//
//  Created by Nathan Fallet on 31/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

class OldFeedViewModel: ObservableObject {
    
    @Published var ticketConfigurations = [TicketConfiguration]()
    
    func onAppear() {
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
