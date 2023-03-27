//
//  ShopItemView.swift
//  BDE
//
//  Created by Nathan Fallet on 27/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct ShopItemView: View {
    
    @EnvironmentObject var rootViewModel: RootViewModel
    @StateObject var viewModel: ShopItemViewModel
    
    var body: some View {
        ScrollView {
            VStack(alignment: .leading) {
                ShopCard(
                    item: viewModel.item,
                    detailsEnabled: false,
                    cotisant: rootViewModel.user?.cotisant != nil
                )
                VStack(alignment: .leading, spacing: 8) {
                    HStack {
                        Text("Acheter")
                            .fontWeight(.bold)
                        Spacer()
                    }
                    if rootViewModel.user?.cotisant != nil {
                        HStack {
                            Text("\(viewModel.item.price ?? 0)€")
                                .strikethrough()
                            Text("\(viewModel.item.priceReduced ?? 0)€")
                                .fontWeight(.bold)
                        }
                    } else {
                        Text("\(viewModel.item.price ?? 0)€")
                    }
                    Picker(selection: $viewModel.payNow, label: Text("")) {
                        Text("Lydia").tag(true)
                        Text("A un membre BDE").tag(false)
                    }
                    .pickerStyle(.segmented)
                    Button("Acheter", action: viewModel.launchBuy)
                    .buttonStyle(FilledButtonStyle())
                }
                .cardView()
            }
            .padding()
        }
        .navigationTitle(Text(viewModel.item.title ?? "Boutique"))
        .onAppear(perform: viewModel.onAppear)
    }
    
}
