//
//  FeedView.swift
//  BDE
//
//  Created by Nathan Fallet on 31/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import MyAppsiOS
import shared
import KMMViewModelSwiftUI
import KMPNativeCoroutinesAsync

struct FeedView: View {
    
    @InjectStateViewModel private var viewModel: FeedViewModel
    
    @Environment(\.openURL) var openURL
    
    @EnvironmentObject var rootViewModel: RootViewModel
    @StateObject var oldViewModel = OldFeedViewModel()
    
    var body: some View {
        NavigationView {
            ScrollView {
                VStack(alignment: .leading) {
                    if let user = rootViewModel.user {
                        if user.cotisant == nil && !oldViewModel.cotisantConfigurations.isEmpty {
                            HStack {
                                Text("Cotisation")
                                    .font(.title)
                                Spacer()
                            }
                            LazyVGrid(
                                columns: [GridItem(.adaptive(minimum: 300, maximum: 400))],
                                alignment: .leading
                            ) {
                                ForEach(oldViewModel.cotisantConfigurations, id: \.id) { configuration in
                                    ShopCard(
                                        item: configuration,
                                        detailsEnabled: true,
                                        cotisant: false
                                    )
                                }
                            }
                            .padding(.bottom)
                        }
                        if !oldViewModel.ticketConfigurations.isEmpty {
                            HStack {
                                Text("Tickets")
                                    .font(.title)
                                Spacer()
                            }
                            LazyVGrid(
                                columns: [GridItem(.adaptive(minimum: 300, maximum: 400))],
                                alignment: .leading
                            ) {
                                ForEach(oldViewModel.ticketConfigurations, id: \.id) { configuration in
                                    ShopCard(
                                        item: configuration,
                                        detailsEnabled: true,
                                        cotisant: rootViewModel.user?.cotisant != nil
                                    )
                                }
                            }
                        }
                    }
                    if !(viewModel.events ?? []).isEmpty {
                        HStack {
                            Text("feed_events")
                                .font(.title)
                            Spacer()
                        }
                        LazyVGrid(
                            columns: [GridItem(.adaptive(minimum: 300, maximum: 400))],
                            alignment: .leading
                        ) {
                            ForEach(viewModel.events ?? [], id: \.id) { event in
                                NavigationLink(destination: EventView(
                                    viewModel: KoinApplication.shared.koin.eventViewModel(id: event.id)
                                )) {
                                    EventCard(event: event)
                                }
                            }
                        }
                    }
                    NavigationLink(
                        isActive: $oldViewModel.isSendNotificationShown,
                        destination: {
                            SendNotificationView()
                        },
                        label: {
                            EmptyView()
                        }
                    )
                    NavigationLink(
                        isActive: $oldViewModel.isNewEventShown,
                        destination: {
                            EventView(viewModel: EventViewModel(
                                event: nil,
                                editable: false
                            ))
                        },
                        label: {
                            EmptyView()
                        }
                    )
                }
                .padding()
            }
            .navigationTitle(Text("feed_title"))
            .toolbar {
                Button(action: oldViewModel.showNewMenu) {
                    Image(systemName: "plus")
                }
                .actionSheet(isPresented: $oldViewModel.isNewMenuShown) {
                    ActionSheet(title: Text("Choisissez une action..."), message: nil, buttons:
                        [.default(Text("Suggérer un évènement"), action: oldViewModel.showNewEvent)] +
                        (rootViewModel.user?.hasPermission(permission: "admin.notifications") ?? false ? [.default(Text("Envoyer une notification"), action: oldViewModel.showSendNotification)] : []) +
                        [.cancel()]
                    )
                }
                NavigationLink(destination: SettingsView()) {
                    Image(systemName: "gearshape")
                }
            }
            .onAppear(perform: oldViewModel.onAppear)
            .onAppear(perform: rootViewModel.fetchData)
            .refreshable {
                oldViewModel.fetchData()
                rootViewModel.fetchData()
            }
            .onAppear {
                Task {
                    try await asyncFunction(for: viewModel.onAppear())
                }
            }
            .refreshable {
                Task {
                    try await asyncFunction(for: viewModel.onAppear())
                }
            }
        }
        .navigationViewStyle(StackNavigationViewStyle())
    }
    
}

struct FeedView_Previews: PreviewProvider {
    
    static var previews: some View {
        FeedView()
    }
    
}
