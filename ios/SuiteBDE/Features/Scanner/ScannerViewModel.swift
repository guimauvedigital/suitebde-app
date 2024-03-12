//
//  ScannerViewModel.swift
//  BDE
//
//  Created by Nathan Fallet on 03/02/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import CodeScanner
import shared

class ScannerViewModel: ObservableObject {
    
    let onURLFound: (URL) -> Void
    
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
              url.scheme == "bdeensisa" || url.scheme == "suitebde"
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
    
}
