//
//  QRCodeCard.swift
//  BDE
//
//  Created by Nathan Fallet on 03/03/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import GuimauveUI

struct QRCodeCard: View {
    
    static let qrcode = "https://suitebde.com".generateQRCode()
    
    var body: some View {
        HStack(spacing: 0) {
            Image(uiImage: QRCodeCard.qrcode!)
                .renderingMode(.template)
                .resizable()
                .interpolation(.none)
                .padding()
                .frame(width: 76, height: 76)
                .clipped()
                Text("qrcode_view")
                    .lineLimit(1)
            Spacer()
            Image(systemName: "chevron.right")
                .foregroundStyle(.secondary)
                .padding()
        }
        .foregroundColor(.primary)
        .multilineTextAlignment(.leading)
        .modifier(CardStyle())
    }
    
}

#Preview {
    QRCodeCard()
}
