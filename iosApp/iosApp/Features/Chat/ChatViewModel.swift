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
    
    var onConversationChatMessage: ((ChatMessage) -> Void)? = nil
    
    init(parentViewModel: RootViewModel) {
        parentViewModel.onWebSocketMessage = onWebSocketMessage
    }
    
    func onAppear(token: String?) {
        AnalyticsService.shared.log(.screenView(screenName: "chat", screenClass: "ChatView"))
        
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
        self.conversations = conversations.map { conversation in
            chatMessage.groupType == conversation.groupType && chatMessage.groupId == conversation.groupId ?
            ChatConversation(
                groupType: conversation.groupType,
                groupId: conversation.groupId,
                name: conversation.name,
                logo: conversation.logo,
                backupLogo: conversation.backupLogo,
                membership: conversation.membership,
                lastMessage: chatMessage
            ) : conversation
        }.sorted { $0.lastMessage?.createdAt?.epochSeconds ?? 0 > $1.lastMessage?.createdAt?.epochSeconds ?? 0 }
        onConversationChatMessage?(chatMessage)
    }
    
    func onChatMembership(chatMembership: ChatMembership) {
        self.conversations = conversations.map { conversation in
            chatMembership.groupType == conversation.groupType && chatMembership.groupId == conversation.groupId ?
            ChatConversation(
                groupType: conversation.groupType,
                groupId: conversation.groupId,
                name: conversation.name,
                logo: conversation.logo,
                backupLogo: conversation.backupLogo,
                membership: chatMembership,
                lastMessage: conversation.lastMessage
            ) : conversation
        }.sorted { $0.lastMessage?.createdAt?.epochSeconds ?? 0 > $1.lastMessage?.createdAt?.epochSeconds ?? 0 }
    }
    
}
