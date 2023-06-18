//
//  NFCDelegate.swift
//  BDE
//
//  Created by Nathan Fallet on 18/06/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import CoreNFC

class NFCDelegate: NSObject, NFCTagReaderSessionDelegate {
    
    let token: String?
    let onResult: (String?, String?) -> Void
    
    init(
        token: String?,
        onResult: @escaping (String?, String?) -> Void
    ) {
        self.token = token
        self.onResult = onResult
    }
    
    func tagReaderSessionDidBecomeActive(_ session: NFCTagReaderSession) {
        
    }
    
    func tagReaderSession(_ session: NFCTagReaderSession, didInvalidateWithError error: Error) {
        onResult(token, nil)
    }
    
    func tagReaderSession(_ session: NFCTagReaderSession, didDetect tags: [NFCTag]) {
        guard let tag = tags.first,
              case .iso7816(let iso7816) = tag
        else { return }
        
        onResult(token, iso7816.formattedIdentifier)
    }
    
}
