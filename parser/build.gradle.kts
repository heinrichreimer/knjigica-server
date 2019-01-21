import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":model"))
    implementation(project(":json-support"))

    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.0.1")

    testImplementation("junit:junit:4.12")
    testImplementation("org.amshove.kluent:kluent:1.4")

    implementation("com.squareup.okio:okio:2.1.0")
    implementation("org.jsoup:jsoup:1.11.3")
    implementation("com.squareup.moshi:moshi:1.7.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.7.0")
    implementation("net.gcardone.junidecode:junidecode:0.4.1")
}
