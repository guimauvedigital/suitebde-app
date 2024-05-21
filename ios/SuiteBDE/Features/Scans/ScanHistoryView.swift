//
//  ScanHistoryView.swift
//  BDE
//
//  Created by Nathan Fallet on 24/08/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared
import KMPObservableViewModelSwiftUI
import KMPNativeCoroutinesAsync

struct ScanHistoryView: View {
    
    @InjectStateViewModel var viewModel: ScanHistoryViewModel
    
    var body: some View {
        List {
            ForEach(viewModel.scans ?? [], id: \.date) { day in
                let title = day.date.renderedDate
                let count = Set(day.scans.map { $0.userId }).count
                Section("\(title) - \(count) personne(s)") {
                    ForEach(day.scans, id: \.id) { entry in
                        NavigationLink(
                            destination: {
                                UserView(viewModel: KoinApplication.shared.koin.userViewModel(
                                    associationId: entry.associationId, userId: entry.userId
                                ))
                            },
                            label: {
                                HStack(spacing: 12) {
                                    
                                    VStack(alignment: .leading) {
                                        Text("\(entry.user?.firstName ?? "?") \(entry.user?.lastName ?? "?")")
                                            .lineLimit(1)
                                        Text("Scanné par \(entry.scanner?.firstName ?? "?") \(entry.scanner?.lastName ?? "?")")
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
        .onAppear {
            Task {
                try await asyncFunction(for: viewModel.onAppear())
            }
        }
        .refreshable {
            Task {
                try await asyncFunction(for: viewModel.fetchScansForDays(reset: true))
            }
        }
        .navigationTitle(Text("Historique de scan"))
    }
    
}
