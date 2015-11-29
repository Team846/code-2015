enablePlugins(FRCPlugin)

organization := "com.team846"

name := "junky-monkey"

version := "0.1-SNAPSHOT"

javaSource in Compile := baseDirectory.value / "src"
javaSource in Test := baseDirectory.value / "test"

resourceDirectory in Test := baseDirectory.value / "test-resources"

teamNumber := 846

robotClass := "com.team846.robot.LRT15Robot"

autoScalaLibrary := false

libraryDependencies += "com.typesafe" % "config" % "1.3.0"

libraryDependencies += "junit" % "junit" % "4.12" % Test
