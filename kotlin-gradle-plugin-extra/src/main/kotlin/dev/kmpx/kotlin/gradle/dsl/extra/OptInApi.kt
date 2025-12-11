package dev.kmpx.kotlin.gradle.dsl.extra

abstract class OptInApi {
    abstract val id: String
}

sealed class StdlibOptInApi : OptInApi() {
    data object ExperimentalJsCollectionsApi : StdlibOptInApi() {
        override val id = "kotlin.js.ExperimentalJsCollectionsApi"
    }
}
