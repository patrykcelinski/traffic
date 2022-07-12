ThisBuild / version      := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.8"

val IntegrationTest = config("it") extend Test

lazy val root = (project in file("."))
  .settings(
    name := "traffic",
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core"            % "2.7.0",
      "org.typelevel" %% "cats-effect"          % "3.3.12",
      "io.circe"      %% "circe-parser"         % "0.14.2",
      "io.circe"      %% "circe-generic-extras" % "0.14.2",
      "com.lihaoyi"   %% "pprint"               % "0.7.3",
      "org.scalatest" %% "scalatest"            % "3.2.12" % "test",
      "org.scalamock" %% "scalamock"            % "5.2.0"  % "test"
    )
  )
  .settings(Defaults.itSettings: _*)
  .configs(IntegrationTest)
