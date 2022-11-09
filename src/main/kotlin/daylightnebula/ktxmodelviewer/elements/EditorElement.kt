package daylightnebula.ktxmodelviewer.elements

import daylightnebula.ktxmodelviewer.ui.ButtonElement
import java.awt.Component
import java.awt.Graphics
import java.awt.Rectangle
import javax.swing.JPanel

abstract class EditorElement {
    val panel = JPanel()
    abstract fun redraw0(g: Graphics)
    abstract fun resize(winWidth: Int, winHeight: Int, gX: Int, gY: Int, gWidth: Int, gHeight: Int)

    fun redraw(g: Graphics) {
        redraw0(g)
        buttons.forEach { it.draw(g) }
    }

    // buttons stuff
    val buttons = mutableListOf<ButtonElement>()
    fun click(xPos: Int, yPos: Int) {
        buttons.forEach { button ->
            if (button.rectangle.intersects(Rectangle(xPos, yPos, 1, 1)))
                button.callback()
        }
    }
}