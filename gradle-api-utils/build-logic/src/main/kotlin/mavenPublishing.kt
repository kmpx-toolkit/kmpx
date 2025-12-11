import com.vanniktech.maven.publish.MavenPublishBaseExtension

fun MavenPublishBaseExtension.mavenPublishingDefaults(
    artifactId: String,
    version: String,
    name: String,
    description: String,
    inceptionYear: Int,
) {
    publishToMavenCentral()

    signAllPublications()

    coordinates(
        groupId = groupId,
        artifactId = artifactId,
        version = version,
    )

    pom {
        this.name.set(name)
        this.description.set(description)
        this.inceptionYear.set(inceptionYear.toString())

        url.set("https://kmpx.dev/")

        licenses {
            license {
                this.name.set("The Apache License, Version 2.0")
                this.url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                this.distribution.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        developers {
            developer {
                this.id.set("KMPX")
                this.name.set("KMPX Team")
                this.url.set("https://kmpx.dev/")
            }
        }

        scm {
            this.url.set("https://github.com/kmpx-toolkit/kmpx/")
            this.connection.set("scm:git:git://github.com/kmpx-toolkit/kmpx.git")
            this.developerConnection.set("scm:git:ssh://git@github.com/kmpx-toolkit/kmpx.git")
        }
    }
}
