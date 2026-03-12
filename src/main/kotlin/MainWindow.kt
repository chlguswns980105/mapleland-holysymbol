import java.awt.FlowLayout
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

data class MainWindow(
    val frame: JFrame,
    val inputField: JTextField,
    val remainLabel: JLabel,
    val refreshButton: JButton,
    val changeKeyButton: JButton,
    val startHour: JTextField,
    val startMin: JTextField,
    val callSymCheck: JCheckBox,
    val forceFocus: JCheckBox,
    val startButton: JButton,
    val endButton: JButton,
    val startTimeButton: JButton
)

fun createMainWindow(): MainWindow {
    val frame = JFrame("타이머")
    val inputField = JTextField(10).apply { text = "120" }
    val remainLabel = JLabel("남은 시간: ")
    val refreshButton = JButton("갱신하기").apply { isVisible = false }
    val changeKeyButton = JButton("")

    val startHour = JTextField("00")
    val startMin = JTextField("00")

    val callSymCheck = JCheckBox("끝날 때 ㄱㄱ복사", null, true)
    val forceFocus = JCheckBox("끝날 때 메랜 포커스", null, true)

    val startButton = JButton("시작")
    val endButton = JButton("종료")
    val startTimeButton = JButton("시작시간 갱신")

    val inputPanel = JPanel(FlowLayout(FlowLayout.LEFT)).apply {
        add(JLabel("설정 시간(초):"))
        add(inputField)
        add(startButton)
        add(endButton)
    }

    val shortcutPanel = JPanel(FlowLayout(FlowLayout.LEFT)).apply {
        add(changeKeyButton)
        add(refreshButton)
        add(remainLabel)
    }

    val checkBoxPanel = JPanel().apply {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        add(callSymCheck)
        add(forceFocus)
    }

    val resultPanel = JPanel(FlowLayout(FlowLayout.LEFT)).apply {
        add(startTimeButton)
        add(startHour)
        add(startMin)
        add(checkBoxPanel)
    }

    val mainPanel = JPanel().apply {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        add(inputPanel)
        add(shortcutPanel)
        add(resultPanel)
    }

    frame.add(mainPanel)
    frame.setSize(350, 170)
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.isVisible = true
    frame.isAlwaysOnTop = true
    frame.toFront()
    frame.requestFocus()

    return MainWindow(
        frame = frame,
        inputField = inputField,
        remainLabel = remainLabel,
        refreshButton = refreshButton,
        changeKeyButton = changeKeyButton,
        startHour = startHour,
        startMin = startMin,
        callSymCheck = callSymCheck,
        forceFocus = forceFocus,
        startButton = startButton,
        endButton = endButton,
        startTimeButton = startTimeButton
    )
}

