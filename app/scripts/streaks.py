# pulsequest/streaks.py

def update_streak(
    current_streak: int,
    completed_today: bool,
) -> int:
    if completed_today:
        return current_streak + 1

    return 0


def streak_bonus(streak: int) -> int:
    if streak >= 30:
        return 500

    if streak >= 14:
        return 200

    if streak >= 7:
        return 100

    if streak >= 3:
        return 25

    return 0
