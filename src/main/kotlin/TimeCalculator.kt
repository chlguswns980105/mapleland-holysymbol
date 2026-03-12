import java.time.LocalDateTime

object TimeCalculator {
    fun getSuggestedStartTime(now: LocalDateTime = LocalDateTime.now()): LocalDateTime {
        // 45초 이상이면 다음 분 입력이 편하도록 약간 보정한다.
        return now.plusSeconds(if (now.second >= 45) 15 else 0)
    }

    fun formatTwoDigits(number: Int): String {
        return String.format("%02d", number)
    }
}

