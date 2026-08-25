allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

val newBuildDir: Directory =
    rootProject.layout.buildDirectory
        .dir("../../build")
        .get()
rootProject.layout.buildDirectory.value(newBuildDir)

subprojects {
    val newSubprojectBuildDir: Directory = newBuildDir.dir(project.name)
    project.layout.buildDirectory.value(newSubprojectBuildDir)
}

// ❌ 删除这一行！它是导致 generateLockfiles 任务重复注册的元凶
// subprojects {
//     project.evaluationDependsOn(":app")
// }

// ✅ 安全地为所有 Android 子模块统一 Java 和 Kotlin 编译目标为 17
subprojects {
    pluginManager.withPlugin("com.android.library") {
        configureAndroidJavaTarget()
    }
    pluginManager.withPlugin("com.android.application") {
        configureAndroidJavaTarget()
    }
}

fun Project.configureAndroidJavaTarget() {
    extensions.findByType<com.android.build.gradle.BaseExtension>()?.apply {
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}
