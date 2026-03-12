import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
import java.awt.Component
import java.util.logging.Level
import java.util.logging.Logger
import javax.swing.JButton
import javax.swing.JOptionPane

class GlobalShortcutManager {
    private val shortcutKeyCodes = mutableMapOf<String, Int>()
    private val shortcutActions = mutableMapOf<String, () -> Unit>()
    private val shortcutLabelUpdaters = mutableMapOf<String, (String) -> Unit>()
    private val shortcutEnabledPredicates = mutableMapOf<String, () -> Boolean>()

    private val globalKeyListener = object : NativeKeyListener {
        override fun nativeKeyPressed(e: NativeKeyEvent) {
            shortcutKeyCodes.forEach { (shortcutId, keyCode) ->
                val enabled = shortcutEnabledPredicates[shortcutId]?.invoke() ?: true
                if (enabled && e.keyCode == keyCode) {
                    shortcutActions[shortcutId]?.invoke()
                }
            }
        }

        override fun nativeKeyReleased(e: NativeKeyEvent) {}
        override fun nativeKeyTyped(e: NativeKeyEvent) {}
    }

    fun registerShortcut(
        shortcutId: String,
        defaultKeyCode: Int,
        onTriggered: () -> Unit,
        onLabelChanged: (String) -> Unit,
        enabledWhen: () -> Boolean = { true }
    ) {
        shortcutKeyCodes[shortcutId] = defaultKeyCode
        shortcutActions[shortcutId] = onTriggered
        shortcutLabelUpdaters[shortcutId] = onLabelChanged
        shortcutEnabledPredicates[shortcutId] = enabledWhen
    }

    fun register() {
        // JNativeHook 기본 로그를 끈다.
        Logger.getLogger(GlobalScreen::class.java.name).level = Level.OFF
        Logger.getLogger("").handlers.forEach { it.level = Level.OFF }

        if (!GlobalScreen.isNativeHookRegistered()) {
            GlobalScreen.registerNativeHook()
        }

        GlobalScreen.addNativeKeyListener(globalKeyListener)
        refreshAllShortcutLabels()
    }

    fun bindChangeShortcutAction(
        owner: Component,
        changeKeyButton: JButton,
        shortcutId: String,
        dialogTitle: String
    ) {
        changeKeyButton.addActionListener {
            openChangeShortcutDialog(owner, shortcutId, dialogTitle)
        }
    }

    fun bindChangeShortcutDialog(owner: Component, shortcutId: String, dialogTitle: String) {
        openChangeShortcutDialog(owner, shortcutId, dialogTitle)
    }

    private fun openChangeShortcutDialog(owner: Component, shortcutId: String, dialogTitle: String) {
        val dialog = JOptionPane("변경할 키를 눌러주세요.", JOptionPane.INFORMATION_MESSAGE)
        val dialogWindow = dialog.createDialog(owner, dialogTitle)
        dialogWindow.isModal = false
        dialogWindow.isVisible = true

        GlobalScreen.addNativeKeyListener(object : NativeKeyListener {
            override fun nativeKeyPressed(e: NativeKeyEvent) {
                shortcutKeyCodes[shortcutId] = e.keyCode
                dialogWindow.dispose()
                updateShortcutLabel(shortcutId)
                GlobalScreen.removeNativeKeyListener(this)
            }

            override fun nativeKeyReleased(e: NativeKeyEvent) {}
            override fun nativeKeyTyped(e: NativeKeyEvent) {}
        })
    }

    private fun refreshAllShortcutLabels() {
        shortcutKeyCodes.keys.forEach { shortcutId ->
            updateShortcutLabel(shortcutId)
        }
    }

    private fun updateShortcutLabel(shortcutId: String) {
        val keyCode = shortcutKeyCodes[shortcutId] ?: return
        val keyText = NativeKeyEvent.getKeyText(keyCode)
        shortcutLabelUpdaters[shortcutId]?.invoke(keyText)
    }
}
