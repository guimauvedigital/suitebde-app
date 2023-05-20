//
//  CalendarEventView.swift
//  BDE
//
//  Created by Nathan Fallet on 19/05/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct CalendarEventView: View {
    
    let title: String
    let description: String
    let start: Date
    let end: Date
    let background: Color
    
    let height: CGFloat
    let yOffset: CGFloat
    
    init(event: CalendarEvent, day: Date) {
        title = event.title ?? "Evènement"
        description = event.content ?? ""
        start = event.start?.asDate ?? Date()
        end = event.end?.asDate ?? Date()
        background = event.type == CalendarEventCalendarEventType.userCourse ? Color.blue : Color.yellow
        
        let startDayComponents = Calendar.current.dateComponents([.day, .month, .year], from: event.start?.asDate ?? Date())
        let endDayComponents = Calendar.current.dateComponents([.day, .month, .year], from: event.end?.asDate ?? Date())
        let startComponents = Calendar.current.dateComponents([.hour, .minute], from: start)
        let endComponents = Calendar.current.dateComponents([.hour, .minute], from: end)
        
        if startDayComponents == endDayComponents {
            // Starts and ends the same day
            let diffComponents = Calendar.current.dateComponents([.hour, .minute], from: start, to: end)
            let duration = (diffComponents.hour ?? 0) * 60 + (diffComponents.minute ?? 0)
            height = 44 * max(CGFloat(duration), 60) / 60
            
            let time = (startComponents.hour ?? 0) * 60 + (startComponents.minute ?? 0)
            yOffset = 44 * CGFloat(time) / 60
        } else {
            // Ends on a different day
            let currentDayComponents = Calendar.current.dateComponents([.day, .month, .year], from: day)
            if startDayComponents == currentDayComponents {
                // Starts today
                let time = (startComponents.hour ?? 0) * 60 + (startComponents.minute ?? 0)
                yOffset = 44 * CGFloat(time) / 60
                height = 44 * 24 - yOffset
            } else if endDayComponents == currentDayComponents {
                // Ends today
                let time = (endComponents.hour ?? 0) * 60 + (endComponents.minute ?? 0)
                yOffset = 0
                height = 44 * CGFloat(time) / 60
            } else {
                // Is all the day
                yOffset = 0
                height = 44 * 24
            }
        }
    }
    
    var body: some View {
        VStack(alignment: .leading) {
            Text(title)
                .font(.footnote)
                .fontWeight(.bold)
            Text(description)
                .font(.footnote)
        }
        .padding(8)
        .frame(maxWidth: .infinity, alignment: .leading)
        .frame(height: height - 2, alignment: .top)
        .background(background.opacity(0.5))
        .cornerRadius(8)
        .padding()
        .padding(.leading, 36)
        .offset(x: 0, y: 11 + yOffset)
    }
    
}
