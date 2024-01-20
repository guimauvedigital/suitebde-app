//
//  IntegrationCreateView.swift
//  BDE
//
//  Created by Nathan Fallet on 04/07/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct IntegrationCreateView: View {
    
    @Environment(\.presentationMode) var presentationMode
    
    @EnvironmentObject var rootViewModel: RootViewModel
    @StateObject var viewModel = IntegrationCreateViewModel()
    
    var body: some View {
        Form {
            Section(header: Text("Informations")) {
                TextField("Nom de l'équipe", text: $viewModel.name)
            }
            Section(header: Text("Description de l'équipe")) {
                TextEditor(text: $viewModel.description)
            }
            Section {
                Button("Créer") {
                    viewModel.createTeam(token: rootViewModel.token) {
                        presentationMode.wrappedValue.dismiss()
                    }
                }
                .disabled(viewModel.name.isEmpty)
            }
        }
        .onAppear(perform: viewModel.onAppear)
        .navigationTitle(Text("Créer une équipe"))
    }
    
}

struct IntegrationCreateView_Previews: PreviewProvider {
    
    static var previews: some View {
        IntegrationCreateView()
    }
    
}
