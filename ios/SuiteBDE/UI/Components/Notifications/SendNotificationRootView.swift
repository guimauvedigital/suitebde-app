//
//  SendNotificationRootView.swift
//  BDE
//
//  Created by Nathan Fallet on 17/03/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import GuimauveUI

struct SendNotificationRootView: View {
    
    let topics: [(String, String)]
    
    @Binding var notificationTopic: String
    @Binding var notificationTitle: String
    @Binding var notificationBody: String
    @Binding var sent: Bool
    
    let isEnabled: Bool
    let send: () -> Void
    
    var body: some View {
        Form {
            Section {
                Picker("send_notification_field_topic", selection: $notificationTopic) {
                    ForEach(topics, id: \.0) { key, value in
                        Text(value).tag(key)
                    }
                }
                TextField("send_notification_field_title", text: $notificationTitle)
                TextField("send_notification_field_content", text: $notificationBody)
            }
            Section {
                Button(action: send) {
                    Text("send_notification_button")
                }
                .disabled(!isEnabled)
            }
        }
        .alert(isPresented: $sent) {
            Alert(title: Text("send_notification_confirm_dialog"))
        }
        .defaultNavigationTitle("send_notification_title".localized())
        .defaultNavigationBackButtonHidden(false)
    }
    
}

#Preview {
    DefaultNavigationView {
        SendNotificationRootView(
            topics: [
                ("key", "name")
            ],
            notificationTopic: .constant(""),
            notificationTitle: .constant(""),
            notificationBody: .constant(""),
            sent: .constant(false),
            isEnabled: true,
            send: {}
        )
    }
}
