<!---Header--->
[![Android CI](https://github.com/renetik/renetik-android-preset/workflows/Android%20CI/badge.svg)
](https://github.com/renetik/renetik-android-preset/actions/workflows/android.yml)

# Renetik Android - Preset

#### [https://github.com/renetik/renetik-android-preset](https://github.com/renetik/renetik-android-preset/)

#### [Documentation](https://renetik.github.io/renetik-android-preset/)

Framework to enjoy, improve and speed up your application development while writing readable code.
Used as library in music production and performance app Renetik Instruments www.renetik.com as well
as in other projects.

```gradle
allprojects {
    repositories {
        // For master-SNAPSHOT
        maven { url 'https://github.com/renetik/maven-snapshot/raw/master/repository' }
        // For release builds
        maven { url 'https://github.com/renetik/maven/raw/master/repository' }
    }
}
```

Step 2. Add the dependency

```gradle
dependencies {
    implementation 'com.renetik.library:renetik-android-preset:$renetik-android-version'
}
```
