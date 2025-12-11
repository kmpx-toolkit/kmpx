plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.vanniktech.mavenPublish.gradlePlugin)
}

gradlePlugin {
    plugins {
        create("buildLogicPlugin") {
            id = "build-logic"
            implementationClass = ""
        }
    }
}
