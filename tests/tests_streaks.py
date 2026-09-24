# tests/test_streaks.py

from pulsequest.streaks import update_streak, streak_bonus


def test_streak_increases():
    assert update_streak(4, True) == 5


def test_streak_resets():
    assert update_streak(10, False) == 0


def test_three_day_bonus():
    assert streak_bonus(3) == 25


def test_seven_day_bonus():
    assert streak_bonus(7) == 100


def test_fourteen_day_bonus():
    assert streak_bonus(14) == 200


def test_thirty_day_bonus():
    assert streak_bonus(30) == 500


def test_no_bonus_for_new_streak():
    assert streak_bonus(2) == 0
