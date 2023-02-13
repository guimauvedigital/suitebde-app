//
//  ClubsView.swift
//  BDE
//
//  Created by Nathan Fallet on 12/02/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct ClubsView: View {
    
    @EnvironmentObject var rootViewModel: RootViewModel
    @StateObject var viewModel = ClubsViewModel()
    
    var body: some View {
        NavigationView {
            ScrollView {
                VStack(alignment: .leading) {
                    if !viewModel.mine.isEmpty {
                        HStack {
                            Text("Mes clubs")
                                .font(.title)
                            Spacer()
                        }
                        LazyVGrid(
                            columns: [GridItem(.adaptive(minimum: 300, maximum: 400))],
                            alignment: .leading
                        ) {
                            ForEach(viewModel.mine, id: \.clubId) { membership in
                                VStack(alignment: .leading, spacing: 8) {
                                    HStack {
                                        VStack(alignment: .leading) {
                                            Text(membership.club?.name ?? "")
                                                .fontWeight(.bold)
                                            Text("\(membership.club?.membersCount ?? 0) membre\(membership.club?.membersCount ?? 0 != 1 ? "s" : "")")
                                        }
                                        Spacer()
                                        if rootViewModel.user != nil {
                                            Text(membership.club?.validated != true ? "EN ATTENTE" : membership.role == "admin" ? "ADMIN" : "MEMBRE")
                                                .font(.caption)
                                                .padding(.horizontal, 10)
                                                .padding(.vertical, 6)
                                                .foregroundColor(.white)
                                                .background(membership.club?.validated != true ? Color.orange : membership.role == "admin" ? Color.black : Color.green)
                                                .cornerRadius(8)
                                        }
                                    }
                                    Text(membership.club?.description_ ?? "")
                                }
                                .cardView()
                                .onAppear {
                                    viewModel.loadMore(token: rootViewModel.token, id: membership.club?.id)
                                }
                            }
                        }
                        HStack {
                            Text("Autres clubs")
                                .font(.title)
                            Spacer()
                        }
                        .padding(.top)
                    }
                    LazyVGrid(
                        columns: [GridItem(.adaptive(minimum: 300, maximum: 400))],
                        alignment: .leading
                    ) {
                        ForEach(viewModel.clubs.filter({ club in
                            !viewModel.mine.contains(where: { $0.clubId == club.id })
                        }), id: \.id) { club in
                            VStack(alignment: .leading, spacing: 8) {
                                HStack {
                                    VStack(alignment: .leading) {
                                        Text(club.name)
                                            .fontWeight(.bold)
                                        Text("\(club.membersCount ?? 0) membre\(club.membersCount ?? 0 != 1 ? "s" : "")")
                                    }
                                    Spacer()
                                    if rootViewModel.user != nil {
                                        Button("REJOINDRE") {
                                            viewModel.joinClub(id: club.id, token: rootViewModel.token)
                                        }
                                        .font(.caption)
                                        .padding(.horizontal, 10)
                                        .padding(.vertical, 6)
                                        .foregroundColor(.white)
                                        .background(Color.accentColor)
                                        .cornerRadius(8)
                                    }
                                }
                                Text(club.description_ ?? "")
                            }
                            .cardView()
                            .onAppear {
                                viewModel.loadMore(token: rootViewModel.token, id: club.id)
                            }
                        }
                    }
                }
                .padding()
            }
            .onAppear {
                viewModel.onAppear(token: rootViewModel.token)
            }
            .refreshable {
                viewModel.onAppear(token: rootViewModel.token)
            }
            .navigationTitle(Text("Clubs"))
        }
    }
    
}

struct ClubsView_Previews: PreviewProvider {
    
    static var previews: some View {
        ClubsView()
    }
    
}
