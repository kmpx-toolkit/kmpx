rootProject.name = "kmpx-gradle-api-utils"

pluginManagement {
    includeBuild("build-logic") {
        name = "kmpx-gradle-api-utils-build-logic"
    }
}

include(
    "gradle-core-api-utils",
    "gradle-kotlin-api-utils",
)
