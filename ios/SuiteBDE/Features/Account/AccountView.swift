//
//  AccountView.swift
//  BDE
//
//  Created by Nathan Fallet on 31/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct AccountView: View {
    
    @EnvironmentObject var rootViewModel: OldRootViewModel
    @StateObject var viewModel: AccountViewModel
    
    var body: some View {
        NavigationView {
            ScrollView {
                VStack(alignment: .leading) {
                    HStack {
                        Spacer()
                    }
                    if let user = rootViewModel.user {
                        if user.nfcIdentifier == nil {
                            Text("Carte étudiante non enregistrée, cliquez pour l'ajouter")
                                .foregroundColor(.white)
                                .frame(maxWidth: .infinity)
                                .cardView(background: Color.accentColor)
                                .onTapGesture {
                                    viewModel.launchNFC(token: rootViewModel.token)
                                }
                        }
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
                                        .foregroundColor(.secondary)
                                }
                                VStack(alignment: .leading, spacing: 4) {
                                    Text(user.cotisant != nil ? "Cotisant" : "Non cotisant")
                                        .foregroundColor(user.cotisant != nil ? Color.green : Color.red)
                                    if let cotisant = user.cotisant {
                                        Text("Expire : \(cotisant.expiration.renderedDate)")
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
                        if !viewModel.tickets.isEmpty {
                            HStack {
                                Text("Tickets")
                                    .font(.title)
                                Spacer()
                            }
                            .padding(.top)
                            LazyVGrid(
                                columns: [GridItem(.adaptive(minimum: 300, maximum: 400))],
                                alignment: .leading
                            ) {
                                ForEach(viewModel.tickets, id: \.id) { ticket in
                                    VStack(alignment: .leading, spacing: 8) {
                                        HStack {
                                            Text(ticket.event?.title ?? "")
                                            Spacer()
                                            if ticket.paid == nil {
                                                Button("NON PAYÉ") {
                                                    viewModel.launchPayment(
                                                        token: rootViewModel.token,
                                                        shopItemType: "tickets",
                                                        shopItemId: ticket.configurationId,
                                                        itemId: ticket.id
                                                    )
                                                }
                                                .font(.caption)
                                                .padding(.horizontal, 10)
                                                .padding(.vertical, 6)
                                                .foregroundColor(.white)
                                                .background(Color.accentColor)
                                                .cornerRadius(8)
                                            } else {
                                                Text("PAYÉ")
                                                    .font(.caption)
                                                    .padding(.horizontal, 10)
                                                    .padding(.vertical, 6)
                                                    .foregroundColor(.white)
                                                    .background(Color.green)
                                                    .cornerRadius(8)
                                            }
                                        }
                                        Text(ticket.event?.content ?? "")
                                            .lineLimit(5)
                                    }
                                    .multilineTextAlignment(.leading)
                                    .cardView()
                                }
                            }
                        }
                    } else {
                        Button("Connexion", action: viewModel.launchLogin)
                            .buttonStyle(DefaultButtonStyle())
                    }
                }
                .padding()
            }
            .navigationTitle(Text("Mon compte"))
            .toolbar {
                if let user = rootViewModel.user {
                    if user.hasPermission(permission: "admin.users.view") {
                        NavigationLink(
                            destination: {
                                ScannerView(viewModel: ScannerViewModel(
                                    onURLFound: rootViewModel.onOpenURL
                                ))
                            },
                            label: {
                                Image(systemName: "qrcode.viewfinder")
                            }
                        )
                        NavigationLink(
                            destination: {
                                UsersView()
                            },
                            label: {
                                Image(systemName: "person.2")
                            }
                        )
                    }
                    NavigationLink(destination: UserView(viewModel: UserViewModel(
                        user: user,
                        editable: false,
                        isMyAccount: true
                    ))) {
                        Image(systemName: "pencil")
                    }
                }
            }
            .sheet(item: $viewModel.url) { url in
                SafariView(url: URL(string: url)!)
            }
            .alert(item: $viewModel.error) { error in
                Alert(
                    title: Text("Une erreur est survenue !"),
                    message: Text(error)
                )
            }
            .onAppear {
                viewModel.onAppear(token: rootViewModel.token, id: rootViewModel.user?.id)
            }
            .onOpenURL(perform: viewModel.onOpenUrl)
        }
        .navigationViewStyle(StackNavigationViewStyle())
    }
    
}

struct AccountView_Previews: PreviewProvider {
    
    static var previews: some View {
        AccountView(viewModel: AccountViewModel(
            saveToken: { _ in }
        ))
        .environmentObject(OldRootViewModel())
    }
    
}
