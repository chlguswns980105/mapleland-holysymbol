fun main() {
    val window = createMainWindow()
    val timerController = TimerController(window)

    val shortcutManager = GlobalShortcutManager(
        onRefreshShortcut = {
            if (window.refreshButton.isVisible) {
                window.refreshButton.doClick()
            }
        },
        onShortcutLabelChanged = { keyText ->
            window.changeKeyButton.text = "갱신 단축키 변경 (현재 $keyText)"
        }
    )

    window.startButton.addActionListener { timerController.runTimer() }
    window.endButton.addActionListener { timerController.stopTimer() }
    window.refreshButton.addActionListener { timerController.runTimer() }
    window.startTimeButton.addActionListener { timerController.updateStartTime() }

    shortcutManager.register()
    shortcutManager.bindChangeShortcutAction(window.frame, window.changeKeyButton)
}