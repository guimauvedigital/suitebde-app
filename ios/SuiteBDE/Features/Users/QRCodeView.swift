//
//  QRCodeView.swift
//  BDE
//
//  Created by Nathan Fallet on 03/03/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared
import KMMViewModelSwiftUI
import KMPNativeCoroutinesAsync

struct QRCodeView: View {
    
    @InjectStateViewModel var viewModel: QRCodeViewModel
    
    var body: some View {
        Group {
            if let user = viewModel.user, let qrCodeUrl = viewModel.qrCodeUrl {
                QRCodeRootView(
                    user: user,
                    qrCodeUrl: qrCodeUrl
                )
            } else {
                ProgressView()
                    .padding()
            }
        }
        .onAppear {
            Task {
                try await asyncFunction(for: viewModel.onAppear())
            }
        }
    }
    
}
