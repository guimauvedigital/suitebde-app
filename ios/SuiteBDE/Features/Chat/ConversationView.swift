//
//  ConversationView.swift
//  BDE
//
//  Created by Nathan Fallet on 07/07/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct ConversationView: View {
    
    @Environment(\.scenePhase) var scenePhase
    
    @EnvironmentObject var rootViewModel: OldRootViewModel
    @StateObject var viewModel: ConversationViewModel
    
    var body: some View {
        ZStack {
            ScrollView {
                ScrollViewReader { reader in
                    LazyVStack {
                        ForEach(viewModel.messages.reversed(), id: \.id) { message in
                            let previousMessage = viewModel.messages.first {
                                $0.createdAt?.asDate ?? Date() < message.createdAt?.asDate ?? Date()
                            }
                            MessageView(
                                message: message,
                                isHeaderShown: previousMessage?.type == "system" || message.userId != previousMessage?.userId,
                                viewedBy: rootViewModel.user
                            )
                            .id(message.id)
                            .onAppear {
                                if viewModel.scrollTo == nil {
                                    viewModel.loadMore(token: rootViewModel.token, id: message.id)
                                }
                                if message.createdAt?.asDate ?? Date() > viewModel.conversation.membership?.lastRead.asDate ?? Date() {
                                    viewModel.markAsRead(token: rootViewModel.token)
                                }
                            }
                        }
                        ForEach(viewModel.sendingMessages, id: \.id) { message in
                            HStack {
                                MessageView(
                                    message: message,
                                    isHeaderShown: false,
                                    viewedBy: rootViewModel.user
                                )
                                ProgressView()
                                    .progressViewStyle(CircularProgressViewStyle())
                            }
                        }
                        HStack {}
                            .padding(.top, 4)
                            .id("bottom")
                    }
                    .padding([.top, .leading, .trailing])
                    .onChange(of: viewModel.messages) { _ in
                        if let scrollTo = viewModel.scrollTo {
                            DispatchQueue.main.async {
                                reader.scrollTo(scrollTo, anchor: scrollTo == "bottom" ? .bottom : .top)
                                viewModel.scrollTo = nil
                            }
                        }
                    }
                    .onChange(of: viewModel.sendingMessages) { _ in
                        if let scrollTo = viewModel.scrollTo {
                            reader.scrollTo(scrollTo, anchor: .bottom)
                            viewModel.scrollTo = nil
                        }
                    }
                    .onChange(of: viewModel.keyboardWillShow) { _ in
                        reader.scrollTo("bottom", anchor: .bottom)
                    }
                }
            }
            .padding(.bottom, 50)
            .onTapGesture {
                UIApplication.shared.sendAction(#selector(UIResponder.resignFirstResponder), to: nil, from: nil, for: nil)
            }
            VStack {
                Spacer()
                HStack(alignment: .top) {
                    TextField("Ecrivez un message...", text: $viewModel.typingMessage)
                        .onTapGesture {
                            DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                                viewModel.keyboardWillShow.toggle()
                            }
                        }
                    Button(
                        action: {
                            viewModel.sendMessage(
                                token: rootViewModel.token,
                                sentBy: rootViewModel.user
                            )
                        },
                        label: {
                            Image(systemName: "paperplane")
                        }
                    )
                    .disabled(viewModel.typingMessage.trim().isEmpty)
                }
                .padding()
            }
        }
        .navigationBarTitle(viewModel.conversation.name, displayMode: .inline)
        .toolbar {
            NavigationLink(destination: ConversationSettingsView(viewModel: ConversationSettingsViewModel(
                conversation: viewModel.conversation
            ))) {
                Image(systemName: "gearshape")
            }
        }
        .onAppear {
            viewModel.onAppear(token: rootViewModel.token)
            WebSocketService.shared.currentConversationId = viewModel.conversation.id
        }
        .onDisappear {
            if WebSocketService.shared.currentConversationId == viewModel.conversation.id {
                WebSocketService.shared.currentConversationId = nil
            }
        }
        .onChange(of: scenePhase) { newPhase in
            if newPhase == .active {
                viewModel.onAppear(token: rootViewModel.token)
            }
        }
    }
    
}
