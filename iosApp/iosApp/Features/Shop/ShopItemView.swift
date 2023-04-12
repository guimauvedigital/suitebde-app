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
                if let success = viewModel.success {
                    Text(success)
                        .foregroundColor(.white)
                        .cardView(background: Color.green)
                }
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
                    ShopItemPrice(item: viewModel.item, cotisant: rootViewModel.user?.cotisant != nil)
                    Picker(selection: $viewModel.payNow, label: Text("")) {
                        Text("Lydia").tag(true)
                        Text("A un membre BDE").tag(false)
                    }
                    .pickerStyle(.segmented)
                    Button("Acheter") {
                        viewModel.launchBuy(token: rootViewModel.token)
                    }
                    .disabled(viewModel.loading)
                    .buttonStyle(FilledButtonStyle())
                }
                .cardView()
            }
            .padding()
        }
        .navigationTitle(Text(viewModel.item.title ?? "Boutique"))
        .onAppear(perform: viewModel.onAppear)
        .alert(isPresented: $viewModel.error) {
            Alert(
                title: Text("Une erreur est survenue !"),
                message: Text("Vérifiez que vous êtes bien connecté à internet, et que vous n'avez pas déjà acheté cet élément.")
            )
        }
    }
    
}
