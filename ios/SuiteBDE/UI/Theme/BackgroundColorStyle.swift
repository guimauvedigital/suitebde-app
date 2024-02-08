//
//  BackgroundColorStyle.swift
//  BDE
//
//  Created by Nathan Fallet on 07/02/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct BackgroundColorStyle: ViewModifier {

    func body(content: Content) -> some View {
        ZStack {
            Color.background.ignoresSafeArea()
            content
        }
    }
}
