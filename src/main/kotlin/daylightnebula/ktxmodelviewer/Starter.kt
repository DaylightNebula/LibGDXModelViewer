package daylightnebula.ktxmodelviewer

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.graphics.Color
import daylightnebula.ktxmodelviewer.viewer.ModelViewer

fun main() {
    val viewer = ModelViewer()

    val config = LwjglApplicationConfiguration()
    config.width = 1280
    config.height = 720
    config.title = "LibGDX Model Viewer"
    config.initialBackgroundColor = Color()
    LwjglApplication(viewer, config)
}