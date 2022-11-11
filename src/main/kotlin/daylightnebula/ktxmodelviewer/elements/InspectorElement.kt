package daylightnebula.ktxmodelviewer.elements

import daylightnebula.ktxmodelviewer.Colors
import daylightnebula.ktxmodelviewer.Globals
import daylightnebula.ktxmodelviewer.ui.TextInputManager
import daylightnebula.ktxmodelviewer.viewer.ModelViewer
import org.w3c.dom.Text
import java.awt.Color
import java.awt.Graphics
import java.awt.Rectangle

class InspectorElement: EditorElement() {

    val mode = InspectorMode.OBJECT

    val objectModifier = ObjectModifier(this)

    override fun redraw0(g: Graphics) {
        // draw background
        g.color = Colors.middleBG
        g.fillRect(panel.x, panel.y, panel.width, panel.height)

        // draw borders
        g.color = Colors.forwardBG
        g.fillRect(panel.x + 2, panel.y, panel.width, 2)
        g.fillRect(panel.x, panel.y, 2, panel.height)

        // stop if we don't have a model
        if (ModelViewer.INSTANCE.modelData == null)
            return

        // draw mode
        when (mode) {
            InspectorMode.OBJECT -> { objectModifier.redraw(g) }
            else -> { println("ERROR cannot render mode $mode") }
        }
    }

    override fun resize(winWidth: Int, winHeight: Int, gX: Int, gY: Int, gWidth: Int, gHeight: Int) {
        panel.setBounds(
            gX, gY + gHeight,
            winWidth - gX, winHeight - (gY + gHeight)
        )
    }
}
enum class InspectorMode {
    OBJECT,
    MATERIAL,
    EFFECTS,
    ANIMATIONS,
    COLLIDERS
}
abstract class Modifier(val inspector: InspectorElement) {
    abstract fun redraw(g: Graphics)
}
class ObjectModifier(inspector: InspectorElement): Modifier(inspector) {

    val posText = "Position"
    val rotText = "Rotation"
    val sclText = "Scale"

    val posX = 0.15f
    val posY = 2f
    val posZ = Math.PI.toFloat()
    val rotX = 90.085f
    val rotY = 270f
    val rotZ = 0f
    val sclX = 1f
    val sclY = 1.5f
    val sclZ = 100f

