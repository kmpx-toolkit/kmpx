plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

repositories {
    configureRepositories()
}


kotlin {
    configureKotlin()

    sourceSets {
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
