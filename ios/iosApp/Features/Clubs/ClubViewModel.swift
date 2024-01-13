//
//  ClubViewModel.swift
//  BDE
//
//  Created by Nathan Fallet on 13/02/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

class ClubViewModel: ObservableObject {
    
    @Published var club: Club
    @Published var members = [ClubMembership]()
    
    init(club: Club) {
        self.club = club
    }
    
    func onAppear() {
        AnalyticsService.shared.log(.screenView(screenName: "club", screenClass: "ClubView"))
        
        fetchMembers()
    }
    
    func fetchMembers() {
        Task {
            let members = try await CacheService.shared.apiService().getClubMembers(id: club.id)
            DispatchQueue.main.async {
                self.members = members
            }
        }
    }
    
    func join(token: String?) {
        guard let token else {
            return
        }
        Task {
            try await CacheService.shared.apiService().joinClub(token: token, id: club.id)
            fetchMembers()
        }
    }
    
    func leave(token: String?) {
        guard let token else {
            return
        }
        Task {
            try await CacheService.shared.apiService().leaveClub(token: token, id: club.id)
            fetchMembers()
        }
    }
    
}
