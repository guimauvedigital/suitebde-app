//
//  DefaultNavigationPreferenceKeys.swift
//  BDE
//
//  Created by Nathan Fallet on 08/02/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct DefaultNavigationTitlePreferenceKey: PreferenceKey {
    
    static var defaultValue: LocalizedStringKey = ""
    
    static func reduce(value: inout LocalizedStringKey, nextValue: () -> LocalizedStringKey) {
        value = nextValue()
    }
    
}

struct DefaultNavigationBackButtonHiddenPreferenceKey: PreferenceKey {
    
    static var defaultValue: Bool = false
    
    static func reduce(value: inout Bool, nextValue: () -> Bool) {
        value = nextValue()
    }
    
}

struct DefaultNavigationToolbarPreferenceKey: PreferenceKey {
    
    static var defaultValue: EquatableViewContainer = EquatableViewContainer(view: AnyView(EmptyView()) )
    
    static func reduce(value: inout EquatableViewContainer, nextValue: () -> EquatableViewContainer) {
        value = nextValue()
    }
    
}

struct EquatableViewContainer: Equatable {
    
    let id = UUID().uuidString
    let view:AnyView
    
    static func == (lhs: EquatableViewContainer, rhs: EquatableViewContainer) -> Bool {
        return lhs.id == rhs.id
    }
    
}

extension View {
    
    func defaultNavigationTitle(_ title: LocalizedStringKey) -> some View {
        preference(key: DefaultNavigationTitlePreferenceKey.self, value: title)
    }
    
    func defaultNavigationBackButtonHidden(_ hidden: Bool) -> some View {
        preference(key: DefaultNavigationBackButtonHiddenPreferenceKey.self, value: hidden)
    }
    
    func defaultNavigationToolbar<Content: View>(@ViewBuilder content: () -> Content) -> some View {
        preference(key: DefaultNavigationToolbarPreferenceKey.self, value: EquatableViewContainer(view: AnyView(content())))
    }
    
}

extension UINavigationController: UIGestureRecognizerDelegate {
    
    override open func viewDidLoad() {
        super.viewDidLoad()
        interactivePopGestureRecognizer?.delegate = self
    }

    public func gestureRecognizerShouldBegin(_ gestureRecognizer: UIGestureRecognizer) -> Bool {
        return viewControllers.count > 1
    }
    
}
