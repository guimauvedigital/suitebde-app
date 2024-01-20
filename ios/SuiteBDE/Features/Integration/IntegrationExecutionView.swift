//
//  IntegrationExecutionView.swift
//  BDE
//
//  Created by Nathan Fallet on 04/07/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct IntegrationExecutionView: View {
    
    @Environment(\.presentationMode) var presentationMode
    
    @EnvironmentObject var rootViewModel: RootViewModel
    @StateObject var viewModel: IntegrationExecutionViewModel
    
    var body: some View {
        Form {
            Section(header: Text("Défi à compléter")) {
                Picker("Défi", selection: $viewModel.challenge) {
                    Text("Sélectionnez...").tag("")
                    ForEach(viewModel.challenges, id: \.id) { challenge in
                        Text(challenge.name).tag(challenge.id)
                    }
                }
            }
            Section(header: Text("Preuve")) {
                if !viewModel.filename.isEmpty {
                    Text(viewModel.filename)
                }
                Button("Sélectionner", action: viewModel.showImagePicker)
            }
            Section {
                if viewModel.uploading {
                    HStack {
                        Spacer()
                        ProgressView()
                            .progressViewStyle(.circular)
                        Spacer()
                    }
                } else {
                    Button("Proposer") {
                        viewModel.createExecution(token: rootViewModel.token) {
                            presentationMode.wrappedValue.dismiss()
                        }
                    }
                    .disabled(viewModel.challenge.isEmpty || viewModel.filename.isEmpty)
                }
            }
        }
        .sheet(isPresented: $viewModel.imagePickerShown) {
            ImagePicker(
                filter: nil,
                imageSelected: viewModel.imageSelected,
                videoSelected: viewModel.videoSelected
            )
        }
        .alert(item: $viewModel.error) { error in
            Alert(
                title: Text("Une erreur est survenue !"),
                message: Text(error)
            )
        }
        .onAppear {
            viewModel.onAppear(token: rootViewModel.token)
        }
        .navigationTitle(Text("Compléter un défi"))
    }
    
}
