import java.io.BufferedInputStream
import javax.sound.sampled.AudioSystem

class SoundPlayer {
    fun play(resourcePath: String) {
        try {
            val resourceStream = ClassLoader.getSystemResourceAsStream(resourcePath) ?: return
            val audioInput = AudioSystem.getAudioInputStream(BufferedInputStream(resourceStream))
            val clip = AudioSystem.getClip()
            clip.open(audioInput)
            clip.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

