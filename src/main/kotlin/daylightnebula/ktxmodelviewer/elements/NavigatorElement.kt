package daylightnebula.ktxmodelviewer.elements

import daylightnebula.ktxmodelviewer.Colors
import daylightnebula.ktxmodelviewer.Utils
import daylightnebula.ktxmodelviewer.ui.ButtonElement
import daylightnebula.ktxmodelviewer.viewer.ModelViewer
import java.awt.Graphics
import java.io.File
import javax.swing.JFileChooser
import javax.swing.JFileChooser.APPROVE_OPTION
import javax.swing.JOptionPane
import javax.swing.filechooser.FileFilter

class NavigatorElement: EditorElement() {

    val importCallback = {
        // setup file chooser
        val chooser = JFileChooser()
        chooser.currentDirectory = File(System.getProperty("user.dir"))
        chooser.fileFilter = object : FileFilter() {
            override fun accept(file: File): Boolean { return file.isDirectory || file.name.endsWith(".gltf") }
            override fun getDescription(): String { return "GLTF Model Files" }
        }

        // get result
        val result = chooser.showOpenDialog(null)
        val target = chooser.selectedFile

        // if result is positive, open the target
        if (result == APPROVE_OPTION) {
            ModelViewer.INSTANCE.modelsToImport.add(target)
        }
    }

    val saveCallback = {
        // get model data, cancel if none found
        val modelData = ModelViewer.INSTANCE.modelData

        // setup file chooser
        val chooser = JFileChooser()
        chooser.currentDirectory = File(System.getProperty("user.dir"))
        chooser.fileFilter = object : FileFilter() {
            override fun accept(file: File): Boolean { return file.isDirectory || file.name.endsWith(".scythe") }
            override fun getDescription(): String { return "SCYTHE Model Files" }
        }

        // get result
        val result = chooser.showSaveDialog(null)
        val target = chooser.selectedFile

        // if result is positive, open the target
        if (result == APPROVE_OPTION) {
            modelData?.compressToFile(target)
        }
    }

    val openCallback = {
        // setup file chooser
        val chooser = JFileChooser()
        chooser.currentDirectory = File(System.getProperty("user.dir"))
        chooser.fileFilter = object : FileFilter() {
            override fun accept(file: File): Boolean { return file.isDirectory || file.name.endsWith(".scythe") }
            override fun getDescription(): String { return "SCYTHE Model Files" }
        }

        // get result
        val result = chooser.showOpenDialog(null)
        val target = chooser.selectedFile

        if (result == APPROVE_OPTION) {
            // clear tmp folder
            val tmpFolder = File(System.getProperty("user.dir"), "tmp")
            tmpFolder.mkdirs()
            tmpFolder.listFiles()?.forEach { it.deleteRecursively() }

            // unzip target to tmp folder
            Utils.unzip(target, tmpFolder)

            // tell model viewer to open from tmp
            ModelViewer.INSTANCE.loadFromTMP = true
        }
    }

    val newCallback = {
        // ask the user if they are sure they want to clear
        val result = JOptionPane.showConfirmDialog(null, "Are you sure you want to clear and start new?  Any unsaved progress will be lost.")

        // if the user confirms they want to clear, save
        if (result == APPROVE_OPTION)
            ModelViewer.INSTANCE.clear()
    }

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
            gX, winHeight
        )
    }
}