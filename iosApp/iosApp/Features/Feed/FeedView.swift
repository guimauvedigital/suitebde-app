//
//  FeedView.swift
//  BDE
//
//  Created by Nathan Fallet on 31/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct FeedView: View {
    
    @EnvironmentObject var rootViewModel: RootViewModel
    @StateObject var viewModel = FeedViewModel()
    
    var body: some View {
        NavigationView {
            ScrollView {
                VStack(alignment: .leading) {
                    HStack {
                        Text("Evènements à venir")
                            .font(.title)
                        Spacer()
                    }
                    if viewModel.events.isEmpty {
                        HStack {
                            Spacer()
                            Text("Pas d'évènements à venir")
                            Spacer()
                        }
                        .padding()
                    }
                    LazyVGrid(
                        columns: [GridItem(.adaptive(minimum: 300, maximum: 400))],
                        alignment: .leading
                    ) {
                        ForEach(viewModel.events, id: \.id) { event in
                            NavigationLink(destination: EventView(
                                viewModel: EventViewModel(
                                    event: event,
                                    editable: rootViewModel.user?.hasPermission(permission: "admin.events.edit") ?? false
                                )
                            )) {
                                HStack(spacing: 12) {
                                    Image(systemName: "calendar.circle")
                                        .resizable()
                                        .frame(width: 50, height: 50)
                                    VStack(alignment: .leading) {
                                        Text(event.title ?? "Evènement")
                                            .fontWeight(.bold)
                                        Text(event.renderedDate)
                                    }
                                    Spacer()
                                }
                                .foregroundColor(.primary)
                                .multilineTextAlignment(.leading)
                                .cardView()
                            }
                        }
                    }
                    HStack {
                        Text("Affaires en cours")
                            .font(.title)
                        Spacer()
                    }
                    .padding(.top)
                    if viewModel.topics.isEmpty {
                        HStack {
                            Spacer()
                            Text("Pas d'affaires en cours")
                            Spacer()
                        }
                        .padding()
                    }
                    LazyVGrid(
                        columns: [GridItem(.adaptive(minimum: 300, maximum: 400))],
                        alignment: .leading
                    ) {
                        ForEach(viewModel.topics, id: \.id) { topic in
                            HStack(spacing: 12) {
                                Image(systemName: "building.columns.circle")
                                    .resizable()
                                    .frame(width: 50, height: 50)
                                VStack(alignment: .leading) {
                                    Text(topic.title ?? "Affaire")
                                        .fontWeight(.bold)
                                    Text("Ajoutée le \(topic.createdAt?.renderedDateTime ?? "?")")
                                }
                                Spacer()
                            }
                            .cardView()
                        }
                    }
                    NavigationLink(
                        isActive: $viewModel.isSendNotificationShown,
                        destination: {
                            SendNotificationView()
                        },
                        label: {
                            EmptyView()
                        }
                    )
                    NavigationLink(
                        isActive: $viewModel.isNewEventShown,
                        destination: {
                            EventView(viewModel: EventViewModel(
                                event: nil,
                                editable: true
                            ))
                        },
                        label: {
                            EmptyView()
                        }
                    )
                }
                .padding()
            }
            .navigationTitle(Text("Actualité"))
            .toolbar {
                if rootViewModel.user?.hasPermissions ?? false {
                    Button(action: viewModel.showNewMenu) {
                        Image(systemName: "plus")
                    }
                    .actionSheet(isPresented: $viewModel.isNewMenuShown) {
                        ActionSheet(title: Text("Choisissez une action..."), message: nil, buttons:
                            (rootViewModel.user?.hasPermission(permission: "admin.events.create") ?? false ? [.default(Text("Nouvel évènement"), action: viewModel.showNewEvent)] : []) +
                            (rootViewModel.user?.hasPermission(permission: "admin.notifications") ?? false ? [.default(Text("Envoyer une notification"), action: viewModel.showSendNotification)] : []) +
                            [.cancel()]
                        )
                    }
                }
                NavigationLink(destination: SettingsView()) {
                    Image(systemName: "gearshape")
                }
            }
            .onAppear(perform: viewModel.onAppear)
            .refreshable {
                viewModel.fetchData()
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
