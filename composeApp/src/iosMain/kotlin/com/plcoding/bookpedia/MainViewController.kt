package com.plcoding.bookpedia

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import io.ktor.client.engine.darwin.Darwin

fun MainViewController() = ComposeUIViewController {
    App(
        engine = remember {
            Darwin.create()
        }
    )
}