//
//  ShopView.swift
//  BDE
//
//  Created by Nathan Fallet on 22/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct ShopView: View {
    
    @EnvironmentObject var rootViewModel: RootViewModel
    @StateObject var viewModel = ShopViewModel()
    
    var body: some View {
        NavigationView {
            ScrollView {
                VStack(alignment: .leading) {
                    if rootViewModel.user?.cotisant == nil {
                        HStack {
                            Text("Cotisation")
                                .font(.title)
                            Spacer()
                        }
                        Text("La cotisation en ligne n'est pas encore disponible")
                            .padding(.vertical, 8)
                    }
                    HStack {
                        Text("Tickets")
                            .font(.title)
                        Spacer()
                    }
                    LazyVGrid(
                        columns: [GridItem(.adaptive(minimum: 300, maximum: 400))],
                        alignment: .leading
                    ) {
                        ForEach(viewModel.ticketConfigurations, id: \.id) { configuration in
                            ShopCard(
                                item: configuration,
                                type: "tickets",
                                detailsEnabled: true,
                                cotisant: rootViewModel.user?.cotisant != nil
                            )
                        }
                    }
                }
                .padding()
            }
            .navigationTitle(Text("Boutique"))
            .onAppear(perform: viewModel.onAppear)
            .refreshable {
                viewModel.fetchData()
            }
        }
        .navigationViewStyle(StackNavigationViewStyle())
    }
    
}

struct ShopView_Previews: PreviewProvider {
    
    static var previews: some View {
        ShopView()
    }
    
}
