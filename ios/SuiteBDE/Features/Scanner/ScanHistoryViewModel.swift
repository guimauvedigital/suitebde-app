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
    
    func onAppear(token: String?) {
        AnalyticsService.shared.log(.screenView(screenName: "scan_history", screenClass: "ScanHistoryView"))
        
        fetchData(token: token)
    }
    
    func fetchData(token: String?) {
        guard let token else {
            return
        }
        Task {
            let entries = try await CacheService.shared.apiService().getScanHistory(token: token)
            DispatchQueue.main.async {
                self.entries = entries
                self.grouped = Dictionary(grouping: self.entries) {
                    "\($0.scannedAt.asDate.asString)-\($0.event?.id ?? "")"
                }
            }
        }
    }
    
}
