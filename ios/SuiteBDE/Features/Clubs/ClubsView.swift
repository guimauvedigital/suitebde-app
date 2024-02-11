//
//  ClubsView.swift
//  BDE
//
//  Created by Nathan Fallet on 12/02/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct ClubsView: View {
    
    @EnvironmentObject var rootViewModel: OldRootViewModel
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
                                DefaultNavigationLink(
                                    destination: ClubView(viewModel: ClubViewModel(club: membership.club!))
                                ) {
                                    ClubCard(
                                        ensisaClub: membership.club!,
                                        club: membership.club!.suiteBde
                                    )
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
                            DefaultNavigationLink(
                                destination: ClubView(viewModel: ClubViewModel(club: club))
                            ) {
                                ClubCard(
                                    ensisaClub: club,
                                    club: club.suiteBde
                                )
                                .onAppear {
                                    viewModel.loadMore(id: club.id)
                                }
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
        .navigationViewStyle(StackNavigationViewStyle())
    }
    
}

struct ClubsView_Previews: PreviewProvider {
    
    static var previews: some View {
        ClubsView()
    }
    
}
