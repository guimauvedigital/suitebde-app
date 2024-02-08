//
//  DefaultNavigationContainer.swift
//  BDE
//
//  Created by Nathan Fallet on 08/02/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct DefaultNavigationContainer<Content: View>: View {
    
    let content: Content
    
    @State var title: LocalizedStringKey = ""
    @State var backButtonHidden: Bool = false
    @State var toolbar: EquatableViewContainer = EquatableViewContainer(view: AnyView(EmptyView()))
    
    init(@ViewBuilder content: () -> Content) {
        self.content = content()
    }
    
    var body: some View {
        VStack(spacing: 0) {
            DefaultNavigationBar(
                title: title,
                backButtonHidden: backButtonHidden,
                toolbar: toolbar.view
            )
            content
                .frame(maxWidth: .infinity, maxHeight: .infinity)
        }
        .modifier(BackgroundColorStyle())
        .onPreferenceChange(DefaultNavigationTitlePreferenceKey.self) { value in
            title = value
        }
        .onPreferenceChange(DefaultNavigationBackButtonHiddenPreferenceKey.self) { value in
            backButtonHidden = value
        }
        .onPreferenceChange(DefaultNavigationToolbarPreferenceKey.self) { value in
            toolbar = value
        }
    }
    
}

#Preview {
    DefaultNavigationContainer {
        Text("Hello world")
            .defaultNavigationTitle("Title")
    }
}
