package com.plcoding.bookpedia

import androidx.compose.ui.window.ComposeUIViewController
import com.plcoding.bookpedia.app.App
import com.plcoding.bookpedia.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initKoin() }
) {
    App()
}