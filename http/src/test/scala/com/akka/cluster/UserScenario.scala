package com.akka.cluster

import io.gatling.core.scenario.Simulation
import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class UserScenario extends Simulation {

  val httpConf = http
    .baseURL("http://localhost:9000")

  val scn = scenario("User")
    .exec(http("create")
      .post("/user")
      .body(StringBody("""{"name": "a", "genre": "b"}"""))
      .header("Content-Type", "application/json")
      .check(jsonPath("id").saveAs("userId"), status.is(200))
    )


  setUp(
    scn.inject(
      atOnceUsers(100),
      rampUsers(150) over (5 seconds)
    )
  ) maxDuration 5000 protocols httpConf

}
