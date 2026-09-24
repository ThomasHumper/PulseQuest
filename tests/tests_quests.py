# tests/test_quests.py

from pulsequest.xp import add_xp


def complete_quest(user, quest):
    if quest["completed"]:
        raise ValueError("Quest already completed")

    updated_user = add_xp(
        user,
        quest["xp_reward"]
    )

    updated_quest = {
        **quest,
        "completed": True,
    }

    return updated_user, updated_quest


def test_complete_quest():
    user = {
        "id": 1,
        "xp": 0,
        "level": 1,
    }

    quest = {
        "id": 1,
        "title": "Morning Warrior",
        "xp_reward": 50,
        "completed": False,
    }

    updated_user, updated_quest = complete_quest(
        user,
        quest
    )

    assert updated_user["xp"] == 50
    assert updated_user["level"] == 1
    assert updated_quest["completed"] is True


def test_completed_quest_cannot_be_completed_again():
    user = {
        "id": 1,
        "xp": 100,
        "level": 1,
    }

    quest = {
        "id": 1,
        "title": "Morning Warrior",
        "xp_reward": 50,
        "completed": True,
    }

    try:
        complete_quest(user, quest)
        assert False, "Expected ValueError"
    except ValueError as error:
        assert str(error) == "Quest already completed"
