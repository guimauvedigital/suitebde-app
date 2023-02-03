//
//  AccountView.swift
//  BDE
//
//  Created by Nathan Fallet on 31/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct AccountView: View {
    
    @EnvironmentObject var rootViewModel: RootViewModel
    @StateObject var viewModel: AccountViewModel
    
    var body: some View {
        VStack {
            if let user = rootViewModel.user {
                if let qrcode = viewModel.qrcode {
                    VStack(alignment: .leading, spacing: 32) {
                        Image(uiImage: qrcode)
                            .renderingMode(.template)
                            .resizable()
                            .interpolation(.none)
                            .scaledToFit()
                            .frame(maxWidth: 400)
                        VStack(alignment: .leading, spacing: 4) {
                            Text("\(user.firstName ?? "Prénom") \(user.lastName ?? "Nom")")
                            Text(user.description_)
                        }
                        VStack(alignment: .leading, spacing: 4) {
                            Text(user.cotisant != nil ? "Cotisant" : "Non cotisant")
                                .foregroundColor(user.cotisant != nil ? Color.green : Color.red)
                            if let cotisant = user.cotisant {
                                Text("Expire : \(cotisant.expiration.rendered)")
                            }
                        }
                    }
                    .padding()
                    .cardView()
                } else {
                    Text("Chargement...")
                        .onAppear {
                            viewModel.generateQRCode(user: user)
                        }
                }
            } else {
                Button("Connexion", action: viewModel.launchLogin)
                    .buttonStyle(FilledButtonStyle())
            }
        }
        .padding()
        .sheet(item: $viewModel.url) { url in
            SafariView(url: URL(string: url)!)
        }
        .alert(item: $viewModel.error) { error in
            Alert(
                title: Text("Une erreur est survenue !"),
                message: Text(error)
            )
        }
        .onOpenURL(perform: viewModel.onOpenUrl)
    }
    
}

struct AccountView_Previews: PreviewProvider {
    
    static var previews: some View {
        AccountView(viewModel: AccountViewModel(
            saveToken: { _ in }
        ))
        .environmentObject(RootViewModel())
    }
    
}
