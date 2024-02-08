//
//  DefaultNavigationBar.swift
//  BDE
//
//  Created by Nathan Fallet on 08/02/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct DefaultNavigationBar<Toolbar: View>: View {
    
    @Environment(\.presentationMode) var presentationMode
    
    let title: LocalizedStringKey
    let backButtonHidden: Bool
    let toolbar: Toolbar
    
    var body: some View {
        ZStack {
            if !backButtonHidden {
                HStack {
                    Button(action: {
                        presentationMode.wrappedValue.dismiss()
                    }) {
                        Image(systemName: "chevron.left")
                            .font(.title2)
                    }
                    Spacer()
                }
            }
            Text(title)
                .font(.title2)
                .fontWeight(.semibold)
            HStack(spacing: 24) {
                Spacer()
                toolbar
                    .font(.title2)
            }
        }
        .padding()
    }
    
}

#Preview {
    DefaultNavigationBar(
        title: "Title",
        backButtonHidden: false,
        toolbar: Group {
            Image(systemName: "gearshape")
            Image(systemName: "gearshape")
        }
    )
}
