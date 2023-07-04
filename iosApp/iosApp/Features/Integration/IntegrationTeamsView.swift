//
//  IntegrationTeamsView.swift
//  BDE
//
//  Created by Nathan Fallet on 04/07/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct IntegrationTeamsView: View {
    
    @EnvironmentObject var rootViewModel: RootViewModel
    @StateObject var viewModel = IntegrationTeamsViewModel()
    
    var body: some View {
        ScrollView {
            VStack(alignment: .leading) {
                HStack {
                    Text("Equipes")
                        .font(.title)
                    Spacer()
                }
                LazyVGrid(
                    columns: [GridItem(.adaptive(minimum: 300, maximum: 400))],
                    alignment: .leading
                ) {
                    ForEach(Array(viewModel.teams.enumerated()), id: \.offset) { offset, team in
                        NavigationLink(destination: IntegrationTeamView(viewModel: IntegrationTeamViewModel(
                            team: team
                        ))) {
                            HStack(spacing: 12) {
                                if rootViewModel.integrationConfiguration?.showRank ?? false {
                                    Text((offset + 1).positionString)
                                        .font(.title)
                                }
                                VStack(alignment: .leading) {
                                    Text(team.name)
                                        .fontWeight(.bold)
                                        .lineLimit(1)
                                    if let score = team.score, rootViewModel.integrationConfiguration?.showScore ?? false {
                                        Text("\(score) pts")
                                    }
                                }
                                Spacer()
                            }
                            .cardView()
                            .foregroundColor(.primary)
                        }
                    }
                }
                HStack {
                    Text("Défis")
                        .font(.title)
                    Spacer()
                }
                .padding(.top)
                LazyVGrid(
                    columns: [GridItem(.adaptive(minimum: 300, maximum: 400))],
                    alignment: .leading
                ) {
                    ForEach(viewModel.challenges, id: \.id) { challenge in
                        VStack(alignment: .leading) {
                            HStack(spacing: 12) {
                                Text(challenge.name)
                                    .fontWeight(.bold)
                                    .lineLimit(1)
                                Spacer()
                                Text("\(challenge.reward) pts")
                                    .lineLimit(1)
                            }
                            Text(challenge.description_)
                        }
                        .cardView()
                    }
                }
            }
            .padding()
        }
        .toolbar {
            if rootViewModel.integrationConfiguration?.canCreateTeams ?? false {
                NavigationLink(destination: IntegrationCreateView()) {
                    Image(systemName: "plus")
                }
            }
        }
        .onAppear {
            viewModel.onAppear(token: rootViewModel.token)
        }
        .refreshable {
            viewModel.onAppear(token: rootViewModel.token)
        }
        .navigationTitle(Text("Chasse"))
    }
    
}

struct IntegrationTeamsView_Previews: PreviewProvider {
    
    static var previews: some View {
        IntegrationTeamsView()
    }
    
}
