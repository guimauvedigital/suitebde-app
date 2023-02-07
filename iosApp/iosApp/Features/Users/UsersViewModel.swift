//
//  UsersViewModel.swift
//  BDE
//
//  Created by Nathan Fallet on 06/02/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

class UsersViewModel: ObservableObject {
    
    @Published var search = ""
    @Published var users = [User]()
    @Published var searchUsers: [User]?
    @Published var hasMore = true
    
    func onAppear(token: String?) {
        AnalyticsService.shared.log(.screenView(screenName: "users", screenClass: "UsersView"))
        
        if !search.isEmpty {
            search(token: token, reset: true)
        } else {
            fetchData(token: token, reset: true)
        }
    }
    
    func fetchData(token: String?, reset: Bool) {
        guard let token else {
            return
        }
        Task {
            let users = try await APIService.shared.getUsers(
                token: token,
                offset: reset ? 0 : Int64(users.count),
                search: nil
            )
            DispatchQueue.main.async {
                if reset {
                    self.users = []
                }
                self.users.append(contentsOf: users)
                self.hasMore = !users.isEmpty
            }
        }
    }
    
    func search(token: String?, reset: Bool) {
        guard let token else {
            return
        }
        guard !search.isEmpty else {
            searchUsers = nil
            return
        }
        Task {
            let users = try await APIService.shared.getUsers(
                token: token,
                offset: reset ? 0 : Int64(searchUsers?.count ?? 0),
                search: search
            )
            DispatchQueue.main.async {
                if reset || self.searchUsers == nil {
                    self.searchUsers = []
                }
                self.searchUsers?.append(contentsOf: users)
                self.hasMore = !users.isEmpty
            }
        }
    }
    
    func loadMore(token: String?, id: String) {
        guard hasMore else {
            return
        }
        if !search.isEmpty {
            if id == searchUsers?.last?.id {
                search(token: token, reset: false)
            }
        } else {
            if id == users.last?.id {
                fetchData(token: token, reset: false)
            }
        }
    }
    
}
