import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

class ClipboardService {
    private val clipboard = Toolkit.getDefaultToolkit().systemClipboard

    fun copyText(text: String) {
        val selection = StringSelection(text)
        clipboard.setContents(selection, null)
    }

    fun copyCallSymbolText(startHour: String, startMin: String) {
        val hour = "${startHour}시"
        val min = "${startMin}분"
        val text = "$hour ${min}시작ㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱ"
        this.copyText(text + System.currentTimeMillis().toString())
    }
}

