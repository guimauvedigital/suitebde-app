//
//  UsersView.swift
//  BDE
//
//  Created by Nathan Fallet on 06/02/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct UsersView: View {
    
    @EnvironmentObject var rootViewModel: OldRootViewModel
    @StateObject var viewModel = UsersViewModel()
    
    var body: some View {
        List {
            Section {
                ForEach(viewModel.searchUsers ?? viewModel.users, id: \.id) { user in
                    NavigationLink(
                        destination: {
                            UserView(viewModel: UserViewModel(
                                user: user,
                                editable: rootViewModel.user?.hasPermission(permission: "admin.users.edit") ?? false
                            ))
                        },
                        label: {
                            VStack(alignment: .leading) {
                                Text("\(user.firstName ?? "?") \(user.lastName ?? "?")")
                                    .lineLimit(1)
                                Text(user.description_)
                                    .font(.callout)
                                    .foregroundColor(.secondary)
                                    .lineLimit(1)
                                Text(
                                    user.cotisant != nil ?
                                    "Cotisant jusqu'au \(user.cotisant?.expiration.renderedDate ?? "?")" :
                                        "Non cotisant"
                                )
                                .font(.callout)
                                .foregroundColor(user.cotisant != nil ? Color.green : Color.red)
                                .lineLimit(1)
                            }
                        }
                    )
                    .onAppear {
                        viewModel.loadMore(token: rootViewModel.token, id: user.id)
                    }
                }
            }
        }
        .searchable(
            text: $viewModel.search,
            placement: .navigationBarDrawer(displayMode: .always)
        )
        .onReceive(viewModel.$search.debounce(for: 0.5, scheduler: RunLoop.main)) { search in
            viewModel.search(token: rootViewModel.token, reset: true)
        }
        .onAppear {
            viewModel.onAppear(token: rootViewModel.token)
        }
        .refreshable {
            viewModel.onAppear(token: rootViewModel.token)
        }
        .navigationTitle(Text("Utilisateurs"))
    }
}
