//
//  CacheService.swift
//  BDE
//
//  Created by Nathan Fallet on 19/05/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import shared

extension CacheService {
    
    static let shared = CacheService(databaseDriverFactory: DatabaseDriverFactory())
    
}
