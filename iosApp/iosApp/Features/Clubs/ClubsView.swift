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
                            ForEach(viewModel.mine, id: \.clubId) { club in
                                
                            }
                        }
                        HStack {
                            Text("Autres clubs")
                                .font(.title)
                            Spacer()
                        }
                    }
                    
                }
            }
            .navigationTitle(Text("Clubs"))
            .onAppear {
                viewModel.onAppear(token: rootViewModel.token)
            }
        }
    }
    
}

struct ClubsView_Previews: PreviewProvider {
    
    static var previews: some View {
        ClubsView()
    }
    
}
