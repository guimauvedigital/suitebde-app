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
    
    func onAppear(token: String?) {
        AnalyticsService.shared.log(.screenView(screenName: "clubs", screenClass: "ClubsView"))
        
        fetchClubs(token: token)
    }
    
    func fetchClubs(token: String?) {
        Task {
            let clubs = try await APIService.shared.getClubs()
            DispatchQueue.main.async {
                self.clubs = clubs
            }
        }
        Task {
            let clubs = try await APIService.shared.getClubsMe(token: token)
            DispatchQueue.main.async {
                self.mine = clubs
            }
        }
    }
    
}
