//
//  IntegrationTeamsViewModel.swift
//  BDE
//
//  Created by Nathan Fallet on 04/07/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

class IntegrationTeamsViewModel: ObservableObject {
    
    @Published var teams = [IntegrationTeam]()
    @Published var challenges = [IntegrationChallenge]()
    
    func onAppear(token: String?) {
        AnalyticsService.shared.log(.screenView(screenName: "integration_teams", screenClass: "IntegrationTeamsView"))
        
        fetchTeams(token: token)
        fetchChallenges(token: token)
    }
    
    func fetchTeams(token: String?) {
        guard let token else {
            return
        }
        Task {
            let teams = try await CacheService.shared.apiService().getIntegrationTeams(token: token)
            DispatchQueue.main.async {
                self.teams = teams
            }
        }
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
    
}
