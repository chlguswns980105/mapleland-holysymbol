import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser
import com.sun.jna.platform.win32.Kernel32
import javax.swing.*
import java.awt.*
import java.awt.datatransfer.StringSelection
import java.util.logging.Level
import java.util.logging.Logger
import javax.sound.sampled.AudioSystem
import java.io.BufferedInputStream
import java.time.LocalDateTime

val frame = JFrame("타이머")
val inputField = JTextField(10).apply { text = "120" }

val remainLabel = JLabel("남은 시간: ")

val refreshButton = JButton("갱신하기").apply { isVisible = false }
val changeKeyButton = JButton("")
var currentShortcutKeyCode = NativeKeyEvent.VC_R  // ← 현재 단축키 기본값 (R)

val startHour = JTextField("00")
val startMin = JTextField("00")

val callSymCheck = JCheckBox("끝날 때 ㄱㄱ복사", null, true)
val forceFocus = JCheckBox("끝날 때 메랜 포커스", null, true)

val clipboard = Toolkit.getDefaultToolkit().systemClipboard

var timer: Timer? = null

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
                remainLabel.text = ""
                playSound("kwarosa.wav")
                refreshButton.isVisible = true
                timer?.stop()

                // 콜심 체크되어있을 때
                if (callSymCheck.isSelected) {
                    val hour = "${startHour.getText()}시"
                    val min = "${startMin.getText()}분"
                    val text = "$hour ${min}시작ㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱ"
                    val selection = StringSelection(text.plus(System.currentTimeMillis().toString()))
                    clipboard.setContents(selection, null)
                }

                // 포커싱 체크했을 때 메랜으로 포커스
                if (forceFocus.isSelected) focusMapleStoryWindow();
            }
        }.apply { start() }
    } else {
        remainLabel.text = "숫자를 입력해주세요!"
    }
}

fun stopTimer() {
    timer?.stop()
    remainLabel.text = "남은 시간: "
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

fun getStartTime() {
    val now = LocalDateTime.now()
    // 15초 정도는 더 줘라 좀
    val startTime = now.plusSeconds(if (now.second >= 45) 15 else 0)
    startHour.text = formatNumber(startTime.hour)
    startMin.text = formatNumber(startTime.minute)
}

fun formatNumber(number: Int): String {
    return String.format("%02d", number)
}

fun focusMapleStoryWindow() {
    val user32 = User32.INSTANCE
    val kernel32 = Kernel32.INSTANCE

    val enumProc = object : WinUser.WNDENUMPROC {
        override fun callback(hWnd: WinDef.HWND?, lParam: Pointer?): Boolean {
            if (hWnd != null) {
                val sb = CharArray(512)
                user32.GetWindowText(hWnd, sb, 512)
                val title = String(sb).trim { it <= ' ' }

                val c1 = title.contains("MapleStory Worlds", ignoreCase = true)
                val c2 = title.contains("Mapleland", ignoreCase = true)

                // 메월드, 메랜 모두 포함일 때 (메랜지지 등 메이플랜드 포함일 때 포커스 꼬임)
                if (c1 && c2) {
                    // 최소화된 창이면 복원
                    user32.ShowWindow(hWnd, WinUser.SW_RESTORE)

                    // 현재 스레드와 포커스를 가진 스레드를 연결
                    val fgWnd = user32.GetForegroundWindow()
                    val kernelId = kernel32.GetCurrentThreadId().toLong()
                    val currentThreadId = WinDef.DWORD(kernelId)
                    val processId = user32.GetWindowThreadProcessId(fgWnd, null).toLong()
                    val fgThreadId = WinDef.DWORD(processId)

                    user32.AttachThreadInput(fgThreadId, currentThreadId, true)

                    // 강제로 포커스
                    user32.BringWindowToTop(hWnd)
                    user32.SetForegroundWindow(hWnd)

                    // 스레드 입력 연결 해제
                    user32.AttachThreadInput(fgThreadId, currentThreadId, false)

                    return false // 첫 번째 매칭 후 종료
                }
            }
            return true // 계속 열거
        }
    }

    user32.EnumWindows(enumProc, null)
}

fun main() {

    val inputPanel = JPanel(FlowLayout(FlowLayout.LEFT))
    val guideText = JLabel("설정 시간(초):")
    val startButton = JButton("시작")
    val endButton = JButton("종료")

    inputPanel.add(guideText)
    inputPanel.add(inputField)
    inputPanel.add(startButton)
    inputPanel.add(endButton)

    val shortCutPanel = JPanel(FlowLayout(FlowLayout.LEFT))
    shortCutPanel.add(changeKeyButton).also { setChangeButtonText() }
    shortCutPanel.add(refreshButton)
    shortCutPanel.add(remainLabel)

    val resultPanel = JPanel(FlowLayout(FlowLayout.LEFT))
    val startTimeButton = JButton("시작시간 갱신")
    resultPanel.add(startTimeButton)
    resultPanel.add(startHour)
    resultPanel.add(startMin)

    val checkBoxPanel = JPanel()
    checkBoxPanel.layout = BoxLayout(checkBoxPanel, BoxLayout.Y_AXIS)
    checkBoxPanel.add(callSymCheck)
    checkBoxPanel.add(forceFocus)
    resultPanel.add(checkBoxPanel)

    val mainPanel = JPanel()
    mainPanel.layout = BoxLayout(mainPanel, BoxLayout.Y_AXIS)
    mainPanel.add(inputPanel)
    mainPanel.add(shortCutPanel)
    mainPanel.add(resultPanel)

    frame.add(mainPanel)
    frame.setSize(350, 170)
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.isVisible = true
    frame.isAlwaysOnTop = true
    frame.toFront()
    frame.requestFocus()

    // 이벤트
    startButton.addActionListener { runTimer() }
    endButton.addActionListener { stopTimer() }
    refreshButton.addActionListener { runTimer() }
    startTimeButton.addActionListener { getStartTime() }

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
                dialogWindow.dispose() // ← 키 입력 시 안내창 닫기
                setChangeButtonText()
                GlobalScreen.removeNativeKeyListener(this)
            }

            override fun nativeKeyReleased(e: NativeKeyEvent) {}
            override fun nativeKeyTyped(e: NativeKeyEvent) {}
        })
    }
}

fun setChangeButtonText() {
    val key = NativeKeyEvent.getKeyText(currentShortcutKeyCode)
    changeKeyButton.text = "갱신 단축키 변경 (현재 $key)"
}