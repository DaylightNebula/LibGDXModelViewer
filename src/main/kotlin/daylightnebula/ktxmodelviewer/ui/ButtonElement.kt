package daylightnebula.ktxmodelviewer.ui

import daylightnebula.ktxmodelviewer.Globals
import daylightnebula.ktxmodelviewer.elements.EditorElement
import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import java.awt.Rectangle

class ButtonElement(
    val element: EditorElement,
    val text: String,
    val xPercent: Float, val yPercent: Float,
    val widthPercent: Float, val heightPercent: Float,
    val color: Color, val hoverColor: Color, val borderRadius: Int,
    val callback: () -> Unit
) {

    var rectangle: Rectangle = Rectangle(
        element.panel.x + (xPercent * element.panel.width).toInt(),
        element.panel.y + (yPercent * element.panel.height).toInt(),
        (element.panel.width * widthPercent).toInt(),
        (element.panel.height * heightPercent).toInt()
    )

    fun draw(g: Graphics) {
        // precalculate some values for element placement
        val textWidth = g.fontMetrics.stringWidth(text)

        // recalculate rectangle
        val container = element.panel.bounds
        rectangle = Rectangle(
            container.x + (xPercent * container.width).toInt(),
            container.y + (yPercent * container.height).toInt(),
            (container.width * widthPercent).toInt(),
            (container.height * heightPercent).toInt()
        )

        // set background color based on hover
        g.color = if (rectangle.intersects(Rectangle(Globals.xPos, Globals.yPos, 1, 1))) hoverColor else color

        // draw background
        g.fillRoundRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height, borderRadius, borderRadius)

        // draw text
        g.color = Color(1f, 1f, 1f, 1f)
        g.drawString(text, rectangle.x + (rectangle.width / 2) - Globals.leftInset - (textWidth / 4), rectangle.y + Globals.topInset + (rectangle.height / 2) - (g.font.size))
    }
}