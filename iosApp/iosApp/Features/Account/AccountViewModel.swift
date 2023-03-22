//
//  AccountViewModel.swift
//  BDE
//
//  Created by Nathan Fallet on 31/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import WidgetKit
import shared

class AccountViewModel: ObservableObject {
    
    let saveToken: (UserToken) -> Void
    
    @Published var url: String?
    @Published var error: String?
    @Published var qrcode: UIImage?
    @Published var tickets = [Ticket]()
    
    init(saveToken: @escaping (UserToken) -> Void) {
        self.saveToken = saveToken
    }
    
    func onAppear(token: String?, id: String?) {
        AnalyticsService.shared.log(.screenView(screenName: "account", screenClass: "AccountView"))
        
        fetchData(token: token, id: id)
    }
    
    func fetchData(token: String?, id: String?) {
        guard let token, let id else {
            return
        }
        Task {
            let tickets = try await APIService.shared.getUserTickets(token: token, id: id)
            DispatchQueue.main.async {
                self.tickets = tickets
            }
        }
    }
    
    func launchLogin() {
        url = APIService.shared.authenticationUrl
    }
    
    func onOpenUrl(url: URL) {
        guard url.host == "authorize" else { return }
        self.url = nil
        Task {
            let userToken = try await APIService.shared.authenticate(code: url.absoluteString)
            DispatchQueue.main.async {
                self.saveToken(userToken)
                WidgetCenter.shared.reloadAllTimelines()
            }
            guard let fcmToken = StorageService.userDefaults?.value(forKey: "fcmToken") as? String else {
                return
            }
            try await APIService.shared.sendNotificationToken(
                token: userToken.token,
                notificationToken: fcmToken
            )
        }
    }
    
    func generateQRCode(user: User) {
        Task {
            let code = "bdeensisa://users/\(user.id)".generateQRCode()
            DispatchQueue.main.async {
                self.qrcode = code
            }
        }
    }
    
}
