//
//  IntegrationTeamView.swift
//  BDE
//
//  Created by Nathan Fallet on 04/07/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct IntegrationTeamView: View {
    
    @EnvironmentObject var rootViewModel: RootViewModel
    @StateObject var viewModel: IntegrationTeamViewModel
    
    var body: some View {
        ScrollView {
            VStack(alignment: .leading) {
                VStack(alignment: .leading, spacing: 8) {
                    HStack {
                        VStack(alignment: .leading) {
                            Text(viewModel.team.name)
                                .fontWeight(.bold)
                            Text("\(viewModel.team.membersCount ?? 0) membre\(viewModel.team.membersCount ?? 0 != 1 ? "s" : "")")
                        }
                        Spacer()
                        if let member = viewModel.member {
                            if member, let score = viewModel.team.score {
                                Text("\(score) pts")
                            } else {
                                Button("REJOINDRE") {
                                    viewModel.join(token: rootViewModel.token, viewedBy: rootViewModel.user)
                                }
                                .font(.caption)
                                .padding(.horizontal, 10)
                                .padding(.vertical, 6)
                                .foregroundColor(.white)
                                .background(Color.accentColor)
                                .cornerRadius(8)
                            }
                        }
                    }
                    Text(viewModel.team.description_ ?? "")
                }
                .foregroundColor(.primary)
                .multilineTextAlignment(.leading)
                .cardView()
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
                                    .fontWeight(.bold)
                                Text(membership.user?.description_ ?? "")
                            }
                            Spacer()
                            Text(membership.role == "parrain" ? "PARRAIN" : "MEMBRE")
                                .font(.caption)
                                .padding(.horizontal, 10)
                                .padding(.vertical, 6)
                                .foregroundColor(.white)
                                .background(membership.role == "parrain" ? Color.black : Color.green)
                                .cornerRadius(8)
                        }
                        .cardView()
                    }
                }
                HStack {
                    Text("Défis réalisés")
                        .font(.title)
                    Spacer()
                }
                .padding(.top)
                LazyVGrid(
                    columns: [GridItem(.adaptive(minimum: 300, maximum: 400))],
                    alignment: .leading
                ) {
                    ForEach(viewModel.executions, id: \.id) { execution in
                        HStack {
                            VStack(alignment: .leading) {
                                Text(execution.challenge?.name ?? "")
                                    .fontWeight(.bold)
                                Text("\(execution.user?.firstName ?? "") \(execution.user?.lastName ?? "")")
                            }
                            Spacer()
                            VStack(spacing: 0) {
                                Text(execution.validated ? "VALIDÉ" : "EN ATTENTE")
                                    .font(.caption)
                                    .padding(.horizontal, 10)
                                    .padding(.vertical, 6)
                                    .foregroundColor(.white)
                                    .background(execution.validated ? Color.green : Color.orange)
                                    .cornerRadius(8)
                                Text("\(execution.challenge?.reward ?? 0) pts")
                            }
                        }
                        .cardView()
                    }
                }
            }
            .padding()
        }
        .onAppear {
            viewModel.onAppear(token: rootViewModel.token, viewedBy: rootViewModel.user)
        }
        .navigationTitle(Text(viewModel.team.name))
        .toolbar {
            if viewModel.member ?? false {
                NavigationLink(destination: IntegrationExecutionView(viewModel: IntegrationExecutionViewModel(
                    team: viewModel.team
                ))) {
                    Image(systemName: "plus")
                }
                Button(
                    action: { viewModel.leave(token: rootViewModel.token, viewedBy: rootViewModel.user) },
                    label: { Image(systemName: "rectangle.portrait.and.arrow.right") }
                )
            }
        }
    }
    
}
