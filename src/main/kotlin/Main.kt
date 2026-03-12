import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import javax.swing.JTextField
import javax.swing.SwingUtilities

fun main() {
    val window = createMainWindow()
    val timerController = TimerController(window)
    val shortcutManager = GlobalShortcutManager()

    fun increaseCounter(field: JTextField) {
        SwingUtilities.invokeLater {
            val value = field.text.toIntOrNull() ?: 0
            field.text = (value + 1).toString()
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
            window.changeKeyButton.text = "갱신 단축키 변경 (현재 $keyText)"
        },
        enabledWhen = { window.tabbedPane.selectedIndex == TIMER_TAB_INDEX }
    )

    shortcutManager.registerShortcut(
        shortcutId = "wyvern-left-counter",
        defaultKeyCode = NativeKeyEvent.VC_F9,
        onTriggered = { increaseCounter(window.leftWyvernField) },
        onLabelChanged = { keyText ->
            window.leftWyvernKeyButton.text = "와이번 좌 키 변경 (현재 $keyText)"
        },
        enabledWhen = { window.tabbedPane.selectedIndex == WYVERN_TAB_INDEX }
    )

    shortcutManager.registerShortcut(
        shortcutId = "wyvern-middle-counter",
        defaultKeyCode = NativeKeyEvent.VC_F10,
        onTriggered = { increaseCounter(window.middleWyvernField) },
        onLabelChanged = { keyText ->
            window.middleWyvernKeyButton.text = "와이번 중 키 변경 (현재 $keyText)"
        },
        enabledWhen = { window.tabbedPane.selectedIndex == WYVERN_TAB_INDEX }
    )

    shortcutManager.registerShortcut(
        shortcutId = "wyvern-right-counter",
        defaultKeyCode = NativeKeyEvent.VC_F11,
        onTriggered = { increaseCounter(window.rightWyvernField) },
        onLabelChanged = { keyText ->
            window.rightWyvernKeyButton.text = "와이번 우 키 변경 (현재 $keyText)"
        },
        enabledWhen = { window.tabbedPane.selectedIndex == WYVERN_TAB_INDEX }
    )

    window.startButton.addActionListener { timerController.runTimer() }
    window.endButton.addActionListener { timerController.stopTimer() }
    window.refreshButton.addActionListener { timerController.runTimer() }
    window.startTimeButton.addActionListener { timerController.updateStartTime() }

    window.leftWyvernPlusButton.addActionListener { increaseCounter(window.leftWyvernField) }
    window.middleWyvernPlusButton.addActionListener { increaseCounter(window.middleWyvernField) }
    window.rightWyvernPlusButton.addActionListener { increaseCounter(window.rightWyvernField) }

    shortcutManager.register()
    shortcutManager.bindChangeShortcutAction(window.frame, window.changeKeyButton, "refresh", "갱신 단축키 변경")
    shortcutManager.bindChangeShortcutAction(window.frame, window.leftWyvernKeyButton, "wyvern-left-counter", "와이번 좌 키 변경")
    shortcutManager.bindChangeShortcutAction(window.frame, window.middleWyvernKeyButton, "wyvern-middle-counter", "와이번 중 키 변경")
    shortcutManager.bindChangeShortcutAction(window.frame, window.rightWyvernKeyButton, "wyvern-right-counter", "와이번 우 키 변경")
}