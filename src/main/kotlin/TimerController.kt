import javax.swing.Timer

class TimerController(
    private val window: MainWindow,
    private val soundPlayer: SoundPlayer = SoundPlayer(),
    private val clipboardService: ClipboardService = ClipboardService(),
    private val focusService: MapleStoryFocusService = MapleStoryFocusService()
) {
    private var timer: Timer? = null

    fun runTimer() {
        timer?.stop()
        var remainingTime = window.inputField.text.toIntOrNull()

        if (remainingTime != null && remainingTime > 0) {
            window.remainLabel.text = "남은 시간: $remainingTime 초"
            window.refreshButton.isVisible = false

            timer = Timer(1000) {
                remainingTime--
                if (remainingTime > 0) {
                    window.remainLabel.text = "남은 시간: $remainingTime 초"
                } else {
                    window.remainLabel.text = ""
                    soundPlayer.play("kwarosa.wav")
                    window.refreshButton.isVisible = true
                    timer?.stop()

                    if (window.callSymCheck.isSelected) {
                        clipboardService.copyCallSymbolText(window.startHour.text, window.startMin.text)
                    }

                    if (window.forceFocus.isSelected) {
                        focusService.focusMapleStoryWindow()
                    }
                }
            }.apply { start() }
        } else {
            window.remainLabel.text = "숫자를 입력해주세요!"
        }
    }

    fun stopTimer() {
        timer?.stop()
        window.remainLabel.text = "남은 시간: "
        window.refreshButton.isVisible = false
    }

    fun updateStartTime() {
        val startTime = TimeCalculator.getSuggestedStartTime()
        window.startHour.text = TimeCalculator.formatTwoDigits(startTime.hour)
        window.startMin.text = TimeCalculator.formatTwoDigits(startTime.minute)
    }
}

