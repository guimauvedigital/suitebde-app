//
//  ScannerView.swift
//  BDE
//
//  Created by Nathan Fallet on 03/02/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import CodeScanner

struct ScannerView: View {
    
    @StateObject var viewModel: ScannerViewModel
    
    var body: some View {
        ZStack {
            CodeScannerView(
                codeTypes: [.qr],
                scanMode: .continuous,
                isTorchOn: viewModel.torchIsOn,
                isGalleryPresented: $viewModel.isGalleryPresented,
                completion: viewModel.completion
            )
            VStack {
                Spacer()
                if viewModel.isInvalidCode {
                    Text("QR Code invalide !")
                        .foregroundColor(.white)
                        .padding()
                        .background(Color.red)
                        .cornerRadius(10)
                }
                HStack {
                    Button(action: viewModel.toggleTorch) {
                        Image(systemName: viewModel.torchIsOn ? "bolt.fill" : "bolt.slash.fill")
                            .imageScale(.large)
                            .frame(width: 40, height: 40)
                            .foregroundColor(viewModel.torchIsOn ? Color.yellow : Color.accentColor)
                            .padding()
                    }
                    .background(Color(.systemGray6))
                    .cornerRadius(10)
                    Button(action: viewModel.openGallery) {
                        Image(systemName: "photo.fill")
                            .imageScale(.large)
                            .frame(width: 40, height: 40)
                            .padding()
                    }
                    .background(Color(.systemGray6))
                    .cornerRadius(10)
                }
            }
            .padding()
        }
        .onAppear(perform: viewModel.onAppear)
        .defaultNavigationTitle("qrcode_scan_title".localized())
        .defaultNavigationBackButtonHidden(false)
        .defaultNavigationToolbar {
            DefaultNavigationLink(destination: ScanHistoryView()) {
                Image(systemName: "list.bullet.clipboard")
            }
        }
    }
    
}
