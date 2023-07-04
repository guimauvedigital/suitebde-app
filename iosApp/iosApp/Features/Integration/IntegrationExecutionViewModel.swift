//
//  IntegrationExecutionViewModel.swift
//  BDE
//
//  Created by Nathan Fallet on 04/07/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

class IntegrationExecutionViewModel: ObservableObject {
    
    @Published var challenges = [IntegrationChallenge]()
    @Published var challenge = ""
    
    func onAppear(token: String?) {
        AnalyticsService.shared.log(.screenView(screenName: "integration_execution", screenClass: "IntegrationExecutionView"))
        
        fetchChallenges(token: token)
    }
    
    func fetchChallenges(token: String?) {
        guard let token else {
            return
        }
        Task {
            let challenges = try await CacheService.shared.apiService().getIntegrationChallenges(token: token)
            DispatchQueue.main.async {
                self.challenges = challenges
            }
        }
    }
    
    func createExecution(token: String?, completionHandler: @escaping () -> Void) {
        guard let token else {
            return
        }
        Task {
            
        }
    }
    
}
