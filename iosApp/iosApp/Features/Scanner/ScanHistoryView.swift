//
//  ScanHistoryView.swift
//  BDE
//
//  Created by Nathan Fallet on 24/08/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct ScanHistoryView: View {
    
    @EnvironmentObject var rootViewModel: RootViewModel
    @StateObject var viewModel = ScanHistoryViewModel()
    
    var body: some View {
        List {
            ForEach(Array(viewModel.grouped.keys).sorted().reversed(), id: \.self) { key in
                let elements = viewModel.grouped[key] ?? []
                let title = elements.first?.event?.title ?? elements.first?.scannedAt.renderedDate
                let count = Set(elements.map { $0.userId }).count
                Section("\(title  ?? "") - \(count) personne(s)") {
                    ForEach(elements, id: \.scannedAt) { entry in
                        if let user = entry.user, let scanner = entry.scanner {
                            NavigationLink(
                                destination: {
                                    UserView(viewModel: UserViewModel(
                                        user: user,
                                        editable: rootViewModel.user?.hasPermission(permission: "admin.users.edit") ?? false
                                    ))
                                },
                                label: {
                                    HStack(spacing: 12) {
                                        Image(systemName: entry.type.scanIcon)
                                            .resizable()
                                            .aspectRatio(contentMode: .fit)
                                            .frame(maxWidth: 44, maxHeight: 44)
                                        VStack(alignment: .leading) {
                                            Text("\(user.firstName ?? "?") \(user.lastName ?? "?")")
                                                .lineLimit(1)
                                            Text("Scanné par \(scanner.firstName ?? "?") \(scanner.lastName ?? "?")")
                                                .font(.callout)
                                                .foregroundColor(.secondary)
                                                .lineLimit(1)
                                            Text(entry.scannedAt.renderedDateTime)
                                                .font(.callout)
                                                .foregroundColor(.secondary)
                                                .lineLimit(1)
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
        .onAppear {
            viewModel.onAppear(token: rootViewModel.token)
        }
        .refreshable {
            viewModel.onAppear(token: rootViewModel.token)
        }
        .navigationTitle(Text("Historique de scan"))
    }
    
}

struct ScanHistoryView_Previews: PreviewProvider {
    
    static var previews: some View {
        ScanHistoryView()
    }
    
}
