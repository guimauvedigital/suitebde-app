//
//  SuiteBDEApp.swift
//  BDE
//
//  Created by Nathan Fallet on 31/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

@main
struct SuiteBDEApp: App {
    
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate
    
    init() {
        KoinApplication.start()
        
        #if !DEBUG
        SentryKt.initializeSentry()
        #endif
    }
    
	var body: some Scene {
		WindowGroup {
			RootView()
		}
	}
    
}
