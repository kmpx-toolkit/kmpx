package dev.kmpx.gradle.kotlin.dsl.utils

sealed class StdlibOptInApi : OptInApi() {
    /**
     * Experimental JS-collections API
     *
     * https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.js/-experimental-js-collections-api/
     */
    data object ExperimentalJsCollectionsApi : StdlibOptInApi() {
        override val qualifiedMarkerName = "kotlin.js.ExperimentalJsCollectionsApi"
    }

    /**
     * Experimental Time API
     *
     * https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.time/-experimental-time/
     */
    data object ExperimentalTimeApi : StdlibOptInApi() {
        override val qualifiedMarkerName = "kotlin.time.ExperimentalTime"
    }

    /**
     * Experimental Unsigned Types API
     *
     * https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-experimental-unsigned-types/
     */
    data object ExperimentalUnsignedTypesApi : StdlibOptInApi() {
        override val qualifiedMarkerName = "kotlin.ExperimentalUnsignedTypes"
    }

    /**
     * Experimental Path API
     *
     * https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.io.path/-experimental-path-api/
     */
    data object ExperimentalPathApi : StdlibOptInApi() {
        override val qualifiedMarkerName = "kotlin.io.path.ExperimentalPathApi"
    }

    /**
     * Experimental Contracts API
     *
     * https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.contracts/-experimental-contracts/
     */
    data object ExperimentalContractsApi : StdlibOptInApi() {
        override val qualifiedMarkerName = "kotlin.contracts.ExperimentalContracts"
    }
}
