//
//  ScannerViewModel.swift
//  BDE
//
//  Created by Nathan Fallet on 03/02/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import CodeScanner
import CoreNFC
import shared

class ScannerViewModel: ObservableObject {
    
    let onURLFound: (URL) -> Void
    
    var nfcDelegate: NFCDelegate?
    var session: NFCTagReaderSession?
    
    @Published var isInvalidCode = false
    @Published var torchIsOn = false
    @Published var isGalleryPresented = false
    
    init(onURLFound: @escaping (URL) -> Void) {
        self.onURLFound = onURLFound
    }
    
    func onAppear() {
        AnalyticsService.shared.log(.screenView(screenName: "scanner", screenClass: "ScannerView"))
    }
    
    func completion(response: Result<ScanResult, ScanError>) {
        // Dismiss gallery if needed
        isGalleryPresented = false
        
        // Handle result
        if case let .success(result) = response {
            completion(string: result.string)
        } else {
            isInvalidCode = true
        }
    }
    
    func completion(string: String) {
        // Check URL with correct host
        guard let url = URL(string: string),
              url.scheme == "bdeensisa"
        else {
            isInvalidCode = true
            return
        }
        isInvalidCode = false
        
        // Handle result
        onURLFound(url)
        AnalyticsService.shared.log(.qrCodeScan)
    }
    
    func toggleTorch() {
        self.torchIsOn.toggle()
    }
    
    func openGallery() {
        self.isGalleryPresented = true
    }
    
    func launchNFC() {
        guard NFCNDEFReaderSession.readingAvailable else {
            return
        }
        nfcDelegate = NFCDelegate(
            token: nil,
            onResult: nfcResult
        )
        session = NFCTagReaderSession(
            pollingOption: [.iso14443],
            delegate: nfcDelegate!
        )
        session?.alertMessage = "Approchez une carte étudiante pour la scanner"
        session?.begin()
    }
    
    func nfcResult(token: String?, identifier: String?) {
        guard let identifier else {
            session?.invalidate()
            return
        }
        DispatchQueue.main.async {
            self.completion(string: "bdeensisa://nfc/\(identifier)")
        }
        session?.invalidate()
    }
    
}
