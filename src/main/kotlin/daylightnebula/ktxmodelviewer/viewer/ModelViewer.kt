package daylightnebula.ktxmodelviewer.viewer

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider
import com.badlogic.gdx.utils.JsonReader
import net.mgsx.gltf.loaders.gltf.GLTFLoader
import java.io.File

class ModelViewer: ApplicationListener {

    lateinit var camera: PerspectiveCamera
    lateinit var batch: ModelBatch
    lateinit var environment: Environment

    lateinit var gridLines: GroundGridLines
    var model: Model? = null
    var modelInstance: ModelInstance? = null

    val modelsToImport = mutableListOf<File>()
    var modelData: ModelData? = null

    companion object {
        val INSTANCE = ModelViewer()
    }

    override fun create() {
        val shaderConfig = DefaultShader.Config()
        shaderConfig.defaultCullFace = GL20.GL_NONE
        shaderConfig.numBones = 255
        batch = ModelBatch(DefaultShaderProvider(shaderConfig))
        gridLines = GroundGridLines(4, 2f)

        environment = Environment()
        environment.set(ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f))

        // setup camera
        camera = PerspectiveCamera(67f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        camera.position.set(3f, 3f, 3f)
        camera.lookAt(0f,0f,0f)
        camera.near = 0.1f
        camera.far = 1000f
        camera.update()

        // setup input
        Gdx.input.inputProcessor = ModelViewerInputProcessor(camera)
    }

    fun importModel(path: File) {
        model?.dispose()
        model = GLTFLoader().load(Gdx.files.absolute(path.absolutePath), true).scene.model

        modelInstance = ModelInstance(model)
        modelData = ModelData(path, modelInstance!!)
    }

    override fun resize(width: Int, height: Int) {
        camera.viewportWidth = width.toFloat()
        camera.viewportHeight = height.toFloat()
    }

    override fun render() {
        // import models
        if (modelsToImport.isNotEmpty()) {
            importModel(modelsToImport[0])
            modelsToImport.clear()
        }

        camera.update()

        Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        batch.begin(camera)
        batch.render(gridLines.instance, environment)
        if (modelInstance != null) batch.render(modelInstance!!, environment)
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