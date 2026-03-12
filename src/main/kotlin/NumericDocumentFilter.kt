import javax.swing.text.AttributeSet
import javax.swing.text.BadLocationException
import javax.swing.text.DocumentFilter

class NumericDocumentFilter : DocumentFilter() {
    @Throws(BadLocationException::class)
    override fun insertString(
        fb: FilterBypass,
        offset: Int,
        string: String?,
        attr: AttributeSet?
    ) {
        if (string == null) return
        val newText = StringBuilder(fb.document.getText(0, fb.document.length))
            .insert(offset, string)
            .toString()
        if (newText.all { it.isDigit() }) {
            super.insertString(fb, offset, string, attr)
        }
    }

    @Throws(BadLocationException::class)
    override fun replace(
        fb: FilterBypass,
        offset: Int,
        length: Int,
        text: String?,
        attrs: AttributeSet?
    ) {
        val replacement = text ?: ""
        val newText = StringBuilder(fb.document.getText(0, fb.document.length))
            .replace(offset, offset + length, replacement)
            .toString()
        if (newText.all { it.isDigit() }) {
            super.replace(fb, offset, length, text, attrs)
        }
    }
}

