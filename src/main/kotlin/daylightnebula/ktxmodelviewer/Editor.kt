package daylightnebula.ktxmodelviewer

import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import daylightnebula.ktxmodelviewer.elements.EditorElement
import daylightnebula.ktxmodelviewer.elements.InspectorElement
import daylightnebula.ktxmodelviewer.elements.NavigatorElement
import daylightnebula.ktxmodelviewer.ui.TextInputManager
import daylightnebula.ktxmodelviewer.viewer.ModelViewer
import java.awt.Graphics
import java.awt.Rectangle
import java.awt.event.*
import javax.swing.JFrame

class Editor: MouseListener, MouseMotionListener, KeyListener, JFrame() {

    lateinit var lwjglCanvas: LwjglAWTCanvas
    val elements = mutableListOf<EditorElement>()

    init {
        // setup libgdx canvas
        val config = LwjglApplicationConfiguration()
        lwjglCanvas = LwjglAWTCanvas(ModelViewer.INSTANCE, config)
        contentPane.add(lwjglCanvas.canvas)

        // setup nativgator
        elements.add(NavigatorElement())
        elements.add(InspectorElement())

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
        addKeyListener(this)

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
        Globals.mouseButtonDown = -1
    }

    override fun mouseClicked(e: MouseEvent) {
        // pass mouse click to elements
        val point = Rectangle(e.x, e.y, 1, 1)
        elements.forEach {
            if (it.panel.bounds.intersects(point))
                it.click(Globals.xPos, Globals.yPos)
        }

        // set mouse button and deselect current text input tag
        Globals.mouseButtonDown = e.button
        TextInputManager.deselectTag()

        // repaint frame
        repaint()
    }

    override fun mouseMoved(e: MouseEvent) {
        Globals.xPos = e.x
        Globals.yPos = e.y
        repaint()
    }

    override fun keyReleased(e: KeyEvent) {
        TextInputManager.manageKey(e.keyChar)
        repaint()
    }

    override fun mouseEntered(e: MouseEvent?) {}
    override fun mouseExited(e: MouseEvent?) {}
    override fun mousePressed(e: MouseEvent?) {}
    override fun mouseReleased(e: MouseEvent?) {}
    override fun mouseDragged(e: MouseEvent?) {}
    override fun keyTyped(e: KeyEvent?) {}
    override fun keyPressed(e: KeyEvent?) {}
}