//
//  StringExtension.swift
//  BDE
//
//  Created by Nathan Fallet on 31/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import CoreImage.CIFilterBuiltins
import UIKit

extension String {
    
    // Localization
    
    func format(_ args: CVarArg...) -> String {
        return String(format: self, locale: .current, arguments: args)
    }
    
    func format(_ args: [String]) -> String {
        return String(format: self, locale: .current, arguments: args)
    }
    
    // Trimming
    
    func trim() -> String {
        return trimmingCharacters(in: .whitespacesAndNewlines)
    }
    
    // Date related
    
    var asDate: Date? {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd"
        return formatter.date(from: self)
    }
    
    // QR Code
    
    func generateQRCode() -> UIImage? {
        // Export data
        guard let data = data(using: .utf8) else { return nil }
        
        // Settings
        let context = CIContext()
        let filter = CIFilter.qrCodeGenerator()
        filter.setValue(data, forKey: "inputMessage")
        
        // Export image
        guard let outputImage = filter.outputImage else { return nil }
        
        // Color it
        let colorParameters = [
            "inputColor0": CIColor(color: .white), // Foreground
            "inputColor1": CIColor(color: .clear) // Background
        ]
        let coloredImage = outputImage.applyingFilter("CIFalseColor", parameters: colorParameters)
        
        // Export
        guard let cgImage = context.createCGImage(coloredImage, from: coloredImage.extent) else { return nil }
        return UIImage(cgImage: cgImage)
    }
    
}

extension String: Identifiable {
    
    public var id: String { self }
    
}
