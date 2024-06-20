//
//  ScanHistoryRootView.swift
//  BDE
//
//  Created by Nathan Fallet on 21/05/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct ScanHistoryRootView: View {
    
    let scans: [CommonsScansForDay]
    
    let loadMore: (Kotlinx_datetimeLocalDate) -> Void
    
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 16) {
                ForEach(scans, id: \.date) { day in
                    let title = day.date.renderedDate
                    let count = Set(day.scans.map { $0.userId }).count
                    
                    Text("\(title) - \(count) personne(s)")
                        .font(.title2)
                        .onAppear {
                            loadMore(day.date)
                        }
                    LazyVGrid(
                        columns: [GridItem(.adaptive(minimum: 300, maximum: 400))],
                        alignment: .leading,
                        spacing: 16
                    ) {
                        ForEach(day.scans, id: \.id) { entry in
                            if let user = entry.user {
                                DefaultNavigationLink(destination: UserView(viewModel: KoinApplication.shared.koin.userViewModel(
                                    associationId: entry.associationId, userId: entry.userId
                                ))) {
                                    UserCard(
                                        user: user,
                                        customDescription: entry.scannedAt.renderedDateTime
                                    )
                                }
                            }
                        }
                    }
                }
            }
            .padding()
        }
        .defaultNavigationTitle("scan_history_title".localized())
        .defaultNavigationBackButtonHidden(false)
    }
    
}

#Preview {
    DefaultNavigationView {
        ScanHistoryRootView(
            scans: [
                CommonsScansForDay(
                    date: Kotlinx_datetimeLocalDate(year: 2024, month: Kotlinx_datetimeMonth.may, dayOfMonth: 21),
                    scans: [
                        CommonsScan(
                            id: CoreUUID(),
                            associationId: CoreUUID(),
                            userId: CoreUUID(),
                            scannerId: CoreUUID(),
                            scannedAt: Date().asKotlinxInstant,
                            user: CommonsUser(
                                id: CoreUUID(),
                                associationId: CoreUUID(),
                                email: "",
                                password: nil,
                                firstName: "Nathan",
                                lastName: "Fallet",
                                superuser: true,
                                lastLoginAt: Date().asKotlinxInstant
                            ),
                            scanner: nil
                        )
                    ],
                    events: []
                )
            ],
            loadMore: { _ in }
        )
    }
}
