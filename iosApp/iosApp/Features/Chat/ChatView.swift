//
//  ChatView.swift
//  BDE
//
//  Created by Nathan Fallet on 07/07/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import Kingfisher

struct ChatView: View {
    
    @EnvironmentObject var rootViewModel: RootViewModel
    @StateObject var viewModel: ChatViewModel
    
    var body: some View {
        NavigationView {
            List(viewModel.conversations, id: \.id) { conversation in
                NavigationLink(destination: ConversationView(viewModel: ConversationViewModel(
                    conversation: conversation,
                    parentViewModel: viewModel
                ))) {
                    HStack(spacing: 12) {
                        if let logo = conversation.logo, let url = URL(string: logo) {
                            KFImage(url)
                                .placeholder { conversation.name.chatLogo(backup: conversation.backupLogo) }
                                .resizable()
                                .aspectRatio(contentMode: .fill)
                                .frame(width: 44, height: 44)
                                .cornerRadius(4)
                        } else {
                            Rectangle()
                                .fill(.clear)
                                .frame(width: 44, height: 44)
                                .overlay(conversation.name.chatLogo(backup: conversation.backupLogo).cornerRadius(4))
                        }
                        VStack(alignment: .leading) {
                            Text(conversation.name)
                                .fontWeight(.bold)
                            if let lastMessage = conversation.lastMessage {
                                Text(lastMessage.content ?? "")
                                    .fontWeight(conversation.isUnread ? .bold : .none)
                                    .lineLimit(1)
                            } else {
                                Text("Aucun message")
                                    .foregroundColor(.secondary)
                            }
                        }
                    }
                }
            }
            .listStyle(.plain)
            .onAppear {
                viewModel.onAppear(token: rootViewModel.token)
            }
            .refreshable {
                viewModel.onAppear(token: rootViewModel.token)
            }
            .navigationTitle(Text("Conversations"))
        }
        .navigationViewStyle(DoubleColumnNavigationViewStyle())
        .badge(viewModel.conversations.filter { $0.isUnread }.count)
    }
    
}
