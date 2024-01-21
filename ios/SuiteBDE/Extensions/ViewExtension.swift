//
//  ViewExtension.swift
//  BDE
//
//  Created by Nathan Fallet on 31/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

extension View {
    
    func cardView<S: ShapeStyle>(background: S) -> some View {
        self
            .padding()
            .background(
                RoundedRectangle(cornerRadius: 10)
                    .fill(background)
                    .shadow(radius: 1)
            )
    }
    
    func cardView() -> some View {
        self.cardView(background: Color("CardColor"))
    }
    
    func cardView(applied: Bool) -> some View {
        Group {
            if (applied) {
                self.cardView()
            } else {
                self
            }
        }
    }
    
}
