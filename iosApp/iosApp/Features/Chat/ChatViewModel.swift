//
//  ChatViewModel.swift
//  BDE
//
//  Created by Nathan Fallet on 07/07/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

class ChatViewModel: ObservableObject {
    
    @Published var conversations = [ChatConversation]()
    
    var onConversationChatMessage: ((ChatMessage) -> Void)?
    
    func onAppear(token: String?) {
        AnalyticsService.shared.log(.screenView(screenName: "chat", screenClass: "ChatView"))
        
        WebSocketService.shared.onWebSocketMessage = onWebSocketMessage
        fetchConversations(token: token)
    }
    
    func fetchConversations(token: String?) {
        guard let token else {
            return
        }
        Task {
            let conversations = try await CacheService.shared.apiService().getChat(token: token)
            DispatchQueue.main.async {
                self.conversations = conversations.sorted {
                    $0.lastMessage?.createdAt?.epochSeconds ?? 0 > $1.lastMessage?.createdAt?.epochSeconds ?? 0
                }
            }
        }
    }
    
    func onWebSocketMessage(message: Any) {
        DispatchQueue.main.async {
            switch (message) {
            case let chatMessage as ChatMessage:
                self.onChatMessage(chatMessage: chatMessage)
            case let chatMembership as ChatMembership:
                self.onChatMembership(chatMembership: chatMembership)
            default:
                break
            }
        }
    }
    
    func onChatMessage(chatMessage: ChatMessage) {
        conversations
            .filter { chatMessage.groupType == $0.groupType && chatMessage.groupId == $0.groupId }
            .forEach { $0.lastMessage = chatMessage }
        conversations = conversations.sorted {
            $0.lastMessage?.createdAt?.epochSeconds ?? 0 > $1.lastMessage?.createdAt?.epochSeconds ?? 0
        }
        onConversationChatMessage?(chatMessage)
    }
    
    func onChatMembership(chatMembership: ChatMembership) {
        conversations
            .filter { chatMembership.groupType == $0.groupType && chatMembership.groupId == $0.groupId }
            .forEach { $0.membership = chatMembership }
        conversations = conversations.sorted {
            $0.lastMessage?.createdAt?.epochSeconds ?? 0 > $1.lastMessage?.createdAt?.epochSeconds ?? 0
        }
    }
    
}
