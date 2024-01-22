//
//  ShopItemView.swift
//  BDE
//
//  Created by Nathan Fallet on 27/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct ShopItemView: View {
    
    @EnvironmentObject var rootViewModel: OldRootViewModel
    @StateObject var viewModel: ShopItemViewModel
    
    var body: some View {
        ScrollView {
            VStack(alignment: .leading) {
                if let success = viewModel.success {
                    Text(success)
                        .foregroundColor(.white)
                        .frame(maxWidth: .infinity)
                        .cardView(background: Color.green)
                }
                if let configuration = viewModel.item as? TicketConfiguration, Int64(truncating: configuration.userLeft ?? 1) < 1 {
                    Text("Cet élément n'est plus disponible.")
                        .foregroundColor(.white)
                        .frame(maxWidth: .infinity)
                        .cardView(background: Color.accentColor)
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
                    if let configuration = viewModel.item as? TicketConfiguration, let userLeft = configuration.userLeft {
                        Text("\(userLeft) place(s) restante(s)")
                    }
                    if viewModel.item.canPayLater {
                        Picker(selection: $viewModel.payNow, label: Text("")) {
                            Text("Lydia").tag(true)
                            Text("A un membre BDE").tag(false)
                        }
                        .pickerStyle(.segmented)
                    }
                    Button("Acheter") {
                        viewModel.launchBuy(token: rootViewModel.token)
                    }
                    .disabled(viewModel.loading)
                    .buttonStyle(FilledButtonStyle())
                    Text("Astuce : ajoutez votre adresse mail uha à votre compte Lydia pour simplifier le processus de paiement.")
                }
                .cardView()
            }
            .padding()
        }
        .navigationTitle(Text(viewModel.item.title ?? "Boutique"))
        .onAppear(perform: viewModel.onAppear)
        .sheet(item: $viewModel.url) { url in
            SafariView(url: URL(string: url)!)
        }
        .alert(item: $viewModel.error) { error in
            Alert(
                title: Text("Une erreur est survenue !"),
                message: Text(error)
            )
        }
    }
    
}
