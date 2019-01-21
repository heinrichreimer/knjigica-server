plugins {
    kotlin("jvm")
}

dependencies {
    compile(kotlin("stdlib-jdk8"))

    implementation(project(":model"))
    implementation(project(":indexer-model"))

    implementation("com.squareup.moshi:moshi:1.7.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.7.0")
}
