//
//  ClubsViewModel.swift
//  BDE
//
//  Created by Nathan Fallet on 13/02/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

class ClubsViewModel: ObservableObject {
    
    @Published var mine = [ClubMembership]()
    @Published var clubs = [Club]()
    @Published var hasMore = true
    
    func onAppear(token: String?) {
        AnalyticsService.shared.log(.screenView(screenName: "clubs", screenClass: "ClubsView"))
        
        fetchClubs(reset: true)
        fetchMine(token: token)
    }
    
    func fetchClubs(reset: Bool) {
        Task {
            let clubs = try await APIService.shared.getClubs(
                offset: reset ? 0 : Int64(clubs.count)
            )
            DispatchQueue.main.async {
                if reset {
                    self.clubs = []
                }
                self.clubs.append(contentsOf: clubs)
                self.hasMore = !clubs.isEmpty
            }
        }
    }
    
    func fetchMine(token: String?) {
        guard let token else {
            return
        }
        Task {
            let clubs = try await APIService.shared.getClubsMe(token: token)
            DispatchQueue.main.async {
                self.mine = clubs
            }
        }
    }
    
    func loadMore(token: String?, id: String?) {
        guard hasMore else {
            return
        }
        if id == clubs.last?.id {
            fetchClubs(reset: false)
        }
    }
    
    func joinClub(id: String, token: String?) {
        guard let token else {
            return
        }
        Task {
            try await APIService.shared.joinClub(token: token, id: id)
            fetchMine(token: token)
        }
    }
    
}
