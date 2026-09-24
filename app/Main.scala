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
