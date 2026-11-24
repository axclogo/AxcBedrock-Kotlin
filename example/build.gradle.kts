plugins { kotlin("jvm") version "2.2.21"; application }
repositories {
    mavenLocal { content { includeModule("com.axclogo", "axc-bedrock") } }
    mavenCentral()
}
dependencies { implementation("com.axclogo:axc-bedrock:0.1.0-SNAPSHOT") }
kotlin { jvmToolchain(17) }
application { mainClass.set("MainKt") }
