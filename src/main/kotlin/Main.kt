import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import javax.swing.JTextField
import javax.swing.SwingUtilities
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun main() {
    val window = createMainWindow()
    val timerController = TimerController(window)
    val shortcutManager = GlobalShortcutManager()
    val clipboardService = ClipboardService()

    fun increaseAndCopy(field: JTextField, prefix: String) {
        SwingUtilities.invokeLater {
            val newValue = (field.text.toIntOrNull() ?: 0) + 1
            field.text = newValue.toString()
            clipboardService.copyText("${prefix}닼 $newValue")
        }
    }

    shortcutManager.registerShortcut(
        shortcutId = "refresh",
        defaultKeyCode = NativeKeyEvent.VC_R,
        onTriggered = {
            if (window.refreshButton.isVisible) {
                SwingUtilities.invokeLater { window.refreshButton.doClick() }
            }
        },
        onLabelChanged = { keyText ->
            window.changeKeyButton.text = "갱신키 변경 (현재 $keyText)"
        },
        enabledWhen = { window.tabbedPane.selectedIndex == TIMER_TAB_INDEX }
    )

    shortcutManager.registerShortcut(
        shortcutId = "wyvern-left-counter",
        defaultKeyCode = NativeKeyEvent.VC_F9,
        onTriggered = { increaseAndCopy(window.leftWyvernField, "좌") },
        onLabelChanged = { keyText -> window.leftWyvernKeyButton.text = "좌 ($keyText)" },
        enabledWhen = { window.tabbedPane.selectedIndex == WYVERN_TAB_INDEX }
    )

    shortcutManager.registerShortcut(
        shortcutId = "wyvern-middle-counter",
        defaultKeyCode = NativeKeyEvent.VC_F10,
        onTriggered = { increaseAndCopy(window.middleWyvernField, "중") },
        onLabelChanged = { keyText -> window.middleWyvernKeyButton.text = "중 ($keyText)" },
        enabledWhen = { window.tabbedPane.selectedIndex == WYVERN_TAB_INDEX }
    )

    shortcutManager.registerShortcut(
        shortcutId = "wyvern-right-counter",
        defaultKeyCode = NativeKeyEvent.VC_F11,
        onTriggered = { increaseAndCopy(window.rightWyvernField, "우") },
        onLabelChanged = { keyText -> window.rightWyvernKeyButton.text = "우 ($keyText)" },
        enabledWhen = { window.tabbedPane.selectedIndex == WYVERN_TAB_INDEX }
    )

    window.startButton.addActionListener { timerController.runTimer() }
    window.endButton.addActionListener { timerController.stopTimer() }
    window.refreshButton.addActionListener { timerController.runTimer() }
    window.startTimeButton.addActionListener { timerController.updateStartTime() }
    window.wyvernCurrentTimeButton.addActionListener {
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        val time = LocalTime.now().format(formatter)
        window.wyvernCurrentTimeField.text = LocalTime.now().format(formatter)
        clipboardService.copyText(time)
    }

    // 와이번 버튼 클릭 시 단축키 변경 다이얼로그 열기
    window.leftWyvernKeyButton.addActionListener {
        shortcutManager.bindChangeShortcutDialog(window.frame, "wyvern-left-counter", "와이번 좌 키 변경")
    }
    window.middleWyvernKeyButton.addActionListener {
        shortcutManager.bindChangeShortcutDialog(window.frame, "wyvern-middle-counter", "와이번 중 키 변경")
    }
    window.rightWyvernKeyButton.addActionListener {
        shortcutManager.bindChangeShortcutDialog(window.frame, "wyvern-right-counter", "와이번 우 키 변경")
    }

    shortcutManager.register()
    shortcutManager.bindChangeShortcutAction(window.frame, window.changeKeyButton, "refresh", "갱신 단축키 변경")
}