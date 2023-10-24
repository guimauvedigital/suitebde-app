//
//  QRCodeWidget.swift
//  QRCodeWidget
//
//  Created by Nathan Fallet on 06/02/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import WidgetKit
import SwiftUI

struct Provider: TimelineProvider {
    
    private func getUserUrl() -> String? {
        if let user = StorageService.userDefaults?.object(forKey: "user") as? String,
           let data = user.data(using: .utf8),
           let json = try? JSONSerialization.jsonObject(with: data) as? [String: Any],
           let id = json["id"] as? String {
            return "bdeensisa://users/\(id)"
        } else {
            return nil
        }
    }
    
    func placeholder(in context: Context) -> SimpleEntry {
        SimpleEntry(qrCode: getUserUrl()?.generateQRCode(), date: Date())
    }

    func getSnapshot(in context: Context, completion: @escaping (SimpleEntry) -> ()) {
        let entry = SimpleEntry(qrCode: getUserUrl()?.generateQRCode(), date: Date())
        completion(entry)
    }

    func getTimeline(in context: Context, completion: @escaping (Timeline<Entry>) -> ()) {
        let timeline = Timeline(entries: [
            SimpleEntry(qrCode: getUserUrl()?.generateQRCode(), date: Date())
        ], policy: .atEnd)
        completion(timeline)
    }
    
}

struct SimpleEntry: TimelineEntry {
    
    let qrCode: UIImage?
    let date: Date
    
}

struct QRCodeWidgetEntryView : View {
    
    var entry: Provider.Entry

    var body: some View {
        if let qrCode = entry.qrCode {
            Image(uiImage: qrCode)
                .renderingMode(.template)
                .resizable()
                .interpolation(.none)
                .scaledToFit()
        } else {
            Text("Veuillez vous connectez à votre compte pour obtenir votre QR Code.")
                .multilineTextAlignment(.center)
        }
    }
    
}

struct QRCodeWidget: Widget {
    
    let kind: String = "QRCodeWidget"

    var body: some WidgetConfiguration {
        StaticConfiguration(kind: kind, provider: Provider()) { entry in
            if #available(iOSApplicationExtension 17, *) {
                QRCodeWidgetEntryView(entry: entry)
                    .containerBackground(.background, for: .widget)
            } else {
                QRCodeWidgetEntryView(entry: entry)
                    .padding()
            }
        }
        .supportedFamilies([.systemSmall])
        .configurationDisplayName("Mon QR Code BDE")
        .description("Affiche le QR Code du BDE")
    }
    
}
