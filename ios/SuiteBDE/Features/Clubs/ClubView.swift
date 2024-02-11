//
//  ClubView.swift
//  BDE
//
//  Created by Nathan Fallet on 13/02/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct ClubView: View {
    
    @Environment(\.openURL) var openURL
    @EnvironmentObject var rootViewModel: OldRootViewModel
    @StateObject var viewModel: ClubViewModel
    
    var body: some View {
        ScrollView {
            VStack(alignment: .leading) {
                ClubCard(
                    ensisaClub: viewModel.club,
                    club: viewModel.club.suiteBde
                )
                HStack {
                    Text("Membres")
                        .font(.title)
                    Spacer()
                }
                .padding(.top)
                LazyVGrid(
                    columns: [GridItem(.adaptive(minimum: 300, maximum: 400))],
                    alignment: .leading
                ) {
                    ForEach(viewModel.members, id: \.userId) { membership in
                        HStack {
                            VStack(alignment: .leading) {
                                Text("\(membership.user?.firstName ?? "") \(membership.user?.lastName ?? "")")
                                Text(membership.user?.description_ ?? "")
                                    .foregroundColor(.secondary)
                            }
                            Spacer()
                            Text(membership.role == "admin" ? "ADMIN" : "MEMBRE")
                                .font(.caption)
                                .padding(.horizontal, 10)
                                .padding(.vertical, 6)
                                .foregroundColor(.white)
                                .background(membership.role == "admin" ? Color.black : Color.green)
                                .cornerRadius(8)
                        }
                        .cardView()
                    }
                }
            }
            .padding()
        }
        .onAppear(perform: viewModel.onAppear)
        .navigationTitle(Text(viewModel.club.name))
        .toolbar {
            if viewModel.members.contains(where: { $0.userId == rootViewModel.user?.id }) {
                Button(
                    action: { viewModel.leave(token: rootViewModel.token) },
                    label: { Image(systemName: "rectangle.portrait.and.arrow.right") }
                )
            }
            if let email = viewModel.club.email, let url = URL(string: "mailto:\(email)") {
                Button(
                    action: { openURL(url) },
                    label: { Image(systemName: "envelope") }
                )
            }
        }
    }
    
}
