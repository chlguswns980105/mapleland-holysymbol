import java.awt.Color
import java.awt.FlowLayout
import java.awt.Font
import javax.swing.Box
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
    val leftWyvernKeyButton: JButton,
    val middleWyvernKeyButton: JButton,
    val rightWyvernKeyButton: JButton
)

fun createMainWindow(): MainWindow {
    val frame = JFrame("타이머")
    val inputField = JTextField(10).apply { text = "120" }
    val remainLabel = JLabel("남은 시간: ")
    val refreshButton = JButton("갱신하기").apply {
        isVisible = false
        font = Font(font.name, Font.PLAIN, 12)
        preferredSize = java.awt.Dimension(100, 35)
        foreground = Color.BLACK
    }
    val changeKeyButton = JButton("").apply {
        font = Font(font.name, Font.PLAIN, 11)
        preferredSize = java.awt.Dimension(140, 32)
        foreground = Color.BLACK
    }

    val startHour = JTextField("00", 2)
    val startMin = JTextField("00", 2)

    val callSymCheck = JCheckBox("끝날 때 ㄱㄱ복사", null, true)
    val forceFocus = JCheckBox("끝날 때 메랜 포커스", null, true)

    val startButton = JButton("시작").apply {
        font = Font(font.name, Font.PLAIN, 12)
        preferredSize = java.awt.Dimension(60, 32)
        foreground = Color.BLACK
    }
    val endButton = JButton("종료").apply {
        font = Font(font.name, Font.PLAIN, 12)
        preferredSize = java.awt.Dimension(60, 32)
        foreground = Color.BLACK
    }
    val startTimeButton = JButton("시작시간 갱신").apply {
        font = Font(font.name, Font.PLAIN, 11)
        preferredSize = java.awt.Dimension(100, 32)
        foreground = Color.BLACK
    }

    val leftWyvernField = JTextField("0", 5)
    val middleWyvernField = JTextField("0", 5)
    val rightWyvernField = JTextField("0", 5)

    val leftWyvernKeyButton = JButton("좌 (F9)").apply {
        font = Font(font.name, Font.PLAIN, 10)
        preferredSize = java.awt.Dimension(100, 28)
        foreground = Color.BLACK
    }
    val middleWyvernKeyButton = JButton("중 (F10)").apply {
        font = Font(font.name, Font.PLAIN, 10)
        preferredSize = java.awt.Dimension(100, 28)
        foreground = Color.BLACK
    }
    val rightWyvernKeyButton = JButton("우 (F11)").apply {
        font = Font(font.name, Font.PLAIN, 10)
        preferredSize = java.awt.Dimension(100, 28)
        foreground = Color.BLACK
    }

    val numberFields = listOf(
        inputField,
        startHour,
        startMin,
        leftWyvernField,
        middleWyvernField,
        rightWyvernField,
    )

    numberFields.forEach {
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

    val wyvernCounterRow = JPanel(FlowLayout(FlowLayout.LEFT, 5, 0)).apply {
        add(JLabel("좌:"))
        add(leftWyvernField)
        add(JLabel("중:"))
        add(middleWyvernField)
        add(JLabel("우:"))
        add(rightWyvernField)
    }

    val wyvernButtonRow2 = JPanel(FlowLayout(FlowLayout.LEFT, 5, 0)).apply {
        add(leftWyvernKeyButton)
        add(middleWyvernKeyButton)
        add(rightWyvernKeyButton)
    }

    val wyvernPanel = JPanel().apply {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        add(wyvernCounterRow)
        add(wyvernButtonRow2)
        add(Box.createVerticalGlue())
    }

    val tabbedPane = JTabbedPane().apply {
        addTab("타이머", timerPanel)
        addTab("와이번", wyvernPanel)
    }

    frame.add(tabbedPane)
    frame.setSize(360, 220)
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
        leftWyvernKeyButton = leftWyvernKeyButton,
        middleWyvernKeyButton = middleWyvernKeyButton,
        rightWyvernKeyButton = rightWyvernKeyButton
    )
}
