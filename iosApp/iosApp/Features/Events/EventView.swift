//
//  EventView.swift
//  BDE
//
//  Created by Nathan Fallet on 10/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct EventView: View {
    
    @EnvironmentObject var rootViewModel: RootViewModel
    @StateObject var viewModel: EventViewModel
    
    var body: some View {
        Form {
            Section(header: Text("Informations")) {
                if viewModel.editing {
                    TextField("Titre", text: $viewModel.title)
                    DatePicker(
                        "Date de début",
                        selection: $viewModel.start
                    )
                    DatePicker(
                        "Date de fin",
                        selection: $viewModel.end
                    )
                    if viewModel.editable {
                        Toggle("Evènement validé", isOn: $viewModel.validated)
                    }
                } else {
                    VStack(alignment: .leading, spacing: 2) {
                        Text(viewModel.event?.title ?? "Evènement")
                        Text(viewModel.event?.renderedDate ?? "Date")
                            .foregroundColor(.secondary)
                    }
                }
            }
            if viewModel.editing {
                Section(header: Text("Contenu de l'évènement")) {
                    TextEditor(text: $viewModel.content)
                }
                Section {
                    Button("Enregistrer") {
                        viewModel.updateInfo(token: rootViewModel.token)
                    }
                }
            } else if let content = viewModel.event?.content, !content.isEmpty {
                Section {
                    Text(content)
                }
            }
        }
        .onAppear(perform: viewModel.onAppear)
        .toolbar {
            if viewModel.editable {
                Button(viewModel.editing ? "Terminé" : "Modifier", action: viewModel.toggleEdit)
            }
        }
        .alert(item: $viewModel.alert, content: constructAlertCase(discardEdit: viewModel.discardEdit))
        .navigationTitle(Text("Evènement"))
    }
    
}
