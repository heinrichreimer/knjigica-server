import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", "1.3.11"))
    }
}
allprojects {
    repositories {
        mavenCentral()
        jcenter()
        maven("https://jitpack.io")
    }

    afterEvaluate {
        tasks.withType<KotlinCompile> {
            kotlinOptions.jvmTarget = "1.8"
        }
    }

}
