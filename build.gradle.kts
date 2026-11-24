import java.io.ByteArrayOutputStream
import java.io.File
import javax.inject.Inject
import org.gradle.process.ExecOperations

plugins {
    kotlin("jvm") version "2.2.21"
    `maven-publish`
}

group = "com.axclogo"
version = "0.1.0-SNAPSHOT"

repositories { mavenCentral() }

kotlin {
    jvmToolchain(17)
    explicitApi()
}
java { withSourcesJar() }

dependencies { testImplementation(kotlin("test-junit5")); testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.12.2") }
tasks.test { useJUnitPlatform() }

publishing {
    publications {
        create<MavenPublication>("library") {
            from(components["java"])
            artifactId = "axc-bedrock"
            pom {
                name.set("AxcBedrock for Kotlin")
                description.set("Typed .axc namespaces for a small Kotlin/JVM core.")
                url.set("https://github.com/axclogo/AxcBedrock-Kotlin")
                licenses { license { name.set("MIT"); url.set("https://opensource.org/license/mit") } }
                developers { developer { id.set("axclogo"); name.set("Axclogo") } }
                scm { url.set("https://github.com/axclogo/AxcBedrock-Kotlin") }
            }
        }
    }
}

// 编译期正反例也走真实编译器，不靠运行时断言替代类型检查。
val typeCheckCompiler by configurations.creating
dependencies {
    add(typeCheckCompiler.name, "org.jetbrains.kotlin:kotlin-compiler-embeddable:2.2.21")
    testImplementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
}
abstract class TypeContractCheck : DefaultTask() {
    @get:Inject abstract val execOperations: ExecOperations
}
val checkTypes by tasks.registering(TypeContractCheck::class) {
    dependsOn(tasks.jar)
    inputs.dir("tool/type-contracts")
    inputs.files(configurations.runtimeClasspath)
    doLast {
        val jar = tasks.jar.get().archiveFile.get().asFile
        val cases = file("tool/type-contracts").listFiles()!!.filter { it.extension == "kt" }
            .sortedBy { if (it.name.startsWith("pass_")) "0" else it.name }
        for (source in cases) {
            val output = ByteArrayOutputStream()
            val result = execOperations.javaexec {
                classpath = typeCheckCompiler
                mainClass.set("org.jetbrains.kotlin.cli.jvm.K2JVMCompiler")
                args("-no-stdlib", "-no-reflect", "-jvm-target", "17", "-classpath",
                    jar.path + File.pathSeparator + configurations.runtimeClasspath.get().asPath,
                    "-d", layout.buildDirectory.dir("type-contracts/${source.nameWithoutExtension}").get().asFile.path,
                    source.path)
                isIgnoreExitValue = true
                standardOutput = output
                errorOutput = output
            }
            val shouldCompile = source.name.startsWith("pass_")
            check((result.exitValue == 0) == shouldCompile) { "${source.name}: $output" }
            if (!shouldCompile) check(output.toString().contains("error:")) { output.toString() }
            logger.lifecycle("PASS: ${source.name}")
        }
    }
}
tasks.check { dependsOn(checkTypes) }
