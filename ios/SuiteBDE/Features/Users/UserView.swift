//
//  UserView.swift
//  BDE
//
//  Created by Nathan Fallet on 03/02/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared
import KMMViewModelSwiftUI
import KMPNativeCoroutinesAsync

struct UserView: View {
    
    @EnvironmentObject var rootViewModel: OldRootViewModel
    @StateObject var oldViewModel: OldUserViewModel
    
    @StateViewModel var viewModel: UserViewModel
    
    var body: some View {
        Group {
            if viewModel.isEditing {
                Form {
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
                    Section(header: Text("Informations")) {
                        TextField("Prénom", text: $oldViewModel.firstName)
                        TextField("Nom", text: $oldViewModel.lastName)
                        Picker("Année", selection: $oldViewModel.year) {
                            Text("1A").tag("1A")
                            Text("2A").tag("2A")
                            Text("3A").tag("3A")
                            Text("4A et plus").tag("other")
                            Text("CPB").tag("CPB")
                        }
                        Picker("Option", selection: $oldViewModel.option) {
                            Text("Informatique et Réseaux").tag("ir")
                            Text("Automatique et Systèmes embarqués").tag("ase")
                            Text("Mécanique").tag("meca")
                            Text("Textile et Fibres").tag("tf")
                            Text("Génie Industriel").tag("gi")
                        }
                        Button("Enregistrer") {
                            oldViewModel.updateInfo(token: rootViewModel.token) { newUser in
                                if oldViewModel.isMyAccount {
                                    rootViewModel.user = newUser
                                }
                            }
                        }
                    }
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
                }
            } else if let user = viewModel.user {
                UserDetailsView(user: user)
            } else {
                ProgressView()
                    .padding()
            }
        }
        .navigationTitle(Text("Utilisateur"))
        .toolbar {
            if oldViewModel.editable {
                Button(oldViewModel.editing ? "Terminé" : "Modifier", action: oldViewModel.toggleEdit)
            }
        }
        .sheet(isPresented: $oldViewModel.imagePickerShown) {
            ImagePicker(
                filter: .images,
                imageSelected: { image in
                    oldViewModel.updateImage(token: rootViewModel.token, image: image)
                },
                videoSelected: { _ in }
            )
        }
        .alert(item: $oldViewModel.alert, content: constructAlertCase(
            discardEdit: oldViewModel.discardEdit,
            deleteAccount: rootViewModel.deleteAccount
        ))
        .onAppear {
            oldViewModel.onAppear(token: rootViewModel.token, viewedBy: rootViewModel.user)
        }
    }
    
}
