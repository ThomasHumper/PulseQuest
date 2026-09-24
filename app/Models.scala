package pulsequest

import zio.json.*

final case class User(
    id: Long,
    username: String,
    email: String,
    xp: Int,
    level: Int
) derives JsonCodec

final case class Quest(
    id: Long,
    title: String,
    description: String,
    xpReward: Int,
    completed: Boolean
) derives JsonCodec

final case class Achievement(
    id: Long,
    name: String,
    description: String
) derives JsonCodec

final case class CompleteQuestResponse(
    user: User,
    quest: Quest,
    xpEarned: Int,
    levelUp: Boolean
) derives JsonCodec
