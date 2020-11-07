import sbt.complete.Parsers.spaceDelimited

lazy val runUsingFilePath = inputKey[Unit]("Run with a file path")
lazy val runUsingCommands = inputKey[Unit]("Run with commands as a string")

runUsingFilePath := Def.inputTaskDyn {
	val args: Seq[String] = spaceDelimited("<arg>").parsed
	println("The arguments to runUsingFilePath task are:")
	args foreach println
	(Compile / runMain).toTask(s""" com.github.amraneze.Main --file "${args.mkString(" ")}"""")
}.evaluated

runUsingCommands := Def.inputTaskDyn {
	val args: Seq[String] = spaceDelimited("<arg>").parsed
	println("The arguments to runUsingCommands task are:")
	args foreach println
	(Compile / runMain).toTask(s""" com.github.amraneze.Main --commands "${args.mkString("")}"""")
}.evaluated