    override fun redraw(g: Graphics) {
        // get string widths
        val posWidth = g.fontMetrics.stringWidth(posText)
        val rotWidth = g.fontMetrics.stringWidth(rotText)
        val sclWidth = g.fontMetrics.stringWidth(sclText)
        val singleWidth = g.fontMetrics.stringWidth("Z")
        val labelWidth = singleWidth + 10

        // unpack source panel
        val initX = inspector.panel.x
        val initY = inspector.panel.y
        val initW = inspector.panel.width
        val initH = inspector.panel.height

        // get spacers
        val fontHeight = g.fontMetrics.height
        val widthSpacer = (initW * 0.15f).toInt()

        // draw vector headers
        g.color = Color.white
        g.drawString(posText, initX + widthSpacer - (posWidth / 2), initY + (fontHeight * 3))
        g.drawString(rotText, initX + (widthSpacer * 2) + posWidth - (rotWidth / 2), initY + (fontHeight * 3))
        g.drawString(sclText, initX + (widthSpacer * 3) + rotWidth + posWidth - (sclWidth / 2), initY + (fontHeight * 3))

        // draw X Y Z labels
        drawLabel(g, "X", Color.red, initX + widthSpacer - posWidth, initY + (fontHeight * 4), labelWidth, fontHeight)
        drawLabel(g, "Y", Color.green, initX + widthSpacer - posWidth, initY + (fontHeight * 5), labelWidth, fontHeight)
        drawLabel(g, "Z", Color.blue, initX + widthSpacer - posWidth, initY + (fontHeight * 6), labelWidth, fontHeight)
        drawLabel(g, "X", Color.red, initX + (widthSpacer * 2) + posWidth - rotWidth, initY + (fontHeight * 4), labelWidth, fontHeight)
        drawLabel(g, "Y", Color.green, initX + (widthSpacer * 2) + posWidth - rotWidth, initY + (fontHeight * 5), labelWidth, fontHeight)
        drawLabel(g, "Z", Color.blue, initX + (widthSpacer * 2) + posWidth - rotWidth, initY + (fontHeight * 6), labelWidth, fontHeight)
        drawLabel(g, "X", Color.red, initX + (widthSpacer * 3) + posWidth + rotWidth - sclWidth, initY + (fontHeight * 4), labelWidth, fontHeight)
        drawLabel(g, "Y", Color.green, initX + (widthSpacer * 3) + posWidth + rotWidth - sclWidth, initY + (fontHeight * 5), labelWidth, fontHeight)
        drawLabel(g, "Z", Color.blue, initX + (widthSpacer * 3) + posWidth + rotWidth - sclWidth, initY + (fontHeight * 6), labelWidth, fontHeight)

        // draw text boxes
        drawTextBox(g, "OBJ_POS_X", initX + widthSpacer - posWidth + labelWidth, initY + (fontHeight * 4), (widthSpacer / 2) - labelWidth, fontHeight)
        drawTextBox(g, "OBJ_POS_Y", initX + widthSpacer - posWidth + labelWidth, initY + (fontHeight * 5), (widthSpacer / 2) - labelWidth, fontHeight)
        drawTextBox(g, "OBJ_POS_Z", initX + widthSpacer - posWidth + labelWidth, initY + (fontHeight * 6), (widthSpacer / 2) - labelWidth, fontHeight)
        drawTextBox(g, "OBJ_ROT_X", initX + (widthSpacer * 2) + posWidth - rotWidth + labelWidth, initY + (fontHeight * 4), (widthSpacer / 2) - labelWidth, fontHeight)
        drawTextBox(g, "OBJ_ROT_Y", initX + (widthSpacer * 2) + posWidth - rotWidth + labelWidth, initY + (fontHeight * 5), (widthSpacer / 2) - labelWidth, fontHeight)
        drawTextBox(g, "OBJ_ROT_Z", initX + (widthSpacer * 2) + posWidth - rotWidth + labelWidth, initY + (fontHeight * 6), (widthSpacer / 2) - labelWidth, fontHeight)
        drawTextBox(g, "OBJ_SCL_X", initX + (widthSpacer * 3) + posWidth + rotWidth - sclWidth + labelWidth, initY + (fontHeight * 4), (widthSpacer / 2) - labelWidth, fontHeight)
        drawTextBox(g, "OBJ_SCL_Y", initX + (widthSpacer * 3) + posWidth + rotWidth - sclWidth + labelWidth, initY + (fontHeight * 5), (widthSpacer / 2) - labelWidth, fontHeight)
        drawTextBox(g, "OBJ_SCL_Z", initX + (widthSpacer * 3) + posWidth + rotWidth - sclWidth + labelWidth, initY + (fontHeight * 6), (widthSpacer / 2) - labelWidth, fontHeight)
    }

    fun drawLabel(g: Graphics, text: String, color: Color, x: Int, y: Int, width: Int, height: Int) {
        // measure string
        val strWidth = g.fontMetrics.stringWidth(text)
        val strHeight = g.fontMetrics.height

        // draw background
        g.color = color
        g.fillRect(x, y, width, height)

        // draw string
        g.color = Color.black
        g.drawString(text, x + (strWidth / 2), y + (strHeight / 4 * 3))
    }

    fun drawTextBox(g: Graphics, tag: String, x: Int, y: Int, width: Int, height: Int) {
        // check if mouse clicked this text input
        if (Globals.mouseButtonDown == 1 && Rectangle(x, y, width, height).intersects(Rectangle(Globals.xPos, Globals.yPos, 1, 1)))
            TextInputManager.selectTag(tag)

        // get and measure string
        val str = TextInputManager.getTagsValueOrDefault(tag, "0")
        val strWidth = g.fontMetrics.stringWidth(str)
        val strHeight = g.fontMetrics.height

        // draw background
        g.color = if (TextInputManager.isTagSelected(tag)) Colors.textInputSelected else Colors.farthestBG
        g.fillRect(x, y, width, height)

        // draw string
        g.color = Color.white
        g.drawString(str, x + 5, y + (strHeight / 4 * 3))

        // if this tag is selected, draw cursor
        g.color = Color.black
        g.fillRect(x + strWidth + 5, y + 2, 2, strHeight - 4)
    }
}