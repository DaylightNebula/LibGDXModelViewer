package daylightnebula.ktxmodelviewer.ui

import daylightnebula.ktxmodelviewer.viewer.ModelViewer

object TextInputManager {
    private val map = hashMapOf<String, Pair<TextInputType, String>>()
    private var selectedTag = ""

    fun getTagsValue(tag: String): String? {
        return map[tag]?.second
    }

    fun getTagsValueOrDefault(tag: String, default: String): String {
        return getTagsValue(tag) ?: default
    }

    fun setTag(tag: String, type: TextInputType, value: String) {
        map[tag] = Pair(type, value)
    }

    fun selectTag(tag: String) {
        selectedTag = tag
    }

    fun deselectTag() {
        selectedTag = ""
    }

    fun getSelectedTag(): String {
        return selectedTag
    }

    fun isTagSelected(tag: String): Boolean {
        return selectedTag == tag
    }

    fun isTagActive(): Boolean {
        return selectedTag != ""
    }

    fun manageKey(c: Char) {
        if (!isTagActive()) return

        // breakup tag entry
        val initPair = map[selectedTag] ?: return
        val type = initPair.first
        var currentValueStr = initPair.second

        // update tag entry as necessary
        when(type) {
            TextInputType.FLOAT -> {
                val hasDot = currentValueStr.contains(".")
                if ((!hasDot && c == '.') || c.isDigit())
                    currentValueStr += c
                else if (c == '\b' && currentValueStr.length > 0)
                    currentValueStr = currentValueStr.substring(0, currentValueStr.length - 1)
            }
            TextInputType.INT -> {
                if (c.isDigit())
                    currentValueStr += c
                else if (c == '\b' && currentValueStr.length > 0)
                    currentValueStr = currentValueStr.substring(0, currentValueStr.length - 1)
            }
            TextInputType.STRING -> {
                if (c == '\b' && currentValueStr.length > 0)
                    currentValueStr = currentValueStr.substring(0, currentValueStr.length - 1)
                else if (c.isDefined())
                    currentValueStr += c
            }
            else -> {
                println("ERROR: No handle made for text input type $type")
                return
            }
        }

        // save new string
        map[selectedTag] = Pair(type, currentValueStr)

        // update model data if necessary
        val tagTokens = selectedTag.split("_")
        when(tagTokens[0]) {
            "OBJ" -> { ModelViewer.INSTANCE.modelData?.updateObjectData() }
            else -> {}
        }
    }
}
enum class TextInputType {
    FLOAT,
    INT,
    STRING
}