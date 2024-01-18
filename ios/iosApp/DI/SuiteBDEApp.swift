//
//  SuiteBDEApp.swift
//  BDE
//
//  Created by Nathan Fallet on 31/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import Firebase
import FirebaseMessaging
import BackgroundTasks
import shared

@main
struct SuiteBDEApp: App {
    
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate
    
    init() {
        KoinApplication.start()
        
        #if !DEBUG
        SentryKt.initializeSentry(context: nil)
        #endif
    }
    
	var body: some Scene {
		WindowGroup {
			RootView()
		}
	}
    
}

class AppDelegate: NSObject, UIApplicationDelegate, UNUserNotificationCenterDelegate, MessagingDelegate {
    
    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
    ) -> Bool {
        FirebaseApp.configure()
        Messaging.messaging().delegate = self
        
        Messaging.messaging().subscribe(toTopic: "broadcast")
        messaging(updateSubscriptionForTopic: "events")
        
        UNUserNotificationCenter.current().delegate = self

        let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
        UNUserNotificationCenter.current().requestAuthorization(
            options: authOptions,
            completionHandler: { _, _ in }
        )
        application.registerForRemoteNotifications()
        
        BGTaskScheduler.shared.register(forTaskWithIdentifier: "me.nathanfallet.bdeensisa.fetchevents", using: DispatchQueue.global()) { task in
             self.handleAppRefresh(task: task as! BGAppRefreshTask)
        }
        scheduleAppRefresh()
        
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
        guard let fcmToken else {
            return
        }
        StorageService.userDefaults?.set(fcmToken, forKey: "fcmToken")
        StorageService.userDefaults?.synchronize()
        guard let token = StorageService.keychain.value(forKey: "token") as? String else {
            return
        }
        Task {
            try await CacheService.shared.apiService().sendNotificationToken(
                token: token,
                notificationToken: fcmToken
            )
        }
    }
    
    func messaging(
        updateSubscriptionForTopic topic: String
    ) {
        if StorageService.userDefaults?.value(forKey: "notifications_\(topic)") as? Bool ?? true {
            Messaging.messaging().subscribe(toTopic: topic)
        } else {
            Messaging.messaging().unsubscribe(fromTopic: topic)
        }
    }
    
    func scheduleAppRefresh() {
        let request = BGProcessingTaskRequest(identifier: "me.nathanfallet.bdeensisa.fetchevents")
        request.requiresNetworkConnectivity = true
        request.earliestBeginDate = Date(timeIntervalSinceNow: 60 * 60) // 60 minutes
        
        do {
            try BGTaskScheduler.shared.submit(request)
        } catch {
            print("Could not schedule app refresh: \(error)")
        }
    }
    
    func handleAppRefresh(task: BGTask) {
        scheduleAppRefresh()
        Task {
            do {
                try await CacheService.shared.getEvents(
                    token: StorageService.keychain.value(forKey: "token") as? String,
                    reload: true
                )
                task.setTaskCompleted(success: true)
            } catch {
                task.setTaskCompleted(success: false)
            }
        }
    }
    
}
