//
//  SendNotificationView.swift
//  BDE
//
//  Created by Nathan Fallet on 14/02/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared
import KMMViewModelSwiftUI
import KMPNativeCoroutinesAsync

struct SendNotificationView: View {
    
    @InjectStateViewModel var viewModel: SendNotificationViewModel
    
    var body: some View {
        SendNotificationRootView(
            topics: viewModel.topics?.topics.map { ($0.0, $0.1) } ?? [],
            notificationTopic: Binding(get: { viewModel.topic ?? "" }, set: viewModel.updateTopic),
            notificationTitle: Binding(get: { viewModel.title }, set: viewModel.updateTitle),
            notificationBody: Binding(get: { viewModel.body }, set: viewModel.updateBody),
            sent: Binding(get: { viewModel.sent }, set: { _ in viewModel.dismiss() }),
            isEnabled: viewModel.topic?.isEmpty == false && !viewModel.title.isEmpty && !viewModel.body.isEmpty,
            send: {
                Task {
                    try await asyncFunction(for: viewModel.send())
                }
            }
        )
        .onAppear {
            Task {
                try await asyncFunction(for: viewModel.onAppear())
            }
        }
    }
    
}
