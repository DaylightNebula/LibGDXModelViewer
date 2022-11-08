package daylightnebula.ktxmodelviewer.elements

import daylightnebula.ktxmodelviewer.Colors
import java.awt.Component
import java.awt.Graphics
import javax.swing.JPanel

class NavigatorElement: EditorElement() {
    override fun redraw(g: Graphics) {
        g.color = Colors.middleBG
        g.fillRect(panel.x, panel.y, panel.width, panel.height)
    }

    override fun resize(winWidth: Int, winHeight: Int, gX: Int, gY: Int, gWidth: Int, gHeight: Int) {
        panel.setBounds(
            0, 0,
            gX, gHeight
        )
    }
}