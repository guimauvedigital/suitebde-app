//
//  EventView.swift
//  BDE
//
//  Created by Nathan Fallet on 10/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import shared
import SwiftUI
import KMPObservableViewModelSwiftUI
import KMPNativeCoroutinesAsync

struct EventView: View {
    
    @StateViewModel var viewModel: EventViewModel
    
    var body: some View {
        Group {
            if viewModel.isEditing {
                Form {
                    Section(header: Text("events_information")) {
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
                    }
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
                }
                .defaultNavigationTitle("events_title".localized())
                .defaultNavigationBackButtonHidden(false)
            } else if let event = viewModel.event {
                EventDetailsView(event: event)
            } else {
                ProgressView()
                    .padding()
            }
        }
        .onAppear {
            Task {
                try await asyncFunction(for: viewModel.onAppear())
            }
        }
        .refreshable {
            Task {
                try await asyncFunction(for: viewModel.fetchEvent(reset: true))
            }
        }
        .alert(
            item: Binding(get: { viewModel.alert }, set: { viewModel.setAlert(value: $0) }),
            content: constructAlertCase(discardEdit: viewModel.discardEditingFromAlert)
        )
        .defaultNavigationToolbar {
            if viewModel.isEditable {
                Button(viewModel.isEditing ? "app_done" : "app_edit", action: viewModel.toggleEditing)
            }
        }
    }
    
}
