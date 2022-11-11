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
import org.json.JSONObject
import java.io.File

class ModelViewer: ApplicationListener {

    lateinit var camera: PerspectiveCamera
    lateinit var batch: ModelBatch
    lateinit var environment: Environment

    lateinit var gridLines: GroundGridLines
    var model: Model? = null
    var modelInstance: ModelInstance? = null

    var loadFromTMP = false
    val modelsToImport = mutableListOf<File>()
    var modelData: ModelData? = null

    companion object {
        val INSTANCE = ModelViewer()
    }

    override fun create() {
        // setup shader
        val shaderConfig = DefaultShader.Config()
        shaderConfig.defaultCullFace = GL20.GL_NONE
        shaderConfig.numBones = 255
        batch = ModelBatch(DefaultShaderProvider(shaderConfig))

        // setup grid lines
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

    fun loadTMP() {
        // get tmp folder
        val tmpFolder = File(System.getProperty("user.dir"), "tmp")

        // replace model with new one
        model?.dispose()
        model = GLTFLoader().load(Gdx.files.absolute(tmpFolder.absolutePath + "/model.gltf"), true).scene.model
        modelInstance = ModelInstance(model)

        // create new model data from json
        val jsonFile = File(tmpFolder, "data.json")
        modelData = ModelData(modelInstance!!, JSONObject(jsonFile.readText()))
        println("Model data after load ${modelData}")
    }

    fun clear() {
        modelData = null
        modelInstance = null
        model?.dispose()
        model = null
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

        // load from tmp if necessary
        if (loadFromTMP) {
            loadTMP()
            loadFromTMP = false
        }

        // update camera
        camera.update()

        // clear window
        Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        // render everything
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