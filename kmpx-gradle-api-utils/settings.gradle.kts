rootProject.name = "kmpx-gradle-api-utils"

pluginManagement {
    includeBuild("build-logic")
}

include(
    "gradle-core-api-utils",
    "gradle-kotlin-api-utils",
)
