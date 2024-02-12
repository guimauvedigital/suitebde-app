//
//  StringExtension.swift
//  BDE
//
//  Created by Nathan Fallet on 31/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import CoreImage.CIFilterBuiltins
import SwiftUI

extension String {
    
    // Localization
    
    func localized(bundle: Bundle = .main, tableName: String = "Localizable") -> String {
        return NSLocalizedString(self, tableName: tableName, value: "**\(self)**", comment: "")
    }
    
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
    
    var asDateWithTime: Date? {
        let formatter = DateFormatter()
        formatter.locale = Locale(identifier: "en_US_POSIX")
        formatter.timeZone = TimeZone(abbreviation: "GMT")
        formatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'"
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
    
    // Icons
    
    var initials: String {
        components(separatedBy: .whitespacesAndNewlines)
            .map { String($0.first ?? " ").uppercased() }
            .filter { "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".contains($0) }
            .joined()
    }
    
    func chatLogo(backup: String? = nil, size: CGFloat = 44) -> some View {
        Text(
            backup?.uppercased() ?? initials
        )
        .lineLimit(1)
        .font(.system(size: size/3))
        .frame(width: size, height: size)
        .background(Color(.systemGray6))
    }
    
    // Scan history icon
    
    var scanIcon: String {
        switch self {
        case "qrcode":
            return "qrcode.viewfinder"
        case "nfc":
            return "person.crop.square.filled.and.at.rectangle.fill"
        default:
            return "camera.metering.unknown"
        }
    }
    
}

extension String: Identifiable {
    
    public var id: String { self }
    
}
