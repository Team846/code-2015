enablePlugins(FRCPlugin)

organization := "com.team846"

name := "junky-monkey"

version := "0.1-SNAPSHOT"

javaSource in Compile := baseDirectory.value / "src"
javaSource in Test := baseDirectory.value / "test"

teamNumber := 846

robotClass := "com.team846.robot.LRT15Robot"

autoScalaLibrary := false