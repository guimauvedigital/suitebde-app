//
//  FeedViewModel.swift
//  BDE
//
//  Created by Nathan Fallet on 31/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

class FeedViewModel: ObservableObject {
    
    @Published var isNewMenuShown = false
    @Published var isNewEventShown = false
    @Published var isSendNotificationShown = false
    @Published var events = [Event]()
    @Published var topics = [Topic]()
    @Published var cotisantConfigurations = [CotisantConfiguration]()
    @Published var ticketConfigurations = [TicketConfiguration]()
    
    func onAppear() {
        AnalyticsService.shared.log(.screenView(screenName: "feed", screenClass: "FeedView"))
        
        fetchData()
    }
    
    func fetchData() {
        Task {
            let events = try await CacheService.shared.apiService().getEvents(offset: 0, limit: 10)
            DispatchQueue.main.async {
                self.events = events
            }
        }
        Task {
            let topics = try await CacheService.shared.apiService().getTopics(offset: 0, limit: 10)
            DispatchQueue.main.async {
                self.topics = topics
            }
        }
        Task {
            let cotisantConfigurations = try await CacheService.shared.apiService().getCotisantConfigurations()
            DispatchQueue.main.async {
                self.cotisantConfigurations = cotisantConfigurations
            }
        }
        Task {
            let ticketConfigurations = try await CacheService.shared.apiService().getTicketConfigurations()
            DispatchQueue.main.async {
                self.ticketConfigurations = ticketConfigurations
            }
        }
    }
    
    func showNewMenu() {
        isNewMenuShown = true
    }
    
    func showNewEvent() {
        isNewEventShown = true
    }
    
    func showSendNotification() {
        isSendNotificationShown = true
    }
    
}
