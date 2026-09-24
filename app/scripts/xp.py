def xp_required(level: int) -> int:
    return 100 * level * level


def calculate_level(xp: int) -> int:
    level = 1

    while xp_required(level + 1) <= xp:
        level += 1

    return level


def add_xp(user: dict, amount: int) -> dict:
    new_xp = user["xp"] + amount
    new_level = calculate_level(new_xp)

    return {
        **user,
        "xp": new_xp,
        "level": new_level,
    }
