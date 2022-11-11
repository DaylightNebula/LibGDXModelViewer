package daylightnebula.ktxmodelviewer

import com.badlogic.gdx.math.Vector3
import org.json.JSONArray
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.ZipInputStream

object Utils {
    fun unzip(zipFile: File, dest: File) {
        // create output directory if it doesn't exist
        if (!dest.exists()) dest.mkdirs()
        val fis: FileInputStream
        //buffer for read and write data to file
        val buffer = ByteArray(1024)
        try {
            fis = FileInputStream(zipFile)
            val zis = ZipInputStream(fis)
            var ze = zis.nextEntry
            while (ze != null) {
                val fileName = ze.getName()
                val newFile = File(dest, fileName)
                System.out.println("Unzipping to " + newFile.getAbsolutePath())
                //create directories for sub directories in zip
                File(newFile.getParent()).mkdirs()
                val fos = FileOutputStream(newFile)
                var len: Int
                while (zis.read(buffer).also { len = it } > 0) {
                    fos.write(buffer, 0, len)
                }
                fos.close()
                //close this ZipEntry
                zis.closeEntry()
                ze = zis.nextEntry
            }
            //close last ZipEntry
            zis.closeEntry()
            zis.close()
            fis.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun convertVec3ToJsonArray(vec: Vector3): JSONArray {
        return JSONArray().put(vec.x).put(vec.y).put(vec.z)
    }

    fun convertJsonArrayToVec3(json: JSONArray): Vector3 {
        return Vector3(
            json.getFloat(0),
            json.getFloat(1),
            json.getFloat(2)
        )
    }
}