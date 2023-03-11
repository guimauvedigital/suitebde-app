//
//  SettingsView.swift
//  BDE
//
//  Created by Nathan Fallet on 14/02/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import MyAppsiOS

struct SettingsView: View {
    
    @Environment(\.openURL) var openURL
    
    @StateObject var viewModel = SettingsViewModel()
    
    var body: some View {
        Form {
            Section(header: Text("Notifications")) {
                Toggle("Evènements", isOn: $viewModel.eventsNotifications)
            }
            Section(header: Text("A propos")) {
                Text("Développée avec ❤️ en Swift et Kotlin par Nathan Fallet")
                    .onTapGesture {
                        if let url = URL(string: "https://nathanfallet.me") {
                            openURL(url)
                        }
                    }
                Button("Site du BDE") {
                    if let url = URL(string: "https://bdensisa.org") {
                        openURL(url)
                    }
                }
                Button("Contacter le BDE") {
                    if let url = URL(string: "mailto:bde@bdensisa.org") {
                        openURL(url)
                    }
                }
                Button("Contacter le développeur") {
                    if let url = URL(string: "mailto:dev@bdensisa.org") {
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
        .navigationTitle(Text("Paramètres"))
        .onAppear(perform: viewModel.onAppear)
    }
    
}

struct SettingsView_Previews: PreviewProvider {
    
    static var previews: some View {
        SettingsView()
    }
    
}
