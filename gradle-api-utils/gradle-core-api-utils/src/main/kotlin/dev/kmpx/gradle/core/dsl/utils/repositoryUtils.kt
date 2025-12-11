package dev.kmpx.gradle.core.dsl.utils

import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import java.net.URI

fun RepositoryHandler.mavenCentralSnapshots(
    action: org.gradle.api.Action<MavenArtifactRepository>? = null,
) {
    maven {
        name = "Maven Central (Snapshots)"
        url = URI("https://central.sonatype.com/repository/maven-snapshots/")

        action?.execute(this)
    }
}
