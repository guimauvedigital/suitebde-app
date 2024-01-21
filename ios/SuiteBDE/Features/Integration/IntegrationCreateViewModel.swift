//
//  IntegrationCreateViewModel.swift
//  BDE
//
//  Created by Nathan Fallet on 04/07/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

class IntegrationCreateViewModel: ObservableObject {
    
    @Published var name = ""
    @Published var description = ""
    
    func onAppear() {
        AnalyticsService.shared.log(.screenView(screenName: "integration_create", screenClass: "IntegrationCreateView"))
    }
    
    func createTeam(token: String?, completionHandler: @escaping () -> Void) {
        guard let token else {
            return
        }
        Task {
            try await CacheService.shared.apiService().postIntegrationTeams(
                token: token,
                name: self.name,
                description: self.description
            )
            DispatchQueue.main.async {
                completionHandler()
            }
        }
    }
    
}
