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
    
    @Published var title: String
    @Published var start: Date
    @Published var end: Date
    @Published var content: String
    @Published var validated: Bool
    
    init(event: Event?, editable: Bool) {
        self.event = event
        self.editable = editable
        self.editing = event == nil
        
        self.title = event?.title ?? ""
        self.start = event?.start?.asDate ?? Date()
        self.end = event?.end?.asDate ?? Date()
        self.content = event?.content ?? ""
        self.validated = event?.validated?.boolValue ?? false
    }
    
    func onAppear() {
        AnalyticsService.shared.log(.screenView(screenName: "event", screenClass: "EventView"))
    }
    
    func toggleEdit() {
        if editable {
            editing.toggle()
        }
    }
    
    func updateInfo(token: String?) {
        guard let token else {
            return
        }
        Task {
            do {
                let event = event != nil ? try await APIService.shared.updateEvent(
                    token: token,
                    id: self.event?.id ?? "",
                    title: title,
                    content: content,
                    start: start.asStringWithTime,
                    end: end.asStringWithTime,
                    topicId: self.event?.topicId,
                    validated: validated
                ) : try await APIService.shared.suggestEvent(
                    token: token,
                    title: title,
                    content: content,
                    start: start.asStringWithTime,
                    end: end.asStringWithTime,
                    topicId: self.event?.topicId
                )
                DispatchQueue.main.async {
                    self.event = event
                }
            } catch {
                print(error.localizedDescription)
            }
        }
    }
    
}
