//
//  UserView.swift
//  BDE
//
//  Created by Nathan Fallet on 03/02/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct UserView: View {
    
    @EnvironmentObject var rootViewModel: RootViewModel
    @StateObject var viewModel: UserViewModel
    
    var body: some View {
        Form {
            Section(header: Text("Informations")) {
                if viewModel.editing {
                    TextField("Prénom", text: $viewModel.firstName)
                    TextField("Nom", text: $viewModel.lastName)
                    Picker("Année", selection: $viewModel.year) {
                        Text("1A").tag("1A")
                        Text("2A").tag("2A")
                        Text("3A").tag("3A")
                        Text("4A et plus").tag("other")
                    }
                    Picker("Option", selection: $viewModel.option) {
                        Text("Informatique et Réseaux").tag("ir")
                        Text("Automatique et Systèmes embarqués").tag("ase")
                        Text("Mécanique").tag("meca")
                        Text("Textile et Fibres").tag("tf")
                        Text("Génie Industriel").tag("gi")
                    }
                    Button("Enregistrer") {
                        viewModel.updateInfo(token: rootViewModel.token)
                    }
                } else {
                    Text("\(viewModel.firstName) \(viewModel.lastName)")
                    Text(viewModel.user.description_)
                }
            }
            Section(header: Text("Cotisation")) {
                Text(viewModel.user.cotisant != nil ? "Cotisant" : "Non cotisant")
                    .foregroundColor(viewModel.user.cotisant != nil ? Color.green : Color.red)
                if let cotisant = viewModel.user.cotisant, !viewModel.editing {
                    Text("Expire : \(cotisant.expiration.renderedDate)")
                }
                if viewModel.editing {
                    DatePicker(
                        "Expire",
                        selection: $viewModel.expiration,
                        displayedComponents: .date
                    )
                    Button("1 an") {
                        viewModel.expiration = .oneYear
                    }
                    Button("Scolarité") {
                        viewModel.expiration = .fiveYears
                    }
                    Button("Enregistrer") {
                        viewModel.updateExpiration(token: rootViewModel.token)
                    }
                }
            }
        }
        .navigationTitle(Text("Utilisateur"))
        .toolbar {
            Button(viewModel.editing ? "Terminé" : "Modifier", action: viewModel.toggleEdit)
                .disabled(!viewModel.editable)
        }
        .onAppear(perform: viewModel.onAppear)
    }
    
}
