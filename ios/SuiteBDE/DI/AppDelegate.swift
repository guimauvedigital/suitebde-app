//
//  AppDelegate.swift
//  BDE
//
//  Created by Nathan Fallet on 17/03/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import Firebase
import FirebaseMessaging
import BackgroundTasks
import shared

class AppDelegate: NSObject, UIApplicationDelegate, UNUserNotificationCenterDelegate, MessagingDelegate {
    
    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
    ) -> Bool {
        let dev = Bundle.main.bundleIdentifier?.hasSuffix(".dev") == true
        let filePath = Bundle.main.path(forResource: "GoogleService-Info\(dev ? ".dev" : "")", ofType: "plist")
        guard let fileopts = FirebaseOptions(contentsOfFile: filePath!) else {
            assert(false, "Couldn't load config file")
            return false
        }
        FirebaseApp.configure(options: fileopts)
        Messaging.messaging().delegate = self
        
        UNUserNotificationCenter.current().delegate = self

        let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
        UNUserNotificationCenter.current().requestAuthorization(
            options: authOptions,
            completionHandler: { _, _ in }
        )
        application.registerForRemoteNotifications()
        
        return true
    }
    
    func application(
        _ application: UIApplication,
        didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data
    ) {
        Messaging.messaging().apnsToken = deviceToken
    }
    
    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        willPresent notification: UNNotification,
        withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void
    ) {
        // Don't show notification for current conversation
        if let conversationId = WebSocketService.shared.currentConversationId,
           notification.request.content.userInfo["conversationId"] as? String == conversationId {
            completionHandler([])
            return
        }
        
        completionHandler([.banner, .badge, .sound])
    }
    
    func messaging(
        _ messaging: Messaging,
        didReceiveRegistrationToken fcmToken: String?
    ) {
        guard let fcmToken else { return }
        
        SwiftModule.shared.tokenRepository.setFcmToken(fcmToken: fcmToken)
        Task {
            try await KoinApplication.shared.koin.setupNotificationsUseCase.invoke()
        }
    }
    
}
