import org.gradle.api.artifacts.dsl.RepositoryHandler

fun RepositoryHandler.configureRepositories() {
    mavenCentral()
}
