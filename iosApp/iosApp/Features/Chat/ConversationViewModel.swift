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
    
    init(conversation: ChatConversation) {
        self.conversation = conversation
    }
    
    @Published var messages = [ChatMessage]()
    @Published var hasMore = true
    @Published var sendingMessages = [ChatMessage]()
    @Published var typingMessage = ""
    @Published var keyboardWillShow = false
    @Published var scrollTo: String?
    
    func onAppear(token: String?) {
        AnalyticsService.shared.log(.screenView(screenName: "conversation", screenClass: "ConversationView"))
        
        WebSocketService.shared.onWebSocketMessageConversation = onWebSocketMessage
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
                    self.scrollTo = "bottom"
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
            self.scrollTo = id
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
        scrollTo = "bottom"
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
    
    func onWebSocketMessage(message: Any) {
        DispatchQueue.main.async {
            switch (message) {
            case let chatMessage as ChatMessage:
                self.onChatMessage(chatMessage: chatMessage)
            default:
                break
            }
        }
    }
    
    func onChatMessage(chatMessage: ChatMessage) {
        guard chatMessage.groupType == conversation.groupType,
              chatMessage.groupId == conversation.groupId
        else { return }
        self.scrollTo = "bottom"
        self.messages.insert(chatMessage, at: 0)
    }
    
}
