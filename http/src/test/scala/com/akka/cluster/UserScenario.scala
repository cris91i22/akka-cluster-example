package com.akka.cluster

import io.gatling.core.scenario.Simulation
import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class UserScenario extends Simulation {

  val httpConf = http
    .baseURL("http://0.0.0.0:9000")

  val scn = scenario("User")

//    .exec(http("homepage_GET")
//      .get("/user")
//      .header("Content-Type", "application/json")
//    )


    .exec(http("create")
      .post("/user")
      .body(StringBody("""{"name": "a", "genre": "b"}"""))
      .header("Content-Type", "application/json")
      .check(jsonPath("id").saveAs("id"))
    )


  setUp(
    scn.inject(
      atOnceUsers(100),
      rampUsers(150) over (5 seconds)
    )
  ) maxDuration 5000 protocols httpConf

}
