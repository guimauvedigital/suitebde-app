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
import CoreNFC
import shared

class AccountViewModel: ObservableObject {
    
    let saveToken: (UserToken) -> Void
    
    var nfcDelegate: NFCDelegate?
    var session: NFCTagReaderSession?
    
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
            let tickets = try await CacheService.shared.apiService().getUserTickets(token: token, id: id)
            DispatchQueue.main.async {
                self.tickets = tickets
            }
        }
    }
    
    func launchLogin() {
        url = CacheService.shared.apiService().authenticationUrl
    }
    
    func onOpenUrl(url: URL) {
        guard url.host == "authorize" else { return }
        self.url = nil
        Task {
            let userToken = try await CacheService.shared.apiService().authenticate(code: url.absoluteString)
            DispatchQueue.main.async {
                self.saveToken(userToken)
                WidgetCenter.shared.reloadAllTimelines()
            }
            guard let fcmToken = StorageService.userDefaults?.value(forKey: "fcmToken") as? String else {
                return
            }
            try await CacheService.shared.apiService().sendNotificationToken(
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
    
    func launchPayment(
        token: String?,
        shopItemType: String,
        shopItemId: String,
        itemId: String
    ) {
        guard let token else {
            return
        }
        Task {
            let response = try await CacheService.shared.apiService()
                .getShopItemPayment(token: token, type: shopItemType, id: shopItemId, itemId: itemId)
            DispatchQueue.main.async {
                self.url = response.url
            }
        }
    }
    
    func launchNFC(token: String?) {
        guard NFCNDEFReaderSession.readingAvailable else {
            error = "La lecture NFC n'est pas supportée."
            return
        }
        nfcDelegate = NFCDelegate(
            token: token,
            onResult: nfcResult
        )
        session = NFCTagReaderSession(
            pollingOption: [.iso14443],
            delegate: nfcDelegate!
        )
        session?.alertMessage = "Veuillez approcher votre carte étudiante pour l'enregistrer"
        session?.begin()
    }
    
    func nfcResult(token: String?, identifier: String?) {
        guard let identifier, let token else {
            session?.invalidate()
            return
        }
        Task {
            let response = try await CacheService.shared.apiService()
                .postNFC(token: token, id: identifier)
            DispatchQueue.main.async {
                self.saveToken(UserToken(
                    token: token,
                    user: response
                ))
            }
        }
        session?.invalidate()
    }
    
}
