//
//  EventViewModel.swift
//  BDE
//
//  Created by Nathan Fallet on 10/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

class EventViewModel: ObservableObject {
    
    @Published var event: Event
    
    init(event: Event) {
        self.event = event
    }
    
    func onAppear() {
        AnalyticsService.shared.log(.screenView(screenName: "event", screenClass: "EventView"))
    }
    
}
