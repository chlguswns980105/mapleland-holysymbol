import java.awt.FlowLayout
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTabbedPane
import javax.swing.JTextField
import javax.swing.text.AbstractDocument

const val TIMER_TAB_INDEX = 0
const val WYVERN_TAB_INDEX = 1

data class MainWindow(
    val frame: JFrame,
    val tabbedPane: JTabbedPane,
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
    val startTimeButton: JButton,
    val leftWyvernField: JTextField,
    val middleWyvernField: JTextField,
    val rightWyvernField: JTextField,
    val leftWyvernPlusButton: JButton,
    val middleWyvernPlusButton: JButton,
    val rightWyvernPlusButton: JButton,
    val leftWyvernKeyButton: JButton,
    val middleWyvernKeyButton: JButton,
    val rightWyvernKeyButton: JButton
)

fun createMainWindow(): MainWindow {
    val frame = JFrame("타이머")
    val inputField = JTextField(10).apply { text = "120" }
    val remainLabel = JLabel("남은 시간: ")
    val refreshButton = JButton("갱신하기").apply { isVisible = false }
    val changeKeyButton = JButton("")

    val startHour = JTextField("00", 2)
    val startMin = JTextField("00", 2)

    val callSymCheck = JCheckBox("끝날 때 ㄱㄱ복사", null, true)
    val forceFocus = JCheckBox("끝날 때 메랜 포커스", null, true)

    val startButton = JButton("시작")
    val endButton = JButton("종료")
    val startTimeButton = JButton("시작시간 갱신")

    val leftWyvernField = JTextField("0", 5)
    val middleWyvernField = JTextField("0", 5)
    val rightWyvernField = JTextField("0", 5)

    val leftWyvernPlusButton = JButton("좌 +1")
    val middleWyvernPlusButton = JButton("중 +1")
    val rightWyvernPlusButton = JButton("우 +1")

    val leftWyvernKeyButton = JButton("")
    val middleWyvernKeyButton = JButton("")
    val rightWyvernKeyButton = JButton("")

    listOf(inputField, startHour, startMin, leftWyvernField, middleWyvernField, rightWyvernField).forEach {
        (it.document as? AbstractDocument)?.documentFilter = NumericDocumentFilter()
    }

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

    val timerPanel = JPanel().apply {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        add(inputPanel)
        add(shortcutPanel)
        add(resultPanel)
    }

    val leftRow = JPanel(FlowLayout(FlowLayout.LEFT)).apply {
        add(JLabel("좌:"))
        add(leftWyvernField)
        add(leftWyvernPlusButton)
        add(leftWyvernKeyButton)
    }

    val middleRow = JPanel(FlowLayout(FlowLayout.LEFT)).apply {
        add(JLabel("중:"))
        add(middleWyvernField)
        add(middleWyvernPlusButton)
        add(middleWyvernKeyButton)
    }

    val rightRow = JPanel(FlowLayout(FlowLayout.LEFT)).apply {
        add(JLabel("우:"))
        add(rightWyvernField)
        add(rightWyvernPlusButton)
        add(rightWyvernKeyButton)
    }

    val wyvernPanel = JPanel().apply {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        add(leftRow)
        add(middleRow)
        add(rightRow)
    }

    val tabbedPane = JTabbedPane().apply {
        addTab("타이머", timerPanel)
        addTab("와이번", wyvernPanel)
    }

    frame.add(tabbedPane)
    frame.setSize(520, 250)
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.isVisible = true
    frame.isAlwaysOnTop = true
    frame.toFront()
    frame.requestFocus()

    return MainWindow(
        frame = frame,
        tabbedPane = tabbedPane,
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
        startTimeButton = startTimeButton,
        leftWyvernField = leftWyvernField,
        middleWyvernField = middleWyvernField,
        rightWyvernField = rightWyvernField,
        leftWyvernPlusButton = leftWyvernPlusButton,
        middleWyvernPlusButton = middleWyvernPlusButton,
        rightWyvernPlusButton = rightWyvernPlusButton,
        leftWyvernKeyButton = leftWyvernKeyButton,
        middleWyvernKeyButton = middleWyvernKeyButton,
        rightWyvernKeyButton = rightWyvernKeyButton
    )
}
