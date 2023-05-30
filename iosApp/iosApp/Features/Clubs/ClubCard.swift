//
//  ClubCard.swift
//  BDE
//
//  Created by Nathan Fallet on 13/02/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import Kingfisher
import shared

struct ClubCard: View {
    
    let club: Club
    let badgeText: String?
    let badgeColor: Color?
    let action: ((Club) -> Void)?
    let detailsEnabled: Bool
    
    @State var showDetails = false
    
    var body: some View {
        NavigationLink(
            destination: ClubView(viewModel: ClubViewModel(club: club)),
            isActive: $showDetails
        ) {
            VStack(alignment: .leading, spacing: 8) {
                HStack {
                    if let logo = club.logo, let url = URL(string: "https://bdensisa.org/clubs/\(club.id)/uploads/\(logo)") {
                        KFImage(url)
                            .resizable()
                            .frame(maxWidth: 44, maxHeight: 44)
                            .cornerRadius(4)
                    }
                    VStack(alignment: .leading) {
                        Text(club.name)
                            .fontWeight(.bold)
                        Text("\(club.membersCount ?? 0) membre\(club.membersCount ?? 0 != 1 ? "s" : "")")
                    }
                    Spacer()
                    if let badgeText, let badgeColor {
                        if let action {
                            Button(badgeText) {
                                action(club)
                            }
                            .font(.caption)
                            .padding(.horizontal, 10)
                            .padding(.vertical, 6)
                            .foregroundColor(.white)
                            .background(badgeColor)
                            .cornerRadius(8)
                        } else {
                            Text(badgeText)
                                .font(.caption)
                                .padding(.horizontal, 10)
                                .padding(.vertical, 6)
                                .foregroundColor(.white)
                                .background(badgeColor)
                                .cornerRadius(8)
                        }
                    }
                }
                Text(club.description_ ?? "")
                    .lineLimit(detailsEnabled ? 5 : nil)
            }
            .foregroundColor(.primary)
            .multilineTextAlignment(.leading)
            .cardView()
            .onTapGesture {
                if detailsEnabled {
                    showDetails = true
                }
            }
        }
    }
    
}
