import Dependencies._

lazy val root = (project in file("."))
  .settings(
    organization := "io.github.rafafrdz",
    name := "stocking-app",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.8",
    libraryDependencies ++= Seq(
      CompilerPlugin.kindProjector,
      CompilerPlugin.betterMonadicFor,
      CompilerPlugin.semanticDB,
      Libraries.cats,
      Libraries.catsEffect,
      Libraries.catsRetry,
      Libraries.circeCore,
      Libraries.circeGeneric,
      Libraries.circeParser,
      Libraries.circeRefined,
      Libraries.cirisCore,
      Libraries.cirisEnum,
      Libraries.cirisRefined,
      Libraries.derevoCore,
      Libraries.derevoCats,
      Libraries.derevoCirce,
      Libraries.http4sDsl,
      Libraries.http4sServer,
      Libraries.http4sClient,
      Libraries.http4sCirce,
      Libraries.doobieCore,
      Libraries.doobiePostgres,
      Libraries.newtype,
    ),
    scalacOptions ++= Seq(
      "-Ymacro-annotations", "-Wconf:cat=unused:info", "-Wunused"
    )
  )
