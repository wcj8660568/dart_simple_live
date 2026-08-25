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

subprojects {
    project.evaluationDependsOn(":app")
}

// ✅ 安全地为所有 Android 子模块统一 Java 和 Kotlin 编译目标为 17
// 使用 pluginManager.withPlugin 确保只在 AGP 已应用的模块上操作
// 这样不会破坏 Android SDK classpath 配置
subprojects {
    // 当 com.android.library 或 com.android.application 插件被应用时
    pluginManager.withPlugin("com.android.library") {
        configureAndroidJavaTarget()
    }
    pluginManager.withPlugin("com.android.application") {
        configureAndroidJavaTarget()
    }
}

fun Project.configureAndroidJavaTarget() {
    // 通过 android extension 设置 compileOptions（这是 AGP 的标准 API，不会破坏 classpath）
    extensions.findByType<com.android.build.gradle.BaseExtension>()?.apply {
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
    }

    // 同时强制 Kotlin 编译目标为 17
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}
