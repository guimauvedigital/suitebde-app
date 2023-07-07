//
//  ConversationViewModel.swift
//  BDE
//
//  Created by Nathan Fallet on 07/07/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

class ConversationViewModel: ObservableObject {
    
    let conversation: ChatConversation
    
    init(conversation: ChatConversation, parentViewModel: ChatViewModel) {
        self.conversation = conversation
        parentViewModel.onConversationChatMessage = onConversationChatMessage
    }
    
    @Published var messages = [ChatMessage]()
    @Published var hasMore = true
    @Published var sendingMessages = [ChatMessage]()
    @Published var typingMessage = ""
    @Published var keyboardWillShow = false
    @Published var scrollToBottom = true
    
    func onAppear(token: String?) {
        AnalyticsService.shared.log(.screenView(screenName: "conversation", screenClass: "ConversationView"))
        
        fetchMessages(token: token, reset: true)
    }
    
    func fetchMessages(token: String?, reset: Bool) {
        guard let token else {
            return
        }
        Task {
            let messages = try await CacheService.shared.apiService().getChatMessages(
                token: token,
                type: conversation.groupType,
                id: conversation.groupId,
                offset: reset ? 0 : Int64(messages.count)
            )
            DispatchQueue.main.async {
                if reset {
                    self.messages = []
                }
                self.messages.append(contentsOf: messages)
                self.hasMore = !messages.isEmpty
            }
        }
    }
    
    func loadMore(token: String?, id: String?) {
        guard hasMore else {
            return
        }
        if id == messages.last?.id {
            fetchMessages(token: token, reset: false)
        }
    }
    
    func sendMessage(token: String?, sentBy: User?) {
        guard let token, let sentBy else {
            return
        }
        let futureMessage = ChatMessage(
            id: UUID().uuidString,
            userId: sentBy.id,
            groupType: conversation.groupType,
            groupId: conversation.groupId,
            type: "text",
            content: typingMessage,
            createdAt: Date().asKotlinxInstant,
            user: sentBy
        )
        Task {
            try await CacheService.shared.apiService().postChatMessages(
                token: token,
                type: conversation.groupType,
                id: conversation.groupId,
                message: ChatMessageUpload(
                    type: futureMessage.type,
                    content: futureMessage.content ?? ""
                )
            )
            DispatchQueue.main.async {
                self.sendingMessages.removeAll { $0.id == futureMessage.id }
            }
        }
        
        sendingMessages.append(futureMessage)
        scrollToBottom = true
        typingMessage = ""
    }
    
    func markAsRead(token: String?) {
        guard let token else {
            return
        }
        Task {
            try await CacheService.shared.apiService().putChatMembers(
                token: token,
                type: conversation.groupType,
                id: conversation.groupId,
                upload: nil
            )
        }
    }
    
    func onConversationChatMessage(chatMessage: ChatMessage) {
        guard chatMessage.groupType == conversation.groupType,
              chatMessage.groupId == conversation.groupId
        else { return }
        self.scrollToBottom = true
        self.messages.insert(chatMessage, at: 0)
    }
    
}
