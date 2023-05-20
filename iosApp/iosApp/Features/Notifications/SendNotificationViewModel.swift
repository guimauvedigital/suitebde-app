//
//  SendNotificationViewModel.swift
//  BDE
//
//  Created by Nathan Fallet on 14/02/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

class SendNotificationViewModel: ObservableObject {
    
    @Published var topic = "broadcast"
    @Published var title = ""
    @Published var body = ""
    @Published var sent = false
    
    func onAppear() {
        AnalyticsService.shared.log(.screenView(screenName: "send_notification", screenClass: "SendNotificationView"))
    }
    
    func send(token: String?) {
        guard let token else {
            return
        }
        Task {
            try await CacheService.shared.apiService().sendNotification(
                token: token,
                payload: NotificationPayload(
                    token: nil,
                    topic: topic,
                    notification: Notification(
                        title: title,
                        body: body
                    )
                )
            )
            DispatchQueue.main.async {
                self.sent = true
            }
        }
    }
    
}
