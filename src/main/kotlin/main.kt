import soundProcessing.SoundProcessing
import java.io.File

fun main(args: Array<String>) {
//    var output = ""
//
//    val soundEngine = SoundProcessing("tmp")
//
//    File("datasets/beeth").walk().forEach {
//        soundEngine.setFile(it)
//
//        val data = soundEngine.toText()
//        if(data != null) {
//            output+=data
//        }
//    }
//
//    File("dataset_Beethoven.txt").writeText(output)

    var tmp = "datasets/beeth/mond_3.mid"

    val soundEngine = SoundProcessing(tmp)
    tmp = soundEngine.toText()?:return
    soundEngine.toMidi(tmp, "mond_3")
}