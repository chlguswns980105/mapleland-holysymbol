import com.sun.jna.Pointer
import com.sun.jna.platform.win32.Kernel32
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser

class MapleStoryFocusService {
    fun focusMapleStoryWindow() {
        val user32 = User32.INSTANCE
        val kernel32 = Kernel32.INSTANCE

        val enumProc = object : WinUser.WNDENUMPROC {
            override fun callback(hWnd: WinDef.HWND?, lParam: Pointer?): Boolean {
                if (hWnd != null) {
                    val titleBuffer = CharArray(512)
                    user32.GetWindowText(hWnd, titleBuffer, 512)
                    val title = String(titleBuffer).trim { it <= ' ' }

                    val containsMapleWorlds = title.contains("MapleStory Worlds", ignoreCase = true)
                    val containsMapleland = title.contains("Mapleland", ignoreCase = true)

                    // 메월드, 메랜 둘 다 포함된 창만 대상으로 하여 오탐 포커스를 줄인다.
                    if (containsMapleWorlds && containsMapleland) {
                        user32.ShowWindow(hWnd, WinUser.SW_RESTORE)

                        val foregroundWindow = user32.GetForegroundWindow()
                        val currentThreadId = WinDef.DWORD(kernel32.GetCurrentThreadId().toLong())
                        val foregroundThreadId =
                            WinDef.DWORD(user32.GetWindowThreadProcessId(foregroundWindow, null).toLong())

                        user32.AttachThreadInput(foregroundThreadId, currentThreadId, true)
                        user32.BringWindowToTop(hWnd)
                        user32.SetForegroundWindow(hWnd)
                        user32.AttachThreadInput(foregroundThreadId, currentThreadId, false)

                        return false
                    }
                }
                return true
            }
        }

        user32.EnumWindows(enumProc, null)
    }
}

