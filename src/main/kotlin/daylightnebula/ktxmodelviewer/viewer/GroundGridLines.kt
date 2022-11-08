package daylightnebula.ktxmodelviewer.viewer

import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import daylightnebula.ktxmodelviewer.Colors

class GroundGridLines(val segmentCount: Int, val lineLength: Float) {
    
    lateinit var model: Model
    lateinit var instance: ModelInstance
    
    init {
        val modelBuilder = ModelBuilder()
        modelBuilder.begin()

        repeat(segmentCount + 1) { seg ->
            val percComplete = seg.toFloat() / segmentCount
            val a = (lineLength - 1) / 2f
            val b = -a + (a * 2f * percComplete)

            val builder = modelBuilder.part("line_x_$seg", 1, 3, Material())
            builder.setColor(Colors.forwardBG)
            builder.line(-a, 0f, b, a, 0f, b)
            builder.line(b, 0f, -a, b, 0f, a)
        }
        model = modelBuilder.end()
        instance = ModelInstance(model)
    }
    
    fun dispose() { model.dispose() }
}