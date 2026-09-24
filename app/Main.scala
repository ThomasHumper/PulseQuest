package pulsequest

{
  "user": {
    "id": 1,
    "username": "player1",
    "email": "player@example.com",
    "xp": 50,
    "level": 1
  },
  "quest": {
    "id": 1,
    "title": "Morning Warrior",
    "description": "Complete your morning routine.",
    "xpReward": 50,
    "completed": true
  },
  "xpEarned": 50,
  "levelUp": false
}

import zio.*
import zio.http.*
import zio.json.*

object Main extends ZIOAppDefault:

  val app =
    Routes(
      // GET /api/quests
      Method.GET / "api" / "quests" ->
        handler {
          QuestService.getQuests.map { quests =>
            Response.json(quests.toJson)
          }
        },

      // GET /api/users/:id
      Method.GET / "api" / "users" / long("id") ->
        handler { (id: Long, _: Request) =>
          QuestService.getUser(id).map { user =>
            Response.json(user.toJson)
          }.catchAll { error =>
            ZIO.succeed(
              Response
                .status(Status.NotFound)
                .text(error)
            )
          }
        },

      // POST /api/users/:userId/quests/:questId/complete
      Method.POST / "api" / "users" / long("userId")
        / "quests" / long("questId") / "complete" ->
        handler { (userId: Long, questId: Long, _: Request) =>
          QuestService
            .completeQuest(userId, questId)
            .map(result =>
              Response.json(result.toJson)
            )
            .catchAll { error =>
              ZIO.succeed(
                Response
                  .status(Status.BadRequest)
                  .text(error)
              )
            }
        }
    )

  override val run =
    Server
      .serve(app)
      .provide(
        Server.defaultWithPort(8080)
      )
