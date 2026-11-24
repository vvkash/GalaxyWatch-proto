pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "GalaxyWatchHeart"
include(":core")   // shared code (logging, HRV, the curved button)
include(":hr")     // Heart Rate app
include(":ecg")    // ECG app
include(":bia")    // Body Composition (BIA) app
