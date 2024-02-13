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
    
    init(filled: Bool = true, large: Bool = false) {
        self.filled = filled
        self.large = large
    }
    
    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .frame(maxWidth: .infinity)
            .padding(large ? 16 : 12)
            .background(RoundedRectangle(cornerRadius: large ? 16 : 12)
                .strokeBorder(Color.accentColor, lineWidth: 1)
                .background(filled ? Color.accentColor : Color.clear)
                .shadow(radius: 6, x: 0, y: 2)
            )
            .foregroundStyle(filled ? .white : .accentColor)
            .cornerRadius(large ? 16 : 12)
            .scaleEffect(configuration.isPressed ? 0.95 : 1)
    }
    
}
