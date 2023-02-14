//
//  ManageView.swift
//  BDE
//
//  Created by Nathan Fallet on 31/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct ManageView: View {
    
    @EnvironmentObject var rootViewModel: RootViewModel
    @StateObject var viewModel: ManageViewModel
    
    var body: some View {
        NavigationView {
            Form {
                Section(header: Text("Scan")) {
                    NavigationLink(
                        "Scanner un QR Code",
                        destination: ScannerView(viewModel: ScannerViewModel(
                            onURLFound: rootViewModel.onOpenURL
                        ))
                    )
                }
                if rootViewModel.user?.hasPermission(permission: "admin.users.view") ?? false {
                    Section(header: Text("Utilisateurs")) {
                        NavigationLink(
                            "Liste des utilisateurs",
                            destination: UsersView()
                        )
                    }
                }
                if rootViewModel.user?.hasPermission(permission: "admin.notifications") ?? false {
                    Section(header: Text("Notifications")) {
                        NavigationLink(
                            "Envoyer une notification",
                            destination: SendNotificationView()
                        )
                    }
                }
            }
            .navigationTitle(Text("Gestion"))
            .onAppear(perform: viewModel.onAppear)
        }
        .navigationViewStyle(StackNavigationViewStyle())
    }
    
}

struct ManageView_Previews: PreviewProvider {
    
    static var previews: some View {
        ManageView(viewModel: ManageViewModel())
    }
    
}
