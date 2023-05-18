//
//  AlertCase.swift
//  BDE
//
//  Created by Nathan Fallet on 18/05/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

enum AlertCase: String, Identifiable {
    
    var id: String {
        rawValue
    }
    
    case saved, cancelling
    
}

func constructAlertCase(discardEdit: @escaping () -> Void = {}) -> ((AlertCase) -> Alert) {
    return { item in
        switch item {
        case .cancelling:
            return Alert(
                title: Text("Les modifications n'ont pas été enregistrées"),
                message: Text("Etes vous sûr de vouloir quitter ?"),
                primaryButton: .cancel(Text("Quitter sans enregistrer"), action: discardEdit),
                secondaryButton: .default(Text("Revenir sur l'édition"))
            )
        case .saved:
            return Alert(
                title: Text("Les modifications ont bien été enregistrées")
            )
        }
    }
}
