//
//  IntegrationExecutionViewModel.swift
//  BDE
//
//  Created by Nathan Fallet on 04/07/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared

class IntegrationExecutionViewModel: ObservableObject {
    
    let team: IntegrationTeam
    
    @Published var imagePickerShown = false
    @Published var challenges = [IntegrationChallenge]()
    @Published var challenge = ""
    @Published var filename = ""
    @Published var filedata: Data?
    @Published var uploading = false
    @Published var error: String?
    
    init(team: IntegrationTeam) {
        self.team = team
    }
    
    func onAppear(token: String?) {
        AnalyticsService.shared.log(.screenView(screenName: "integration_execution", screenClass: "IntegrationExecutionView"))
        
        fetchChallenges(token: token)
    }
    
    func fetchChallenges(token: String?) {
        guard let token else {
            return
        }
        Task {
            let challenges = try await CacheService.shared.apiService().getIntegrationChallenges(token: token)
            DispatchQueue.main.async {
                self.challenges = challenges
            }
        }
    }
    
    func createExecution(token: String?, completionHandler: @escaping () -> Void) {
        guard let token, let filedata = self.filedata else {
            return
        }
        
        uploading = true
        Task {
            do {
                try await CacheService.shared.apiService().postIntegrationTeamExecution(
                    token: token,
                    id: team.id,
                    challengeId: self.challenge,
                    proof: ByteArrayExtensionKt.toByteArray(filedata),
                    filename: self.filename
                )
                DispatchQueue.main.async {
                    completionHandler()
                }
            } catch {
                DispatchQueue.main.async {
                    self.error = "Vérifiez que vous êtes bien connecté à internet et que ce défi peut encore être complété par vous ou votre équipe."
                    self.uploading = false
                }
            }
        }
    }
    
    func showImagePicker() {
        imagePickerShown = true
    }
    
    func imageSelected(image: UIImage?) {
        guard let image = image else {
            return
        }
        DispatchQueue.main.async {
            self.filename = "\(UUID().uuidString).jpeg"
            self.filedata = image.jpegData(compressionQuality: 0.5)
        }
    }
    
    func videoSelected(data: Data?) {
        guard let data = data else {
            return
        }
        DispatchQueue.main.async {
            self.filename = "\(UUID().uuidString).mov"
            self.filedata = data
        }
    }
    
}
