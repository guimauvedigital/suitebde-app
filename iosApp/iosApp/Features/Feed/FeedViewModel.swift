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
    @Published var isSendNotificationShown = false
    @Published var events = [Event]()
    @Published var topics = [Topic]()
    
    func onAppear() {
        AnalyticsService.shared.log(.screenView(screenName: "feed", screenClass: "FeedView"))
        
        fetchData()
    }
    
    func fetchData() {
        Task {
            let events = try await APIService.shared.getEvents()
            DispatchQueue.main.async {
                self.events = events
            }
        }
        Task {
            let topics = try await APIService.shared.getTopics()
            DispatchQueue.main.async {
                self.topics = topics
            }
        }
    }
    
    func showNewMenu() {
        isNewMenuShown = true
    }
    
    func showSendNotification() {
        isSendNotificationShown = true
    }
    
}
