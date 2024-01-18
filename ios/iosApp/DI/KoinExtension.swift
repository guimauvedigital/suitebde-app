//
//  KoinExtension.swift
//  BDE
//
//  Created by Nathan Fallet on 18/01/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import shared

typealias KoinApplication = Koin_coreKoinApplication
typealias Koin = Koin_coreKoin

extension KoinApplication {
    
    static let shared = companion.start()
    
    @discardableResult
    static func start() -> KoinApplication {
        SwiftModule.shared.environment = environment
        return shared
    }
    
    private static var environment: Suitebde_commonsSuiteBDEEnvironment {
        Bundle.main.bundleIdentifier?.hasSuffix(".dev") == true ?
            Suitebde_commonsSuiteBDEEnvironment.development :
            Suitebde_commonsSuiteBDEEnvironment.production
    }
    
    private static let keyPaths: [PartialKeyPath<Koin>] = [
        \.feedViewModel,
         \.usersViewModel
    ]
    
    func inject<T>() -> T {
        for partialKeyPath in Self.keyPaths {
            guard let keyPath = partialKeyPath as? KeyPath<Koin, T> else { continue }
            return koin[keyPath: keyPath]
        }
        fatalError("\(T.self) is not registered with KoinApplication")
    }
    
}
