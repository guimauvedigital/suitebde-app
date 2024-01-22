//
//  ConversationSettingsView.swift
//  BDE
//
//  Created by Nathan Fallet on 07/07/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct ConversationSettingsView: View {
    
    @EnvironmentObject var rootViewModel: OldRootViewModel
    @StateObject var viewModel: ConversationSettingsViewModel
    
    var body: some View {
        Form {
            Section(header: Text("Notifications")) {
                Toggle("Activer les notifications", isOn: $viewModel.notifications)
            }
            Section(header: Text("Dans la conversation")) {
                ForEach(viewModel.members, id: \.id) { member in
                    VStack(alignment: .leading) {
                        Text("\(member.firstName ?? "?") \(member.lastName ?? "?")")
                            .lineLimit(1)
                        Text(member.description_)
                            .font(.callout)
                            .foregroundColor(.secondary)
                            .lineLimit(1)
                    }
                }
            }
        }
        .onChange(of: viewModel.notifications) { _ in
            viewModel.updateMembership(token: rootViewModel.token)
        }
        .onAppear {
            viewModel.onAppear(token: rootViewModel.token)
        }
        .navigationTitle(Text("Paramètres de la conversation"))
    }
    
}
