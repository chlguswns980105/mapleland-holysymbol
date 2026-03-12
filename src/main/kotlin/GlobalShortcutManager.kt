import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
import java.awt.Component
import java.util.logging.Level
import java.util.logging.Logger
import javax.swing.JButton
import javax.swing.JOptionPane

class GlobalShortcutManager(
    private val onRefreshShortcut: () -> Unit,
    private val onShortcutLabelChanged: (String) -> Unit
) {
    var currentShortcutKeyCode: Int = NativeKeyEvent.VC_R

    private val refreshKeyListener = object : NativeKeyListener {
        override fun nativeKeyPressed(e: NativeKeyEvent) {
            if (e.keyCode == currentShortcutKeyCode) {
                onRefreshShortcut()
            }
        }

        override fun nativeKeyReleased(e: NativeKeyEvent) {}
        override fun nativeKeyTyped(e: NativeKeyEvent) {}
    }

    fun register() {
        // JNativeHook 기본 로그를 끈다.
        Logger.getLogger(GlobalScreen::class.java.name).level = Level.OFF
        Logger.getLogger("").handlers.forEach { it.level = Level.OFF }

        if (!GlobalScreen.isNativeHookRegistered()) {
            GlobalScreen.registerNativeHook()
        }

        GlobalScreen.addNativeKeyListener(refreshKeyListener)
        updateButtonText()
    }

    fun bindChangeShortcutAction(owner: Component, changeKeyButton: JButton) {
        changeKeyButton.addActionListener {
            val dialog = JOptionPane("변경할 키를 눌러주세요.", JOptionPane.INFORMATION_MESSAGE)
            val dialogWindow = dialog.createDialog(owner, "단축키 변경")
            dialogWindow.isModal = false
            dialogWindow.isVisible = true

            GlobalScreen.addNativeKeyListener(object : NativeKeyListener {
                override fun nativeKeyPressed(e: NativeKeyEvent) {
                    currentShortcutKeyCode = e.keyCode
                    dialogWindow.dispose()
                    updateButtonText()
                    GlobalScreen.removeNativeKeyListener(this)
                }

                override fun nativeKeyReleased(e: NativeKeyEvent) {}
                override fun nativeKeyTyped(e: NativeKeyEvent) {}
            })
        }
    }

    private fun updateButtonText() {
        val keyText = NativeKeyEvent.getKeyText(currentShortcutKeyCode)
        onShortcutLabelChanged(keyText)
    }
}

