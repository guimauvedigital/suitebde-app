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
        ScanHistoryRootView(
            scans: viewModel.scans ?? [],
            loadMore: viewModel.loadMoreIfNeeded
        )
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
    }
    
}
