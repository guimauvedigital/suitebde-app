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
    
    @Published var events = [Event]()
    
    func onAppear() {
        fetchEvents()
    }
    
    func fetchEvents() {
        Task {
            let events = try await APIService.shared.getEvents()
            DispatchQueue.main.async {
                self.events = events
            }
        }
    }
    
}
