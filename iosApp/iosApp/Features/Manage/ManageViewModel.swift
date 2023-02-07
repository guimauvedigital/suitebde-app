//
//  ManageViewModel.swift
//  BDE
//
//  Created by Nathan Fallet on 31/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation

class ManageViewModel: ObservableObject {
    
    func onAppear() {
        AnalyticsService.shared.log(.screenView(screenName: "manage", screenClass: "ManageView"))
    }
    
}
