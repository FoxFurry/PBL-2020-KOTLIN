package soundProcessing

import java.io.File
import javax.sound.midi.MetaMessage
import kotlin.math.max

const val TICK_ASCII_OFFSET     = 35
const val NOTE_ON_ASCII_OFFSET  = 50     // 122 notes are getting out of standard ASCII. Consider using utf-8
const val NOTE_ON_SIGNAL        = 0x90
const val NOTE_COUNT            = 122

val tickTranlate = arrayOf(6144, 3072, 1536, 768, 384, 192, 64, 32, 8, 2)

class SoundProcessing {
    private var sourceFile: File
    private var lastMessagesList = ArrayList<MetaMessage>()
    private var lastMidiResolution = 120

    constructor(source: File) {
        sourceFile = source
    }

    constructor(source: String) {
        sourceFile = File(source)
    }

    fun toText(): String {
        if (sourceFile.extension == "wav" || sourceFile.extension == "mp3") {
            throw(MidiDecodingException("Wav and mp3 are not supported"))
        } else if (sourceFile.extension != "midi" && sourceFile.extension != "mid") {
            throw(MidiDecodingException("Unrecognized extension"))
        }

        val sourceBuffer = sourceFile
        val tempBuffer = File("tmp_of.tmp")

        tempBuffer.deleteOnExit()

        val currentMidiType = MidiProcessing().getType(sourceFile)
        try{
            if (currentMidiType > 0) {
                MidiProcessing().midiTypeConvert(sourceFile, tempBuffer)
                sourceFile = tempBuffer
            } else if (currentMidiType < 0) {
                throw(Exception("Error getting midi type data"))
            }

            val midiToStringData = MidiProcessing().midiToString(sourceFile)

            sourceFile = sourceBuffer
            lastMessagesList = midiToStringData.second
            lastMidiResolution = max(24, midiToStringData.third)

            tempBuffer.delete()

            println("Success <${sourceFile.name}>")
            return midiToStringData.first
        }catch(e:Exception){
            throw e;
        }
    }

    fun toMidi(source: String, output: String): Int {
        if (source.isEmpty()) {
            throw(Exception("Source string is empty. Looks like conversion was not successful."))
        }

        return MidiProcessing().stringToMidi(Triple(source, lastMessagesList, lastMidiResolution), output)
    }

    fun setFile(fl: File) {
        sourceFile = fl
    }

}

