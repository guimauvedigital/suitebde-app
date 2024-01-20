//
//  AlertCase.swift
//  BDE
//
//  Created by Nathan Fallet on 18/05/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

extension AlertCase: Identifiable {
    
    public var id: String { name }
    
}

func constructAlertCase(
    discardEdit: @escaping () -> Void,
    deleteAccount: @escaping () -> Void = {}
) -> ((AlertCase) -> Alert) {
    return { item in
        switch item {
        case .cancelling:
            return Alert(
                title: Text("Les modifications n'ont pas été enregistrées"),
                message: Text("Etes vous sûr de vouloir quitter ?"),
                primaryButton: .cancel(Text("Quitter sans enregistrer"), action: discardEdit),
                secondaryButton: .default(Text("Revenir sur l'édition"))
            )
        case .deleting:
            return Alert(
                title: Text("Es tu sûr de vouloir supprimer ton compte ?"),
                message: Text("Cette action est irréversible et tu perdras toutes tes données."),
                primaryButton: .cancel(Text("Annuler")),
                secondaryButton: .destructive(Text("Supprimer mon compte"), action: deleteAccount)
            )
        case .saved:
            return Alert(
                title: Text("Les modifications ont bien été enregistrées")
            )
        default:
            return Alert(
                title: Text("Error")
            )
        }
    }
}
