//
//  DefaultNavigationView.swift
//  BDE
//
//  Created by Nathan Fallet on 08/02/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct DefaultNavigationView<Content: View>: View {
    
    let content: Content
    
    init(@ViewBuilder content: () -> Content) {
        self.content = content()
    }
    
    var body: some View {
        NavigationView {
            DefaultNavigationContainer {
                content
            }
            .navigationBarHidden(true)
        }
        .navigationViewStyle(.stack)
    }
    
}

#Preview {
    DefaultNavigationView {
        Text("Hello world")
            .defaultNavigationTitle("Title")
    }
}
