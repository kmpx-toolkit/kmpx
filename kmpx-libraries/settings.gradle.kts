rootProject.name = "kmpx-libraries"

pluginManagement {
    includeBuild("build-logic") {
        name = "kmpx-libraries-build-logic"
    }
}

include(
    "collections",
    "collections:data-structures",
    "js-api-compat",
    "platform",
)
