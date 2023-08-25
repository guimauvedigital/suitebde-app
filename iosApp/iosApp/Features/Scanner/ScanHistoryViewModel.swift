//
//  ScanHistoryViewModel.swift
//  BDE
//
//  Created by Nathan Fallet on 24/08/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

class ScanHistoryViewModel: ObservableObject {
    
    @Published var entries = [ScanHistoryEntry]()
    @Published var grouped = [String: [ScanHistoryEntry]]()
    @Published var hasMore = true
    
    func onAppear(token: String?) {
        AnalyticsService.shared.log(.screenView(screenName: "scan_history", screenClass: "ScanHistoryView"))
        
        fetchData(token: token, reset: true)
    }
    
    func fetchData(token: String?, reset: Bool) {
        guard let token else {
            return
        }
        Task {
            let entries = try await CacheService.shared.apiService().getScanHistory(
                token: token,
                offset: reset ? 0 : Int64(entries.count)
            )
            DispatchQueue.main.async {
                if reset {
                    self.entries = []
                }
                self.entries.append(contentsOf: entries)
                self.hasMore = !entries.isEmpty
                self.grouped = Dictionary(grouping: self.entries) {
                    "\($0.scannedAt.asDate.asString)-\($0.event?.id ?? "")"
                }
            }
        }
    }
    
    func loadMore(token: String?, scannedAt: Kotlinx_datetimeInstant) {
        guard hasMore else {
            return
        }
        if scannedAt == entries.last?.scannedAt {
            fetchData(token: token, reset: false)
        }
    }
    
}
