//
//  EventView.swift
//  BDE
//
//  Created by Nathan Fallet on 10/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import shared
import SwiftUI
import KMMViewModelSwiftUI
import KMPNativeCoroutinesAsync

struct EventView: View {
    
    @StateViewModel var viewModel: EventViewModel
    
    var body: some View {
        Form {
            Section(header: Text("events_information")) {
                if viewModel.isEditing {
                    TextField(
                        "events_name",
                        text: Binding(get: { viewModel.name }, set: { viewModel.updateName(value: $0) })
                    )
                    DatePicker(
                        "events_startsAt",
                        selection: Binding(get: { viewModel.startsAt.asDate }, set: { viewModel.updateStartsAt(value: $0.asKotlinxInstant) })
                    )
                    DatePicker(
                        "events_endsAt",
                        selection: Binding(get: { viewModel.endsAt.asDate }, set: { viewModel.updateEndsAt(value: $0.asKotlinxInstant) })
                    )
                    if viewModel.isEditable {
                        Toggle(
                            "events_validated",
                            isOn: Binding(get: { viewModel.validated }, set: { viewModel.updateValidated(value: $0) })
                        )
                    }
                } else if let event = viewModel.event {
                    HStack(spacing: 12) {
                        Image(systemName: "calendar.circle")
                            .resizable()
                            .frame(width: 44, height: 44)
                        VStack(alignment: .leading) {
                            Text(event.name)
                            Text(event.renderedDate)
                                .foregroundColor(.secondary)
                        }
                        Spacer()
                    }
                    .foregroundColor(.primary)
                    .multilineTextAlignment(.leading)
                }
            }
            if viewModel.isEditing {
                Section(header: Text("events_description")) {
                    TextEditor(
                        text: Binding(get: { viewModel.description_ }, set: { viewModel.updateDescription(value: $0) })
                    )
                }
                Section {
                    Button("app_save") {
                        Task {
                            try await asyncFunction(for: viewModel.saveChanges())
                        }
                    }
                }
            } else if let description = viewModel.event?.description_, !description.isEmpty {
                Section {
                    Text(description)
                }
            }
        }
        .onAppear {
            Task {
                try await asyncFunction(for: viewModel.onAppear())
            }
        }
        .toolbar {
            if viewModel.isEditable {
                Button(viewModel.isEditing ? "app_done" : "app_edit", action: viewModel.toggleEditing)
            }
        }
        .alert(
            item: Binding(get: { viewModel.alert }, set: { viewModel.setAlert(value: $0) }),
            content: constructAlertCase(discardEdit: viewModel.discardEditingFromAlert)
        )
        .navigationTitle(Text("events_title"))
    }
    
}
