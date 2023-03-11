//
//  EventView.swift
//  BDE
//
//  Created by Nathan Fallet on 10/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct EventView: View {
    
    @EnvironmentObject var rootViewModel: RootViewModel
    @StateObject var viewModel: EventViewModel
    
    var body: some View {
        Form {
            Section(header: Text("Informations")) {
                Text(viewModel.event.title ?? "Evènement")
                    .fontWeight(.bold)
                Text(viewModel.event.renderedDate)
            }
            if let content = viewModel.event.content, !content.isEmpty {
                Section {
                    Text(content)
                }
            }
        }
        .onAppear(perform: viewModel.onAppear)
        .navigationTitle(Text("Evènement"))
    }
    
}
