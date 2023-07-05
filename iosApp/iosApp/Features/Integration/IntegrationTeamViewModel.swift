//
//  IntegrationTeamViewModel.swift
//  BDE
//
//  Created by Nathan Fallet on 04/07/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

class IntegrationTeamViewModel: ObservableObject {
    
    @Published var team: IntegrationTeam
    @Published var members = [IntegrationMembership]()
    @Published var executions = [IntegrationExecution]()
    @Published var member: Bool?
    
    init(team: IntegrationTeam) {
        self.team = team
    }
    
    func onAppear(token: String?, viewedBy: User?) {
        AnalyticsService.shared.log(.screenView(screenName: "integration_team", screenClass: "IntegrationTeamView"))
        
        fetchMembers(token: token, viewedBy: viewedBy)
        fetchExecutions(token: token)
    }
    
    func fetchMembers(token: String?, viewedBy: User?) {
        guard let token, let viewedBy else {
            return
        }
        Task {
            let members = try await CacheService.shared.apiService().getIntegrationTeamMembers(token: token, id: team.id)
            DispatchQueue.main.async {
                self.members = members
                self.member = members.contains(where: { $0.userId == viewedBy.id })
            }
        }
    }
    
    func fetchExecutions(token: String?) {
        guard let token else {
            return
        }
        Task {
            let executions = try await CacheService.shared.apiService().getIntegrationTeamExecutions(token: token, id: team.id)
            DispatchQueue.main.async {
                self.executions = executions
            }
        }
    }
    
    func join(token: String?, viewedBy: User?) {
        guard let token, let viewedBy else {
            return
        }
        Task {
            try await CacheService.shared.apiService().joinIntegrationTeam(token: token, id: team.id)
            fetchMembers(token: token, viewedBy: viewedBy)
        }
    }
    
    func leave(token: String?, viewedBy: User?) {
        guard let token, let viewedBy else {
            return
        }
        Task {
            try await CacheService.shared.apiService().leaveIntegrationTeam(token: token, id: team.id)
            fetchMembers(token: token, viewedBy: viewedBy)
        }
    }
    
}
