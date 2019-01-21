plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":model"))
    implementation(project(":indexer-model"))
    implementation(project(":parser"))
    implementation(project(":json-support"))

    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.0.1")
    testImplementation("junit:junit:4.12")

    implementation("com.squareup.okio:okio:2.1.0")
    implementation("com.squareup.moshi:moshi:1.7.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.7.0")
    implementation("com.heinrichreimer:elasticsearch-kotlin-dsl:0.1.1")
//    implementation("org.elasticsearch.client:transport:6.5.2")
    implementation("org.elasticsearch.client:elasticsearch-rest-high-level-client:6.5.2")
}
