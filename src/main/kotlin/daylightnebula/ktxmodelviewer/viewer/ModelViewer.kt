package daylightnebula.ktxmodelviewer.viewer

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import java.io.File

class ModelViewer: ApplicationListener {

    lateinit var camera: PerspectiveCamera
    lateinit var batch: ModelBatch

    lateinit var gridLines: GroundGridLines
    var model: Model? = null
    var modelInstance: ModelInstance? = null

    override fun create() {
        batch = ModelBatch()
        gridLines = GroundGridLines(4, 2f)

        // setup camera
        camera = PerspectiveCamera(67f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        camera.position.set(3f, 3f, 3f)
        camera.lookAt(0f,0f,0f)
        camera.near = 0.001f
        camera.far = 30000f
        camera.update()

        // setup input
        Gdx.input.inputProcessor = ModelViewerInputProcessor(camera)

        loadModel("D:\\projectscythe\\ScytheModelViewer\\testassets\\CORVETTE_FINAL 01.obj")
    }

    fun loadModel(path: String) {
        if (!File(path).exists()) return


        model?.dispose()
        model = ObjLoader().loadModel(Gdx.files.absolute(path))
        modelInstance = ModelInstance(model)
    }

    override fun resize(width: Int, height: Int) {
        camera.viewportWidth = width.toFloat()
        camera.viewportHeight = height.toFloat()
    }

    override fun render() {
        camera.update()

        Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        batch.begin(camera)
        batch.render(gridLines.instance)
        if (modelInstance != null) batch.render(modelInstance!!)
        batch.end()
    }

    override fun pause() {
        
    }

    override fun resume() {
        
    }

    override fun dispose() {
        gridLines.dispose()
        model?.dispose()
    }
}