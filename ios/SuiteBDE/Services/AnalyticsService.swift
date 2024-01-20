//
//  AnalyticsService.swift
//  BDE
//
//  Created by Nathan Fallet on 07/02/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import Firebase
import FirebaseAnalytics

enum AnalyticsServiceEvent {
    
    case screenView(screenName: String, screenClass: String)
    case qrCodeScan
    
}

enum AnalyticsServiceDimension: String {
    
    case cotisant = "custom_cotisant"
    
}

class AnalyticsService {
    
    // Shared instance
    
    static let shared = AnalyticsService()
    
    // Methods
    
    func log(_ event: AnalyticsServiceEvent) {
        switch event {
        case .screenView(let screenName, let screenClass):
            Analytics.logEvent(AnalyticsEventScreenView, parameters: [
                AnalyticsParameterScreenName: screenName,
                AnalyticsParameterScreenClass: screenClass
            ])
        case .qrCodeScan:
            Analytics.logEvent("qrcode_scan", parameters: nil)
        }
    }
    
    func updateDimension(_ dimension: AnalyticsServiceDimension, value: Int) {
        Analytics.setUserProperty(String(value), forName: dimension.rawValue)
    }
    
    func updateDimension(_ dimension: AnalyticsServiceDimension, value: Bool) {
        Analytics.setUserProperty(String(value), forName: dimension.rawValue)
    }
    
}
