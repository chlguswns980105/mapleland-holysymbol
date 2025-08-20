import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
import javax.swing.*
import java.awt.*
import java.util.logging.Level
import java.util.logging.Logger
import javax.sound.sampled.AudioSystem
import java.io.BufferedInputStream

val inputField = JTextField(10).apply { text = "120" }
const val defaultRemainText = "남은 시간이 여기에 표시됩니다."
val remainLabel = JLabel(defaultRemainText)
var currentRefreshKey = "R"
val refreshButton = JButton("갱신하기").apply { isVisible = false }

var timer: Timer? = null
var currentShortcutKeyCode = NativeKeyEvent.VC_R  // ← 현재 단축키 기본값

fun playSound(resourcePath: String) {
    try {
        val audioInput = AudioSystem.getAudioInputStream(
            BufferedInputStream(ClassLoader.getSystemResourceAsStream(resourcePath))
        )
        val clip = AudioSystem.getClip()
        clip.open(audioInput)
        clip.start()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun runTimer() {
    timer?.stop()
    val inputText = inputField.text
    var remainingTime = inputText.toIntOrNull()

    if (remainingTime != null && remainingTime > 0) {
        remainLabel.text = "남은 시간: $remainingTime 초"
        refreshButton.isVisible = false

        timer = Timer(1000) {
            remainingTime--
            if (remainingTime > 0) {
                remainLabel.text = "남은 시간: $remainingTime 초"
            } else {
                remainLabel.text = "타이머 종료!"
                playSound("kwarosa.wav")
                refreshButton.isVisible = true
                timer?.stop()
            }
        }.apply { start() }
    } else {
        remainLabel.text = "숫자를 입력해주세요!"
    }
}

fun stopTimer() {
    timer?.stop()
    remainLabel.text = defaultRemainText
    refreshButton.isVisible = false
}

fun registerGlobalShortcut() {
    // 로그 제거
    Logger.getLogger(GlobalScreen::class.java.name).level = Level.OFF
    Logger.getLogger("").handlers.forEach { it.level = Level.OFF }

    if (!GlobalScreen.isNativeHookRegistered()) {
        GlobalScreen.registerNativeHook()
    }

    GlobalScreen.addNativeKeyListener(object : NativeKeyListener {
        override fun nativeKeyPressed(e: NativeKeyEvent) {
            if (e.keyCode == currentShortcutKeyCode && refreshButton.isVisible) {
                refreshButton.doClick()
            }
        }

        override fun nativeKeyReleased(e: NativeKeyEvent) {}
        override fun nativeKeyTyped(e: NativeKeyEvent) {}
    })
}

fun main() {
    val frame = JFrame("타이머")

    val inputPanel = JPanel(FlowLayout(FlowLayout.LEFT))
    val guideText = JLabel("설정 시간(초):")
    val startButton = JButton("시작하기")
    val endButton = JButton("종료하기")
    val changeKeyButton = JButton("단축키 변경")

    inputPanel.add(guideText)
    inputPanel.add(inputField)
    inputPanel.add(startButton)
    inputPanel.add(endButton)

    val shortCutPanel = JPanel(FlowLayout(FlowLayout.LEFT))
    val shortCutLabel = JLabel("현재 갱신 단축키: $currentRefreshKey")
    shortCutPanel.add(shortCutLabel)
    shortCutPanel.add(changeKeyButton)

    val resultPanel = JPanel(FlowLayout(FlowLayout.LEFT))
    resultPanel.add(remainLabel)
    resultPanel.add(refreshButton)

    val mainPanel = JPanel()
    mainPanel.layout = BoxLayout(mainPanel, BoxLayout.Y_AXIS)
    mainPanel.add(inputPanel)
    mainPanel.add(shortCutPanel)
    mainPanel.add(resultPanel)

    frame.add(mainPanel)
    frame.setSize(600, 200)
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.isVisible = true
    frame.isAlwaysOnTop = true
    frame.toFront()
    frame.requestFocus()

    // 버튼 이벤트
    startButton.addActionListener { runTimer() }
    endButton.addActionListener { stopTimer() }
    refreshButton.addActionListener { runTimer() }

    // 전역 단축키 등록
    registerGlobalShortcut()

    // 단축키 변경 버튼 기능
    changeKeyButton.addActionListener {
        // 키 입력 안내용 다이얼로그 직접 생성
        val dialog = JOptionPane("변경할 키를 눌러주세요.", JOptionPane.INFORMATION_MESSAGE)
        val dialogWindow = dialog.createDialog(frame, "단축키 변경")
        dialogWindow.isModal = false
        dialogWindow.isVisible = true

        GlobalScreen.addNativeKeyListener(object : NativeKeyListener {
            override fun nativeKeyPressed(e: NativeKeyEvent) {
                currentShortcutKeyCode = e.keyCode
                currentRefreshKey = NativeKeyEvent.getKeyText(currentShortcutKeyCode)
                shortCutLabel.text = "현재 갱신 단축키: $currentRefreshKey"
                dialogWindow.dispose() // ← 키 입력 시 안내창 닫기
                GlobalScreen.removeNativeKeyListener(this)
            }

            override fun nativeKeyReleased(e: NativeKeyEvent) {}
            override fun nativeKeyTyped(e: NativeKeyEvent) {}
        })
    }
}