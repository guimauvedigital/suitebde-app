package com.suitebde.extensions

import software.guimauve.extensions.swiftUI
import software.guimauve.models.apps.GuimauveApp
import software.guimauve.native.SwiftUIGuimauveApp

fun guimauveAppsIos(): List<SwiftUIGuimauveApp> = GuimauveApp.entries.swiftUI
