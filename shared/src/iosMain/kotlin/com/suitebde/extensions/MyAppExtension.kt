package com.suitebde.extensions

import me.nathanfallet.myapps.extensions.ios
import me.nathanfallet.myapps.models.MyApp
import me.nathanfallet.myapps.models.MyAppIos

fun myAppsIos(): List<MyAppIos> = MyApp.entries.ios
