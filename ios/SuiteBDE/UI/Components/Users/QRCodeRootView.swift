//
//  QRCodeRootView.swift
//  BDE
//
//  Created by Nathan Fallet on 03/03/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct QRCodeRootView: View {
    
    var body: some View {
        VStack {
            HStack {
                Text("Suite BDE")
                Spacer()
                Text("BDE X")
            }
            .foregroundStyle(.accent)
            HStack {
                VStack {
                    Text("Name")
                    Text("Nathan")
                }
                Spacer()
                VStack {
                    Text("Valid until")
                    
                }
            }
            Image(uiImage: QRCodeCard.qrcode!) // TODO: User qrcode
                .renderingMode(.template)
                .resizable()
                .interpolation(.none)
                .scaledToFit()
                .frame(maxWidth: 400)
        }
        .padding()
        .frame(maxWidth: .infinity)
        .modifier(CardStyle())
        .padding()
        .defaultNavigationTitle("qrcode_title".localized())
        .defaultNavigationBackButtonHidden(false)
    }
    
}

#Preview {
    DefaultNavigationView {
        QRCodeRootView()
    }
}
