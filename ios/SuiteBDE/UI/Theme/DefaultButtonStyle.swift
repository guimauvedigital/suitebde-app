//
//  DefaultButtonStyle.swift
//  BDE
//
//  Created by Nathan Fallet on 31/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct DefaultButtonStyle: ButtonStyle {
    
    let filled: Bool
    let large: Bool
    let tint: Color
    let textColor: Color
    let expands: Bool
    
    init(
        filled: Bool = true,
        large: Bool = false,
        tint: Color = .accentColor,
        textColor: Color = .white,
        expands: Bool = true
    ) {
        self.filled = filled
        self.large = large
        self.tint = tint
        self.textColor = textColor
        self.expands = expands
    }
    
    func makeBody(configuration: Configuration) -> some View {
        Group {
            if expands {
                configuration.label
                    .frame(maxWidth: .infinity)
            } else {
                configuration.label
            }
        }
        .padding(large ? 16 : 12)
        .background(RoundedRectangle(cornerRadius: large ? 16 : 12)
            .strokeBorder(tint, lineWidth: 1)
            .background(filled ? tint : Color.clear)
            .shadow(radius: 6, x: 0, y: 2)
        )
        .foregroundStyle(filled ? textColor : tint)
        .cornerRadius(large ? 16 : 12)
        .scaleEffect(configuration.isPressed ? 0.95 : 1)
    }
    
}
