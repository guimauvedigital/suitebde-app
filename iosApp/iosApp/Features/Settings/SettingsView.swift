//
//  SettingsView.swift
//  BDE
//
//  Created by Nathan Fallet on 14/02/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct SettingsView: View {
    
    @Environment(\.openURL) var openURL
    
    @EnvironmentObject var rootViewModel: RootViewModel
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
        }
        .navigationTitle(Text("Paramètres"))
    }
    
}

struct SettingsView_Previews: PreviewProvider {
    
    static var previews: some View {
        SettingsView()
    }
    
}
