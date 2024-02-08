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
import KMMViewModelSwiftUI
import KMPNativeCoroutinesAsync

struct SettingsView: View {
    
    @InjectStateViewModel var viewModel: SettingsViewModel
    
    @Environment(\.openURL) var openURL
    
    let developedWith = ["❤️", "Kotlin", "Swift", "Nathan Fallet", "Toast.cie"]
    
    var body: some View {
        Form {
            Section("settings_logout") {
                Button("settings_logout") {
                    Task {
                        try await asyncFunction(for: viewModel.logout())
                    }
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
                ForEach(MyApp.values) { app in
                    MyAppView(app: app)
                }
            }
        }
        .defaultNavigationTitle("settings_title")
        .onAppear {
            Task {
                try await asyncFunction(for: viewModel.onAppear())
            }
        }
    }
    
}

struct SettingsView_Previews: PreviewProvider {
    
    static var previews: some View {
        SettingsView()
    }
    
}
