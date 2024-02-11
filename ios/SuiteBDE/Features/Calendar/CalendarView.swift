//
//  CalendarView.swift
//  BDE
//
//  Created by Nathan Fallet on 19/05/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct CalendarView: View {
    
    @EnvironmentObject var rootViewModel: OldRootViewModel
    @StateObject var viewModel = CalendarViewModel()
    
    var body: some View {
        NavigationView {
            ScrollViewReader { value in
                ScrollView {
                    ZStack(alignment: .topLeading) {
                        VStack(spacing: 24) {
                            ForEach(0 ..< 24) { hour in
                                HStack(spacing: 8) {
                                    Text("\(hour)h")
                                        .font(.system(size: 12))
                                        .frame(width: 28, height: 20, alignment: .center)
                                        .id(hour)
                                    VStack {
                                        Divider()
                                    }
                                }
                            }
                            VStack {
                                Spacer()
                            }
                        }
                        .padding()
                        ForEach(viewModel.todayEvents, id: \.id) { event in
                            CalendarEventView(
                                event: event,
                                day: viewModel.day
                            )
                        }
                    }
                }
                .onAppear {
                    viewModel.day = Date()
                }
                .onChange(of: viewModel.day) { _ in
                    value.scrollTo(7, anchor: .top)
                }
            }
            .navigationTitle(Text(viewModel.day.asKotlinxInstant.renderedDate))
            .toolbar {
                ToolbarItem(placement: .navigationBarLeading) {
                    Button(action: viewModel.previous) {
                        Image(systemName: "arrow.left")
                    }
                    .disabled(viewModel.day < .tomorrow)
                }
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button(action: viewModel.next) {
                        Image(systemName: "arrow.right")
                    }
                }
            }
            .onAppear {
                viewModel.onAppear(token: rootViewModel.token)
            }
            .refreshable {
                viewModel.fetchData(token: rootViewModel.token, reload: true)
            }
        }
        .navigationViewStyle(StackNavigationViewStyle())
    }
    
}
