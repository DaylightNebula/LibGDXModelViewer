package daylightnebula.ktxmodelviewer

import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import daylightnebula.ktxmodelviewer.elements.EditorElement
import daylightnebula.ktxmodelviewer.elements.InspectorElement
import daylightnebula.ktxmodelviewer.elements.ModificationsElement
import daylightnebula.ktxmodelviewer.elements.NavigatorElement
import daylightnebula.ktxmodelviewer.viewer.ModelViewer
import java.awt.Graphics
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import javax.swing.JFrame


class Editor: JFrame() {

    lateinit var lwjglCanvas: LwjglAWTCanvas
    val elements = mutableListOf<EditorElement>()

    init {
        // setup libgdx canvas
        val config = LwjglApplicationConfiguration()
        lwjglCanvas = LwjglAWTCanvas(ModelViewer(), config)
        contentPane.add(lwjglCanvas.canvas)

        // setup nativgator
        elements.add(NavigatorElement())
        elements.add(InspectorElement())
        elements.add(ModificationsElement())

        // setup resize call
        addComponentListener(object : ComponentAdapter() {
            override fun componentResized(componentEvent: ComponentEvent) {
                updateLWJGLCanvas()
                updateElements()
            }
        })

        // setup window
        defaultCloseOperation = EXIT_ON_CLOSE
        pack()
        isVisible = true
        title = "Scythe Model Viewer"
        setSize(1600, 900)

        // update first frame
        updateLWJGLCanvas()
        updateElements()
        repaint()
    }

    fun updateLWJGLCanvas() {
        lwjglCanvas.canvas.setBounds(
            (width * 0.175f).toInt(),
            0,
            (width * 0.65).toInt(),
            (height * 0.65).toInt()
        )
    }

    fun updateElements() {
        // add back all elements and resize them
        val topInset = insets.top
        val leftInset = insets.left
        elements.forEach {
            it.resize(width, height, lwjglCanvas.canvas.x + leftInset, lwjglCanvas.canvas.y, lwjglCanvas.graphics.width, lwjglCanvas.graphics.height + topInset)
        }
    }

    override fun paint(g: Graphics) {
        elements.forEach { it.redraw(g) }
    }
}