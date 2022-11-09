package daylightnebula.ktxmodelviewer.elements

import daylightnebula.ktxmodelviewer.Colors
import java.awt.Graphics
import javax.swing.JPanel

class ModificationsElement: EditorElement() {
    override fun redraw0(g: Graphics) {
        g.color = Colors.middleBG
        g.fillRect(panel.x, panel.y, panel.width, panel.height)
        g.color = Colors.forwardBG
        g.fillRect(panel.x + 2, panel.y, panel.width, 2)
    }

    override fun resize(winWidth: Int, winHeight: Int, gX: Int, gY: Int, gWidth: Int, gHeight: Int) {
        panel.setBounds(
            0, gY + gHeight,
            winWidth, winHeight - (gY + gHeight)
        )
    }
}