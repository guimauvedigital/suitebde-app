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
        Form {
            Section("Paramètres") {
                Picker(
                    "Sujet",
                    selection: Binding(get: { viewModel.topic ?? "" }, set: viewModel.updateTopic)
                ) {
                    // TODO: Find how to iterate that shit
                    //ForEach(viewModel.topics?.topics, \.self) { entry in
                        
                    //}
                    Text("Général").tag("broadcast")
                    Text("Cotisants").tag("cotisants")
                    Text("Evènements").tag("events")
                }
                TextField(
                    "Titre",
                    text: Binding(get: { viewModel.title }, set: viewModel.updateTitle)
                )
                TextField(
                    "Contenu",
                    text: Binding(get: { viewModel.body }, set: viewModel.updateBody)
                )
            }
            Section {
                Button("Envoyer") {
                    Task {
                        try await asyncFunction(for: viewModel.send())
                    }
                }
                .disabled(viewModel.topic?.isEmpty ?? false || viewModel.title.isEmpty || viewModel.body.isEmpty)
            }
        }
        .alert(isPresented: Binding(get: { viewModel.sent }, set: { _ in viewModel.dismiss() })) {
            Alert(title: Text("Notification envoyée !"))
        }
        .navigationTitle(Text("Envoi de notification"))
        .onAppear {
            Task {
                try await asyncFunction(for: viewModel.onAppear())
            }
        }
    }
    
}
