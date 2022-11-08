package daylightnebula.ktxmodelviewer.elements

import java.awt.Component
import java.awt.Graphics
import javax.swing.JPanel

abstract class EditorElement {
    val panel = JPanel()
    abstract fun redraw(g: Graphics)
    abstract fun resize(winWidth: Int, winHeight: Int, gX: Int, gY: Int, gWidth: Int, gHeight: Int)
}