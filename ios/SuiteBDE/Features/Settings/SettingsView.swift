//
//  SettingsView.swift
//  BDE
//
//  Created by Nathan Fallet on 14/02/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import MyAppsiOS
import shared
import KMPObservableViewModelSwiftUI
import KMPNativeCoroutinesAsync

struct SettingsView: View {
    
    @EnvironmentObject var rootViewModel: RootViewModel
    @InjectStateViewModel var viewModel: SettingsViewModel
    
    @Environment(\.openURL) var openURL
    
    let developedWith = ["❤️", "Kotlin", "Swift", "Nathan Fallet", "Toast.cie"]
    
    var body: some View {
        Form {
            Section(header: Text("settings_notifications")) {
                Toggle(
                    "settings_notifications_events",
                    isOn: Binding(get: { viewModel.subscribedToEvents }, set: viewModel.subscribeToEvents)
                )
            }
            Section("settings_logout") {
                Button("settings_logout") {
                    rootViewModel.logout()
                }
            }
            Section(header: Text("settings_about")) {
                Text("settings_developed_with_love".localized().format(developedWith))
                    .onTapGesture {
                        if let url = URL(string: "https://suitebde.com") {
                            openURL(url)
                        }
                    }
                Button("settings_contact_us") {
                    if let url = URL(string: "mailto:hey@suitebde.com") {
                        openURL(url)
                    }
                }
            }
            Section(header: MyAppHeader()) {
                ForEach(MyAppExtensionKt.myAppsIos()) { app in
                    MyAppView(app: app)
                }
            }
        }
        .defaultNavigationTitle("settings_title".localized())
        .defaultNavigationBackButtonHidden(false)
        .onAppear(perform: viewModel.onAppear)
    }
    
}
