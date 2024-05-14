//
//  InjectStateViewModel.swift
//  BDE
//
//  Created by Nathan Fallet on 18/01/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import KMPObservableViewModelCore
import KMPObservableViewModelSwiftUI

@propertyWrapper
struct InjectStateViewModel<ViewModelType: ViewModel>: DynamicProperty {
    
    @StateViewModel private var viewModel: ViewModelType
    
    var wrappedValue: ViewModelType {
        _viewModel.wrappedValue
    }
    
    var projectedValue: ObservableViewModel<ViewModelType>.Projection {
        _viewModel.projectedValue
    }
    
    init() {
        self._viewModel = StateViewModel(wrappedValue: KoinApplication.shared.inject())
    }
    
}
