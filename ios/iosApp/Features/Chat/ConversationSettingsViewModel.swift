//
//  ConversationSettingsViewModel.swift
//  BDE
//
//  Created by Nathan Fallet on 07/07/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

class ConversationSettingsViewModel: ObservableObject {
    
    let conversation: ChatConversation
    
    @Published var notifications: Bool
    @Published var members = [User]()
    
    init(conversation: ChatConversation) {
        self.conversation = conversation
        self.notifications = conversation.membership?.notifications ?? true
    }
    
    func onAppear(token: String?) {
        AnalyticsService.shared.log(.screenView(screenName: "conversation_settings", screenClass: "ConversationSettingsView"))
        
        fetchMembers(token: token)
    }
    
    func fetchMembers(token: String?) {
        guard let token else {
            return
        }
        Task {
            let members = try await CacheService.shared.apiService().getChatMembers(
                token: token,
                type: conversation.groupType,
                id: conversation.groupId
            )
            DispatchQueue.main.async {
                self.members = members
            }
        }
    }
    
    func updateMembership(token: String?) {
        guard let token else {
            return
        }
        Task {
            try await CacheService.shared.apiService().putChatMembers(
                token: token,
                type: conversation.groupType,
                id: conversation.groupId,
                upload: ChatMembershipUpload(
                    notifications: KotlinBoolean(bool: notifications)
                )
            )
        }
    }
    
}
