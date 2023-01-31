//
//  ViewExtension.swift
//  BDE
//
//  Created by Nathan Fallet on 31/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

extension View {
    
    func snapshot() -> UIImage {
        let controller = UIHostingController(rootView: self)
        let view = controller.view

        let targetSize = controller.view.intrinsicContentSize
        view?.bounds = CGRect(origin: .zero, size: targetSize)
        view?.backgroundColor = .clear

        let renderer = UIGraphicsImageRenderer(size: targetSize)

        return renderer.image { _ in
            view?.drawHierarchy(in: controller.view.bounds, afterScreenUpdates: true)
        }
    }
    
    func cardView<S: ShapeStyle>(background: S) -> some View {
        self
            .padding()
            .background(
                RoundedRectangle(cornerRadius: 10)
                    .fill(background)
                    .shadow(radius: 1)
            )
    }
    
    func cardView() -> some View {
        self.cardView(background: Color("CardColor"))
    }
    
}
