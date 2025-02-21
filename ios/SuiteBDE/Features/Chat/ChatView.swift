//
//  ChatView.swift
//  BDE
//
//  Created by Nathan Fallet on 07/07/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct ChatView: View {
    
    @Environment(\.scenePhase) var scenePhase
    
    @EnvironmentObject var rootViewModel: OldRootViewModel
    @StateObject var viewModel = ChatViewModel()
    
    var body: some View {
        NavigationView {
            List(viewModel.conversations, id: \.id) { conversation in
                NavigationLink(destination: ConversationView(viewModel: ConversationViewModel(
                    conversation: conversation
                ))) {
                    HStack(spacing: 12) {
                        AsyncImage(
                            url: URL(string: conversation.logo ?? ""),
                            content: { image in
                                image
                                    .resizable()
                                    .aspectRatio(contentMode: .fill)
                            },
                            placeholder: {
                                Color(UIColor.systemGray3)
                                    .overlay {
                                        Text(conversation.name.initials)
                                            .font(.title)
                                    }
                            }
                        )
                        .frame(width: 44, height: 44)
                        .cornerRadius(4)
                        VStack(alignment: .leading, spacing: 2) {
                            HStack {
                                Text(conversation.name)
                                    .lineLimit(1)
                                if conversation.membership?.notifications == false {
                                    Image(systemName: "bell.slash.fill")
                                        .resizable()
                                        .frame(width: 12, height: 12)
                                        .foregroundColor(.secondary)
                                }
                            }
                            Text(conversation.lastMessage?.content ?? "Aucun message")
                                .foregroundColor(.secondary)
                                .fontWeight(conversation.isUnread ? .bold : .none)
                                .lineLimit(1)
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
            .onChange(of: scenePhase) { newPhase in
                if newPhase == .active {
                    viewModel.onAppear(token: rootViewModel.token)
                }
            }
            .navigationTitle(Text("Conversations"))
        }
        .navigationViewStyle(DoubleColumnNavigationViewStyle())
        .badge(viewModel.conversations.filter { $0.isUnread }.count)
    }
    
}
