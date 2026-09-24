import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class Activity(
    val date: LocalDate,
    val completed: Boolean
)

data class Streak(
    val current: Int,
    val longest: Int,
    val lastActive: LocalDate?
)

fun generateStreak(activities: List<Activity>): Streak {
    val completedDays = activities
        .filter { it.completed }
        .map { it.date }
        .distinct()
        .sorted()

    if (completedDays.isEmpty()) {
        return Streak(
            current = 0,
            longest = 0,
            lastActive = null
        )
    }

    var current = 1
    var longest = 1

    for (i in 1 until completedDays.size) {
        val previous = completedDays[i - 1]
        val currentDay = completedDays[i]

        val difference =
            ChronoUnit.DAYS.between(previous, currentDay)

        if (difference == 1L) {
            current++
        } else {
            current = 1
        }

        longest = maxOf(longest, current)
    }

    return Streak(
        current = current,
        longest = longest,
        lastActive = completedDays.last()
    )
}

fun main() {
    val today = LocalDate.now()

    val activities = listOf(
        Activity(today.minusDays(4), true),
        Activity(today.minusDays(3), true),
        Activity(today.minusDays(2), true),
        Activity(today.minusDays(1), true),
        Activity(today, true)
    )

    val streak = generateStreak(activities)

    println("🔥 Current streak: ${streak.current} days")
    println("🏆 Longest streak: ${streak.longest} days")
    println("📅 Last active: ${streak.lastActive}")
}
