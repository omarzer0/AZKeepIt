# AZKeepIt

## Tech stack & Open-source libraries
- Minimum SDK level 21
- [Kotlin](https://kotlinlang.org/) based, [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) together with [Flow](https://developer.android.com/kotlin/flow) for asynchronous streams and one side viewModel to fragment communication.
- [Dagger Hilt](https://developer.android.com/training/dependency-injection/hilt-android) for dependency injection.
  
- Compose:
  - [Jetpack Compose](https://developer.android.com/jetpack/compose/documentation) for the UI.
  - [Compose Destination](https://composedestinations.rafaelcosta.xyz) KSP library that hides the complex, non-type-safe and boilerplate code you would have to write otherwise.
  - [Compose Pager](https://google.github.io/accompanist/pager/) Provides paging layouts for Jetpack Compose.
  - [Accompanist](https://github.com/google/accompanist): A group of libraries that aim to supplement Jetpack Compose with features that are commonly required by developers 
  - [Coil Compose](https://coil-kt.github.io/coil): An image loading library for Android backed by Kotlin Coroutines for loading async images for compose.
  - [System UI controller](https://google.github.io/accompanist/systemuicontroller/) Provides easy-to-use utilities for updating the System UI bar colors within Jetpack Compose.
  - [Extended Icons](https://developer.android.com/reference/kotlin/androidx/compose/material/icons/package-summary) Provides a full set of Material icons to be used with Compose.
  
- JetPack:
  - Lifecycle - Dispose of observing data when the lifecycle state changes.
  - ViewModel - UI-related data holder, lifecycle aware.
  - Room: Persistence library that provides an abstraction layer over SQLite database.
  - SaveStateHandler to handle process death and pass data between composables.


- Architecture:
  - Clean Architecture divided into three layers (Data, Domain and UI).
  - [MVI Architecture (Model-View-Intent)](https://www.raywenderlich.com/817602-mvi-architecture-for-android-tutorial-getting-started).
  - Repository pattern.
  - Use ViewModels to handle Non-UI actions and Expose only one UI state for each screen to have single source of truth.
  - UI passes actions to ViewModels and observe on single UI state.
