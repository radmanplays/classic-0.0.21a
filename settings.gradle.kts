import java.io.File

pluginManagement {
	repositories {
		gradlePluginPortal()
		maven {
			name = "eagler-teavm"
			url = uri("https://eaglercraft-teavm-fork.github.io/maven/")
		}
		maven {
			name = "eagler-local"
			url = uri(File(rootDir, "gradle/local-libs"))
		}
		mavenCentral()
	}
}

rootProject.name = "Classic 0.0.20_2"

include("target_lwjgl_desktop")
include("target_teavm_javascript")
include("target_teavm_wasm_gc")