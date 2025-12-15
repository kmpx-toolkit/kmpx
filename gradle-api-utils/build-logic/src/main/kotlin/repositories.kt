import org.gradle.api.artifacts.dsl.RepositoryHandler

fun RepositoryHandler.repositoriesDefaults() {
    gradlePluginPortal()
}
