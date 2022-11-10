package daylightnebula.ktxmodelviewer

import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import daylightnebula.ktxmodelviewer.elements.EditorElement
import daylightnebula.ktxmodelviewer.elements.ModificationsElement
import daylightnebula.ktxmodelviewer.elements.NavigatorElement
import daylightnebula.ktxmodelviewer.viewer.ModelViewer
import java.awt.Graphics
import java.awt.Rectangle
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import javax.swing.JFrame

class Editor: MouseListener, MouseMotionListener, JFrame() {

    lateinit var lwjglCanvas: LwjglAWTCanvas
    val elements = mutableListOf<EditorElement>()

    init {
        // setup libgdx canvas
        val config = LwjglApplicationConfiguration()
        lwjglCanvas = LwjglAWTCanvas(ModelViewer.INSTANCE, config)
        contentPane.add(lwjglCanvas.canvas)

        // setup nativgator
        elements.add(NavigatorElement())
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

        // setup click listener
        addMouseListener(this)
        addMouseMotionListener(this)

        // update first frame
        updateLWJGLCanvas()
        updateElements()
        repaint()
    }

    fun updateLWJGLCanvas() {
        lwjglCanvas.canvas.setBounds(
            (width * 0.175f).toInt(),
            0,
            (width * 0.825).toInt(),
            (height * 0.65).toInt()
        )
    }

    fun updateElements() {
        // add back all elements and resize them
        Globals.topInset = insets.top
        Globals.leftInset = insets.left
        Globals.bottomInset = insets.bottom
        Globals.rightInset = insets.right
        elements.forEach {
            it.resize(width, height, lwjglCanvas.canvas.x + Globals.leftInset, lwjglCanvas.canvas.y, lwjglCanvas.graphics.width, lwjglCanvas.graphics.height + Globals.topInset)
        }
    }

    override fun paint(g: Graphics) {
        elements.forEach { it.redraw(g) }
    }

    override fun mouseClicked(e: MouseEvent) {
        val point = Rectangle(e.x, e.y, 1, 1)
        elements.forEach {
            if (it.panel.bounds.intersects(point))
                it.click(Globals.xPos, Globals.yPos)
        }
        repaint()
    }

    override fun mouseMoved(e: MouseEvent) {
        Globals.xPos = e.x
        Globals.yPos = e.y
        repaint()
    }

    override fun mouseEntered(e: MouseEvent?) {}
    override fun mouseExited(e: MouseEvent?) {}
    override fun mousePressed(e: MouseEvent?) {}
    override fun mouseReleased(e: MouseEvent?) {}
    override fun mouseDragged(e: MouseEvent?) {}
}