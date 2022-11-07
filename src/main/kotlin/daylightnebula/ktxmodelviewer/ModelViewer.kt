package daylightnebula.ktxmodelviewer

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder

class ModelViewer: ApplicationListener {

    lateinit var camera: PerspectiveCamera
    lateinit var batch: ModelBatch

    lateinit var gridLines: GroundGridLines

    override fun create() {
        batch = ModelBatch()
        gridLines = GroundGridLines(4, 2f)

        // setup camera
        camera = PerspectiveCamera(67f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        camera.position.set(3f, 3f, 3f)
        camera.lookAt(0f,0f,0f)
        camera.near = 1f
        camera.far = 300f
        camera.update()
    }

    override fun resize(width: Int, height: Int) {
        
    }

    override fun render() {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        batch.begin(camera)
        batch.render(gridLines.instance)
        batch.end()
    }

    override fun pause() {
        
    }

    override fun resume() {
        
    }

    override fun dispose() {
        gridLines.dispose()
    }
}