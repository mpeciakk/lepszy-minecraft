package shader

// Yes, I made custom preprocessor for shaders, I am not mad, am I?
class GLSLPreprocessor {
    fun process(shader: String): String {
        val out = StringBuilder()
        val lines = shader.split("\\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val passVariables = mutableListOf<String>()

        for (line in lines) {
            if (line.startsWith("#section")) {
                val elements = line.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val section = elements[1]

                if (section == "VERTEX_SHADER") {
                    out.append("#ifdef VERTEX").append("\n")

                    for (variable in passVariables) {
                        out.append("out ").append(variable).append("\n")
                    }
                }
                if (section == "FRAGMENT_SHADER") {
                    out.append("#endif").append("\n").append("#ifdef FRAGMENT").append("\n")

                    for (variable in passVariables) {
                        out.append("in ").append(variable).append("\n")
                    }
                }
            } else if (line.startsWith("varying")) {
                val elements = line.split(" ".toRegex(), limit = 2).toTypedArray()
                val variable = elements[1]

                passVariables.add(variable)
            } else {
                out.append('\n')
                out.append(line)
            }
        }

        out.append('\n').append("#endif")
        return out.toString()
    }
}