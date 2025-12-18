plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.kmpx.gradleKotlinApiUtils)
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
