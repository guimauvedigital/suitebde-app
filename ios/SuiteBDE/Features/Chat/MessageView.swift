//
//  MessageView.swift
//  BDE
//
//  Created by Nathan Fallet on 07/07/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import Kingfisher
import shared

struct MessageView: View {
    
    let message: ChatMessage
    let isHeaderShown: Bool
    let viewedBy: User?
    
    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            if message.type == "system" {
                Text(message.content ?? "")
                    .multilineTextAlignment(.center)
                    .foregroundColor(.secondary)
                    .padding()
            } else {
                if isHeaderShown && message.userId != viewedBy?.id {
                    HStack(alignment: .bottom) {
                        if let url = URL(string: "https://bdensisa.org/api/users/\(message.userId)/picture") {
                            KFImage(url)
                                .placeholder {
                                    "\(message.user?.firstName ?? "") \(message.user?.lastName ?? "")".chatLogo(size: 20)
                                }
                                .resizable()
                                .aspectRatio(contentMode: .fill)
                                .frame(width: 20, height: 20)
                                .cornerRadius(10)
                        }
                        Text("\(message.user?.firstName ?? "") \(message.user?.lastName ?? "")")
                    }
                    .padding(.vertical, 8)
                }
                HStack {
                    if message.userId == viewedBy?.id {
                        Spacer(minLength: UIScreen.main.bounds.width * 0.2)
                    }
                    Text(message.content ?? "")
                        .padding(10)
                        .foregroundColor(message.userId == viewedBy?.id ? .white : .primary)
                        .background(message.userId == viewedBy?.id ? .accentColor : Color(.systemGray6))
                        .cornerRadius(10)
                    if message.userId != viewedBy?.id {
                        Spacer(minLength: UIScreen.main.bounds.width * 0.2)
                    }
                }
            }
        }
    }
    
}
