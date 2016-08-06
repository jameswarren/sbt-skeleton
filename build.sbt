name := "neophyte"

organization in ThisBuild := "jameswarren"

version in ThisBuild := "0.0.1"

isSnapshot := true

version in ThisBuild <<= (version in ThisBuild, isSnapshot) { (v,s) => if (s) { v + "-SNAPSHOT" } else v }

scalaVersion in ThisBuild := "2.10.6"

scalacOptions in ThisBuild += "-feature"

credentials in ThisBuild += Credentials(Path.userHome / ".ivy2" / ".credentials")


def defProject(name: String, directory: String): Project = {
  val mergeSuffixes = List(".RSA", ".xsd", ".dtd", ".properties")

  Project(name, file(directory)).
    configs(IntegrationTest).
    settings(Defaults.itSettings: _*).
    settings(
      libraryDependencies ++= Seq(
        "org.scalatest" %% "scalatest" % "2.2.4" % "it, test",
        "org.pegdown" % "pegdown" % "1.6.0" % "test",
        "org.mockito" % "mockito-all" % "1.10.19" % "test"
      ),
      testOptions in Test += Tests.Argument("-h", (target.value / "test-reports-html").absolutePath),
      test in assembly := {},
      assemblyMergeStrategy in assembly := {
        case PathList(ps @ _*) if mergeSuffixes.exists(ps.last.endsWith) => MergeStrategy.first
        case x =>
          val oldStrategy = (assemblyMergeStrategy in assembly).value
          oldStrategy(x)
      },
    )
}

lazy val root = defProject("base", ".")
