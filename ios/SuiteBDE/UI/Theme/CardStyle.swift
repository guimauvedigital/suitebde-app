//
//  CardStyle.swift
//  BDE
//
//  Created by Nathan Fallet on 08/02/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct CardStyle: ViewModifier {

    func body(content: Content) -> some View {
        content
            .background(Color.card)
            .cornerRadius(12)
            .shadow(
                color: Color(.sRGBLinear, white: 0, opacity: 0.15),
                radius: 6, x: 0, y: 2
            )
    }
}
