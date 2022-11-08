package daylightnebula.ktxmodelviewer.viewer

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.math.Vector3

class ModelViewerInputProcessor(val camera: PerspectiveCamera): InputProcessor {

    val rotSpeed = 2000f
    val scrollSpeed = 0.1f

    var lastMouseX = 0
    var lastMouseY = 0

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        lastMouseX = screenX
        lastMouseY = screenY
        return true
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        // calculate deltas
        val deltaX = screenX - lastMouseX
        val deltaY = screenY - lastMouseY
        lastMouseX = screenX
        lastMouseY = screenY

        // get change percentages
        val changeX = (deltaX.toFloat() / Gdx.graphics.height) * rotSpeed * Gdx.graphics.deltaTime
        val changeY = (deltaY.toFloat() / Gdx.graphics.height) * rotSpeed * Gdx.graphics.deltaTime

        // rotate cameras location around origin
        camera.rotateAround(Vector3.Zero, Vector3.Y, -changeX)
        camera.rotateAround(Vector3.Zero, Vector3.X, -changeY)
        return true
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        val amount = amountY * scrollSpeed
        camera.translate(camera.position.x * amount, camera.position.y * amount, camera.position.z * amount)
        return true
    }

    override fun keyDown(keycode: Int): Boolean { return true }
    override fun keyUp(keycode: Int): Boolean { return true }
    override fun keyTyped(character: Char): Boolean { return true }
    override fun mouseMoved(screenX: Int, screenY: Int): Boolean { return true }
}