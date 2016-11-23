package com.akka.cluster

import io.gatling.core.scenario.Simulation
import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class UserScenario extends Simulation {

  val httpConf = http
    .baseURL("http://127.0.0.1:9000")

  val scn = scenario("User")
    .exec(http("create")
      .post("/user")
      .body(StringBody("""{"name": "a", "genre": "b"}"""))
      .header("Content-Type", "application/json")
      .check(jsonPath("$.id").saveAs("userId"), status.is(201))
    ).exec(session => {
      val maybeId = session.get("userId").asOption[String]
      http("create")
        .get(s"/user/${maybeId.get}")
        .check(status.is(200))
      session
    })

  setUp(
    scn.inject(
      atOnceUsers(100),
      rampUsers(150) over (5 seconds)
    )
  ) maxDuration 5000 protocols httpConf

}
