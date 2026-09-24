module Server where

import API
import Models
import Servant

users :: [User]
users =
  [ User
      (UserId 1)
      "player1"
      "player@example.com"
      350
      2
  ]

quests :: [Quest]
quests =
  [ Quest
      (QuestId 1)
      "Morning Warrior"
      "Complete your morning routine"
      50
      False
  ]

server :: Server API
server =
       return users
  :<|> getUser
  :<|> return quests
  :<|> completeQuestEndpoint

getUser :: Int -> Handler User
getUser uid =
  case filter (\u -> userId u == UserId uid) users of
    (u:_) -> return u
    []    -> throwError err404

completeQuestEndpoint :: Int -> Handler User
completeQuestEndpoint qid =
  case filter (\q -> questId q == QuestId qid) quests of
    (q:_) ->
      case users of
        (u:_) -> return (completeQuest u q)
        []    -> throwError err404

    [] -> throwError err404

package pulsequest

import zio.*

object QuestService:

  private val questsRef =
    Ref.unsafe.make(
      Map(
        1L -> Quest(
          id = 1,
          title = "Morning Warrior",
          description = "Complete your morning routine.",
          xpReward = 50,
          completed = false
        ),

        2L -> Quest(
          id = 2,
          title = "Deep Focus",
          description = "Work distraction-free for 60 minutes.",
          xpReward = 100,
          completed = false
        ),

        3L -> Quest(
          id = 3,
          title = "Knowledge Hunter",
          description = "Learn something new for 30 minutes.",
          xpReward = 75,
          completed = false
        )
      )
    )

  private val usersRef =
    Ref.unsafe.make(
      Map(
        1L -> User(
          id = 1,
          username = "player1",
          email = "player@example.com",
          xp = 0,
          level = 1
        )
      )
    )

  def getQuests: UIO[List[Quest]] =
    questsRef.get.map(_.values.toList)

  def getUser(id: Long): IO[String, User] =
    usersRef.get.flatMap { users =>
      users.get(id) match
        case Some(user) =>
          ZIO.succeed(user)

        case None =>
          ZIO.fail(s"User $id not found")
    }

  def completeQuest(
      userId: Long,
      questId: Long
  ): IO[String, CompleteQuestResponse] =
    for
      quest <- questsRef.get.flatMap { quests =>
        quests.get(questId) match
          case Some(q) if !q.completed =>
            ZIO.succeed(q)

          case Some(_) =>
            ZIO.fail("Quest already completed")

          case None =>
            ZIO.fail("Quest not found")
      }

      user <- getUser(userId)

      updatedUser =
        XpSystem.addXp(user, quest.xpReward)

      updatedQuest =
        quest.copy(completed = true)

      _ <- usersRef.update(_.updated(userId, updatedUser))

      _ <- questsRef.update(
        _.updated(questId, updatedQuest)
      )

    yield CompleteQuestResponse(
      user = updatedUser,
      quest = updatedQuest,
      xpEarned = quest.xpReward,
      levelUp = updatedUser.level > user.level
    )
