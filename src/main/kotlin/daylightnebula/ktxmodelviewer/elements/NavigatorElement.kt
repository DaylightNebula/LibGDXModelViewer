package daylightnebula.ktxmodelviewer.elements

import daylightnebula.ktxmodelviewer.Colors
import daylightnebula.ktxmodelviewer.ui.ButtonElement
import java.awt.Color
import java.awt.Component
import java.awt.Graphics
import javax.swing.JPanel

class NavigatorElement: EditorElement() {

    val newCallback = { println("New clicked") }
    val importCallback = { println("Import clicked") }
    val openCallback = { println("Open clicked") }
    val saveCallback = { println("Save clicked") }

    val newButton = ButtonElement(this,
        "New", 0f, 0f, 0.25f, 0.085f,
        Colors.middleBG, Colors.forwardBG, 0, newCallback
    )
    val importButton = ButtonElement(this,
        "Import", 0.25f, 0f, 0.25f, 0.085f,
        Colors.middleBG, Colors.forwardBG, 0, importCallback
    )
    val openButton = ButtonElement(this,
        "Open", 0.5f, 0f, 0.25f, 0.085f,
        Colors.middleBG, Colors.forwardBG, 0, openCallback
    )
    val saveButton = ButtonElement(this,
        "Save", 0.75f, 0f, 0.25f, 0.085f,
        Colors.middleBG, Colors.forwardBG, 0, saveCallback
    )

    init {
        buttons.add(newButton)
        buttons.add(importButton)
        buttons.add(openButton)
        buttons.add(saveButton)
    }

    override fun redraw0(g: Graphics) {
        // draw background
        g.color = Colors.middleBG
        g.fillRect(panel.x, panel.y, panel.width, panel.height)

        // draw line separating top bar from the rest
        g.color = Colors.forwardBG
        g.fillRect(0, newButton.rectangle.y + newButton.rectangle.height, panel.width, 2)
    }

    override fun resize(winWidth: Int, winHeight: Int, gX: Int, gY: Int, gWidth: Int, gHeight: Int) {
        panel.setBounds(
            0, 0,
            gX, gHeight
        )
    }
}