//
//  SendNotificationView.swift
//  BDE
//
//  Created by Nathan Fallet on 14/02/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct SendNotificationView: View {
    
    @EnvironmentObject var rootViewModel: OldRootViewModel
    @StateObject var viewModel = SendNotificationViewModel()
    
    var body: some View {
        Form {
            Section("Paramètres") {
                Picker("Sujet", selection: $viewModel.topic) {
                    Text("Général").tag("broadcast")
                    Text("Cotisants").tag("cotisants")
                    Text("Evènements").tag("events")
                }
                TextField("Titre", text: $viewModel.title)
                TextField("Contenu", text: $viewModel.body)
            }
            Section {
                Button("Envoyer") {
                    viewModel.send(token: rootViewModel.token)
                }
                .disabled(viewModel.title.isEmpty || viewModel.body.isEmpty)
            }
        }
        .alert(isPresented: $viewModel.sent) {
            Alert(title: Text("Notification envoyée !"))
        }
        .navigationTitle(Text("Envoi de notification"))
        .onAppear(perform: viewModel.onAppear)
    }
    
}

struct SendNotificationView_Previews: PreviewProvider {
    
    static var previews: some View {
        SendNotificationView()
    }
    
}
