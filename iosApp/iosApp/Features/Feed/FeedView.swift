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
                    if rootViewModel.integrationConfiguration?.enabled ?? false {
                        NavigationLink(destination: IntegrationTeamsView()) {
                            HStack(spacing: 12) {
                                VStack(alignment: .leading, spacing: 12) {
                                    Text("La chasse est ouverte aux équipes !")
                                        .font(.title2)
                                        .foregroundColor(.white)
                                        .multilineTextAlignment(.leading)
                                    Text("C'est parti")
                                        .padding(.horizontal)
                                        .padding(.vertical, 8)
                                        .foregroundColor(.accentColor)
                                        .background(Color(.white))
                                        .cornerRadius(10)
                                }
                                Spacer()
                                Image("Integration")
                                    .resizable()
                                    .scaledToFit()
                                    .frame(width: 100, height: 130)
                            }
                            .cardView(background: LinearGradient(
                                colors: [.accentColor, Color("SecondaryColor")],
                                startPoint: .topLeading,
                                endPoint: .bottomTrailing
                            ))
                            .padding(.bottom)
                        }
                    }
                    if let user = rootViewModel.user {
                        if user.cotisant == nil && !viewModel.cotisantConfigurations.isEmpty {
                            HStack {
                                Text("Cotisation")
                                    .font(.title)
                                Spacer()
                            }
                            LazyVGrid(
                                columns: [GridItem(.adaptive(minimum: 300, maximum: 400))],
                                alignment: .leading
                            ) {
                                ForEach(viewModel.cotisantConfigurations, id: \.id) { configuration in
                                    ShopCard(
                                        item: configuration,
                                        detailsEnabled: true,
                                        cotisant: false
                                    )
                                }
                            }
                            .padding(.bottom)
                        }
                        if !viewModel.ticketConfigurations.isEmpty {
                            HStack {
                                Text("Tickets")
                                    .font(.title)
                                Spacer()
                            }
                            LazyVGrid(
                                columns: [GridItem(.adaptive(minimum: 300, maximum: 400))],
                                alignment: .leading
                            ) {
                                ForEach(viewModel.ticketConfigurations, id: \.id) { configuration in
                                    ShopCard(
                                        item: configuration,
                                        detailsEnabled: true,
                                        cotisant: rootViewModel.user?.cotisant != nil
                                    )
                                }
                            }
                        }
                    }
                    if !viewModel.events.isEmpty {
                        HStack {
                            Text("Evènements à venir")
                                .font(.title)
                            Spacer()
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
                    }
                    if !viewModel.topics.isEmpty {
                        HStack {
                            Text("Affaires en cours")
                                .font(.title)
                            Spacer()
                        }
                        .padding(.top)
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
            .navigationTitle(Text("Actualité"))
            .toolbar {
                Button(action: viewModel.showNewMenu) {
                    Image(systemName: "plus")
                }
                .actionSheet(isPresented: $viewModel.isNewMenuShown) {
                    ActionSheet(title: Text("Choisissez une action..."), message: nil, buttons:
                        [.default(Text("Suggérer un évènement"), action: viewModel.showNewEvent)] +
                        (rootViewModel.user?.hasPermission(permission: "admin.notifications") ?? false ? [.default(Text("Envoyer une notification"), action: viewModel.showSendNotification)] : []) +
                        [.cancel()]
                    )
                }
                NavigationLink(destination: SettingsView()) {
                    Image(systemName: "gearshape")
                }
            }
            .onAppear(perform: viewModel.onAppear)
            .onAppear(perform: rootViewModel.fetchData)
            .refreshable {
                viewModel.fetchData()
                rootViewModel.fetchData()
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
