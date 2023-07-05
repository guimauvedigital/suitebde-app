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
    
    @Published var event: Event?
    @Published var editable: Bool
    @Published var editing: Bool
    
    @Published var hasUnsavedChanges = false
    @Published var alert: AlertCase? {
        willSet {
            if alert == .saved && newValue == nil {
                self.hasUnsavedChanges = false
            }
        }
    }
    
    @Published var title = "" { didSet { hasUnsavedChanges = true }}
    @Published var start = Date() { didSet { hasUnsavedChanges = true }}
    @Published var end = Date() { didSet { hasUnsavedChanges = true }}
    @Published var content = "" { didSet { hasUnsavedChanges = true }}
    @Published var validated = false { didSet { hasUnsavedChanges = true }}
    
    init(event: Event?, editable: Bool) {
        self.event = event
        self.editable = editable
        self.editing = event == nil
        
        resetChanges()
    }
    
    func resetChanges() {
        self.title = event?.title ?? ""
        self.start = event?.start?.asDate ?? Date()
        self.end = event?.end?.asDate ?? Date()
        self.content = event?.content ?? ""
        self.validated = event?.validated?.boolValue ?? false
        
        self.hasUnsavedChanges = false
    }
    
    func onAppear() {
        AnalyticsService.shared.log(.screenView(screenName: "event", screenClass: "EventView"))
    }
    
    func toggleEdit() {
        if editable {
            if editing && hasUnsavedChanges {
                alert = .cancelling
                return
            }
            editing.toggle()
            if editing {
                resetChanges()
            }
        }
    }
    
    func discardEdit() {
        editing = false
    }
    
    func updateInfo(token: String?) {
        guard let token else {
            return
        }
        Task {
            let event = event != nil ? try await CacheService.shared.apiService().updateEvent(
                token: token,
                id: self.event?.id ?? "",
                title: title,
                content: content,
                start: start.asStringWithTime,
                end: end.asStringWithTime,
                topicId: self.event?.topicId,
                validated: validated
            ) : try await CacheService.shared.apiService().suggestEvent(
                token: token,
                title: title,
                content: content,
                start: start.asStringWithTime,
                end: end.asStringWithTime,
                topicId: self.event?.topicId
            )
            DispatchQueue.main.async {
                self.event = event
                self.alert = .saved
            }
        }
    }
    
}
