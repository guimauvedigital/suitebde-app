//
//  CalendarViewModel.swift
//  BDE
//
//  Created by Nathan Fallet on 19/05/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

class CalendarViewModel: ObservableObject {
    
    @Published var day = Date() { didSet { updateTodayEvents() }}
    @Published var events = [CalendarEvent]() { didSet { updateTodayEvents() }}
    @Published var todayEvents = [CalendarEvent]()
    
    func onAppear(token: String?) {
        AnalyticsService.shared.log(.screenView(screenName: "calendar", screenClass: "CalendarView"))
        
        fetchData(token: token, reload: false)
    }
    
    func fetchData(token: String?, reload: Bool) {
        Task {
            let events = try await CacheService.shared.getEvents(token: token, reload: reload)
            DispatchQueue.main.async {
                self.events = events
            }
            if !reload {
                let events = try await CacheService.shared.getEvents(token: token, reload: true)
                DispatchQueue.main.async {
                    self.events = events
                }
            }
        }
    }
    
    func updateTodayEvents() {
        let components = Calendar.current.dateComponents([.day, .month, .year], from: day)
        todayEvents = events.filter({ event in
            Calendar.current.dateComponents([.day, .month, .year], from: event.start?.asDate ?? Date()) == components ||
            Calendar.current.dateComponents([.day, .month, .year], from: event.end?.asDate ?? Date()) == components
        })
    }
    
    func previous() {
        day = Calendar.current.date(byAdding: .day, value: -1, to: day) ?? Date()
    }
    
    func next() {
        day = Calendar.current.date(byAdding: .day, value: 1, to: day) ?? Date()
    }
    
}
