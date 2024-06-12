//
//  QRCodeRootView.swift
//  BDE
//
//  Created by Nathan Fallet on 03/03/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct QRCodeRootView: View {
    
    @State var image: UIImage? = nil
    
    let user: Suitebde_commonsUser
    let qrCodeUrl: String
    
    var body: some View {
        VStack(spacing: 16) {
            HStack {
                Text("Suite BDE")
                Spacer()
                Text("BDE X")
            }
            .foregroundStyle(.accent)
            HStack {
                VStack(alignment: .leading) {
                    Text("Name")
                        .foregroundStyle(.secondary)
                    Text("\(user.firstName) \(user.lastName)")
                }
                Spacer()
                VStack(alignment: .trailing) {
                    Text("Valid until")
                        .foregroundStyle(.secondary)
                    Text("???")
                }
            }
            if let image {
                Image(uiImage: image)
                    .renderingMode(.template)
                    .resizable()
                    .interpolation(.none)
                    .scaledToFit()
                    .frame(maxWidth: 400)
            } else {
                ProgressView()
                    .frame(maxWidth: 400, maxHeight: 400)
            }
        }
        .padding()
        .frame(maxWidth: .infinity)
        .modifier(CardStyle())
        .padding()
        .defaultNavigationTitle("qrcode_title".localized())
        .defaultNavigationBackButtonHidden(false)
        .onAppear {
            if image == nil {
                image = qrCodeUrl.generateQRCode()
            }
        }
    }
    
}

#Preview {
    DefaultNavigationView {
        QRCodeRootView(
            user: Suitebde_commonsUser(
                id: "userId",
                associationId: "associationId",
                email: "",
                password: nil,
                firstName: "Nathan",
                lastName: "Fallet",
                superuser: true,
                lastLoginAt: Date().asKotlinxInstant
            ),
            qrCodeUrl: ""
        )
    }
}
