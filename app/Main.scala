POST /api/users/1/quests/1/complete

Start the server:
clojure -M -m pulsequest.core

Get all quests:
curl http://localhost:8080/api/quests

Get a player:
curl http://localhost:8080/api/users/1

Complete a quest:
curl -X POST \
  http://localhost:8080/api/users/1/quests/1/complete

The response will contain:
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
    "xp-reward": 50,
    "completed": true
  },
  "xp-earned": 50,
  "level-up": false
}

package pulsequest

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
