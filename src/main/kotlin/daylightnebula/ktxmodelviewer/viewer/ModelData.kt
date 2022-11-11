package daylightnebula.ktxmodelviewer.viewer

import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.model.Animation
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import daylightnebula.ktxmodelviewer.Utils
import daylightnebula.ktxmodelviewer.ui.TextInputManager
import daylightnebula.ktxmodelviewer.ui.TextInputType
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class ModelData(
    val modelFile: File,
    var position: Vector3,
    var rotation: Vector3,
    var scale: Vector3,
    val animations: com.badlogic.gdx.utils.Array<Animation>,
    val materials: com.badlogic.gdx.utils.Array<Material>
) {
    constructor(modelFile: File, instance: ModelInstance): this(
        modelFile,
        Vector3(0f, 0f, 0f), Vector3(0f, 0f, 0f), Vector3(1f, 1f, 1f),
        instance.animations, instance.materials
    ) {
        // copy model file to temporary folder
        modelFile.copyTo(File(System.getProperty("user.dir"), "tmp/model.gltf"), true)
    }

    constructor(instance: ModelInstance, json: JSONObject): this(
        File(System.getProperty("user.dir"), "tmp/model.gltf"),
        Utils.convertJsonArrayToVec3(json.getJSONArray("position")),
        Utils.convertJsonArrayToVec3(json.getJSONArray("rotation")),
        Utils.convertJsonArrayToVec3(json.getJSONArray("scale")),
        instance.animations, instance.materials
    ) {
        updateObjectData()
    }

    init {
        TextInputManager.setTag("OBJ_POS_X", TextInputType.FLOAT, specFloatToStringConvert(position.x))
        TextInputManager.setTag("OBJ_POS_Y", TextInputType.FLOAT, specFloatToStringConvert(position.y))
        TextInputManager.setTag("OBJ_POS_Z", TextInputType.FLOAT, specFloatToStringConvert(position.z))
        TextInputManager.setTag("OBJ_ROT_X", TextInputType.FLOAT, specFloatToStringConvert(rotation.x))
        TextInputManager.setTag("OBJ_ROT_Y", TextInputType.FLOAT, specFloatToStringConvert(rotation.y))
        TextInputManager.setTag("OBJ_ROT_Z", TextInputType.FLOAT, specFloatToStringConvert(rotation.z))
        TextInputManager.setTag("OBJ_SCL_X", TextInputType.FLOAT, specFloatToStringConvert(scale.x))
        TextInputManager.setTag("OBJ_SCL_Y", TextInputType.FLOAT, specFloatToStringConvert(scale.y))
        TextInputManager.setTag("OBJ_SCL_Z", TextInputType.FLOAT, specFloatToStringConvert(scale.z))
    }

    fun specFloatToStringConvert(value: Float): String {
        val str = value.toString()
        if (str.endsWith(".0"))
            return str.substring(0, str.length - 2)
        else
            return str
    }

    fun getTextInputFloat(tag: String, default: Float): Float {
        return TextInputManager.getTagsValue(tag)?.toFloatOrNull() ?: default
    }

    fun updateObjectData() {
        // get model instance, return if none
        val instance = ModelViewer.INSTANCE.modelInstance ?: return

        // get create vector3's out of position rotation and scale from text input
        position = Vector3(getTextInputFloat("OBJ_POS_X",0f), getTextInputFloat("OBJ_POS_Y", 0f), getTextInputFloat("OBJ_POS_Z", 0f))
        rotation = Vector3(getTextInputFloat("OBJ_ROT_X",0f), getTextInputFloat("OBJ_ROT_Y", 0f), getTextInputFloat("OBJ_ROT_Z", 0f))
        scale = Vector3(getTextInputFloat("OBJ_SCL_X",0f), getTextInputFloat("OBJ_SCL_Y", 0f), getTextInputFloat("OBJ_SCL_Z", 0f))

        // set model instance position rotation and scale
        val rotationQuat = Quaternion().setEulerAngles(rotation.x, rotation.y, rotation.z)
        instance.transform.set(position, rotationQuat, scale)

        // save json to temporary file
        saveJSONToTMP()
    }



    fun saveJSONToTMP() {
        // create json
        val json = JSONObject()

        // save basic data
        json.put("position", Utils.convertVec3ToJsonArray(position))
        json.put("rotation", Utils.convertVec3ToJsonArray(rotation))
        json.put("scale", Utils.convertVec3ToJsonArray(scale))

        // convert json to string and then save it to the temporary folder
        val jsonStr = json.toString(1)
        File(System.getProperty("user.dir"), "tmp/data.json").writeText(jsonStr)
    }

    fun compressToFile(target: File) {
        // get temporary files and list of files to save
        val tmpFile = File(System.getProperty("user.dir"), "tmp")
        val filesToCompress = tmpFile.listFiles()

        // get output streams
        val fos = FileOutputStream(target)
        val zos = ZipOutputStream(fos)

        // loop through all files synchronously, and save each to the zip output stream
        for (file in filesToCompress) {
            // add file to zip output stream
            val fis = FileInputStream(file)
            val zipEntry = ZipEntry(file.name)
            zos.putNextEntry(zipEntry)

            // add files data to zip output stream
            val bytes = ByteArray(1024)
            var length = fis.read(bytes)
            do {
                zos.write(bytes, 0, length)
                length = fis.read(bytes)
            } while(length >= 0)
            fis.close()
        }

        // close everything
        zos.close()
        fos.close()
    }
}