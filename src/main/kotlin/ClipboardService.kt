import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

class ClipboardService {
    private val clipboard = Toolkit.getDefaultToolkit().systemClipboard

    fun copyCallSymbolText(startHour: String, startMin: String) {
        val hour = "${startHour}시"
        val min = "${startMin}분"
        val text = "$hour ${min}시작ㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱ"
        val selection = StringSelection(text + System.currentTimeMillis().toString())
        clipboard.setContents(selection, null)
    }
}

