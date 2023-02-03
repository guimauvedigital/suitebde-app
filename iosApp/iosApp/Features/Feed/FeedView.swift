//
//  FeedView.swift
//  BDE
//
//  Created by Nathan Fallet on 31/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct FeedView: View {
    
    @StateObject var viewModel = FeedViewModel()
    
    var body: some View {
        NavigationView {
            ScrollView {
                VStack(alignment: .leading) {
                    HStack {
                        Text("Evènements")
                            .font(.title)
                        Spacer()
                    }
                    LazyVGrid(
                        columns: [GridItem(.adaptive(minimum: 300, maximum: 400))],
                        alignment: .leading
                    ) {
                        ForEach(viewModel.events, id: \.id) { event in
                            HStack(spacing: 12) {
                                Image(systemName: "calendar.circle")
                                    .resizable()
                                    .frame(width: 50, height: 50)
                                VStack(alignment: .leading) {
                                    Text(event.title ?? "Evènement")
                                        .fontWeight(.bold)
                                    Text("Du \(event.start?.rendered ?? "?")")
                                    Text("Au \(event.end?.rendered ?? "?")")
                                }
                                Spacer()
                            }
                            .cardView()
                        }
                    }
                }
                .padding()
            }
            .navigationTitle(Text("Actualité"))
            .onAppear(perform: viewModel.onAppear)
        }
    }
    
}

struct FeedView_Previews: PreviewProvider {
    
    static var previews: some View {
        FeedView()
    }
    
}
