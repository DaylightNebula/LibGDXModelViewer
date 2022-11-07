package daylightnebula.ktxmodelviewer

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.graphics.Color

fun main() {
    val config = LwjglApplicationConfiguration()
    config.width = 1280
    config.height = 720
    config.title = "LibGDX Model Viewer"
    config.initialBackgroundColor = Color()
    LwjglApplication(ModelViewer(), config)
}