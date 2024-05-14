//
//  UserView.swift
//  BDE
//
//  Created by Nathan Fallet on 03/02/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared
import KMPObservableViewModelSwiftUI
import KMPNativeCoroutinesAsync

struct UserView: View {
    
    @StateViewModel var viewModel: UserViewModel
    
    var body: some View {
        Group {
            if viewModel.isEditing {
                Form {
                    /*
                    Section(header: Text("Photo d'identité")) {
                        HStack {
                            if let image = oldViewModel.image {
                                Image(uiImage: image)
                                    .resizable()
                                    .aspectRatio(contentMode: .fill)
                                    .frame(width: 44, height: 45)
                                    .clipShape(Circle())
                            }
                            Button(oldViewModel.image != nil ? "Modifier la photo" : "Ajouter une photo", action: oldViewModel.showImagePicker)
                        }
                    }
                     */
                    Section(header: Text("Informations")) {
                        TextField(
                            "Prénom",
                            text: Binding(get: { viewModel.firstName }, set: { viewModel.updateFirstName(value: $0) })
                        )
                        TextField(
                            "Nom",
                            text: Binding(get: { viewModel.lastName }, set: { viewModel.updateLastName(value: $0) })
                        )
                        Button("app_save") {
                            Task {
                                try await asyncFunction(for: viewModel.saveChanges())
                            }
                        }
                    }
                    /*
                    if oldViewModel.isMyAccount {
                        Section {
                            Button("Supprimer mon compte") {
                                oldViewModel.alert = .deleting
                            }
                            .foregroundColor(.red)
                        }
                    }
                    if !oldViewModel.editing || rootViewModel.user?.hasPermission(permission: "admin.users.edit") ?? false {
                        Section(header: Text("Cotisation")) {
                            Text(oldViewModel.user.cotisant != nil ? "Cotisant" : "Non cotisant")
                                .foregroundColor(oldViewModel.user.cotisant != nil ? Color.green : Color.red)
                            if let cotisant = oldViewModel.user.cotisant, !oldViewModel.editing {
                                Text("Expire : \(cotisant.expiration.renderedDate)")
                            }
                            if oldViewModel.editing {
                                DatePicker(
                                    "Expire",
                                    selection: $oldViewModel.expiration,
                                    displayedComponents: .date
                                )
                                Button("1 jour") {
                                    oldViewModel.expiration = .tomorrow
                                }
                                Button("1 an") {
                                    oldViewModel.expiration = .oneYear
                                }
                                Button("Scolarité") {
                                    oldViewModel.expiration = .fiveYears
                                }
                                Button("Enregistrer") {
                                    oldViewModel.updateExpiration(token: rootViewModel.token)
                                }
                            }
                        }
                    }
                    if (
                        !oldViewModel.tickets.isEmpty &&
                        (!oldViewModel.editing || rootViewModel.user?.hasPermission(permission: "admin.tickets.edit") ?? false)
                    ) {
                        Section(header: Text("Tickets")) {
                            ForEach(oldViewModel.tickets, id: \.id) { ticket in
                                VStack(alignment: .leading, spacing: 8) {
                                    HStack {
                                        Text(ticket.event?.title ?? "")
                                        Spacer()
                                        Picker("", selection: Binding(
                                            get: {
                                                oldViewModel.paid[ticket.id] ?? false
                                            },
                                            set: { value in
                                                oldViewModel.paid[ticket.id] = value
                                                oldViewModel.updateTicket(
                                                    token: rootViewModel.token,
                                                    id: ticket.id
                                                )
                                            }
                                        )) {
                                            Text("Payé").tag(true)
                                            Text("Non payé").tag(false)
                                        }
                                    }
                                    Text(ticket.event?.content ?? "")
                                        .lineLimit(5)
                                }
                            }
                        }
                    }
                     */
                }
                .defaultNavigationTitle("users_title".localized())
                .defaultNavigationBackButtonHidden(false)
            } else if let user = viewModel.user {
                UserDetailsView(
                    user: user,
                    subscriptions: viewModel.subscriptions ?? []
                )
            } else {
                ProgressView()
                    .padding()
            }
        }
        /*
        .sheet(isPresented: $oldViewModel.imagePickerShown) {
            ImagePicker(
                filter: .images,
                imageSelected: { image in
                    oldViewModel.updateImage(token: rootViewModel.token, image: image)
                },
                videoSelected: { _ in }
            )
        }
         */
        .onAppear {
            Task {
                try await asyncFunction(for: viewModel.onAppear())
            }
        }
        .refreshable {
            Task {
                try await asyncFunction(for: viewModel.fetchUser())
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
