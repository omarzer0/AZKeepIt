# AZKeepIt

## Jetpack Compose Note Keeping app


## Preview
<table align="center">
  <tr>
    <td> 
      <video src ="https://user-images.githubusercontent.com/55766997/215361330-618d295e-ee02-4cee-8666-79251ea24ab3.mp4" width="150"/>
    </td>
    <td>
      <video src ="https://user-images.githubusercontent.com/55766997/215361340-4d9c1829-b27a-4024-a624-df2343d256ce.mp4" width="150"/>
    </td>
    <td>
      <video src ="https://user-images.githubusercontent.com/55766997/215361351-fad8cc8f-29f6-4326-a33d-3cc18d7c6809.mp4" width="150"/>
    </td>
  </tr>
</table>

## Features:
  - Note:
    - Create notes and Save them locally
    - Add Title and Content
    - Select note color
    - Lock Note with password
    - Add Images to note
    - Delete Note
    - Add or Remove Note from Folder (Note has 0 or 1 Folder "One to one relation")
    - Save changes when back button pressed
    - Doesn't save empty note
   
  - Folder:
    - Create Folders to group notes
    - Delete Folder
    - Rename Folder 
    - Delete all notes form a Folder
    - Add Mutliple notes to Folder (Folder has 0 to many Notes "One to many relation")

  - Search:
    - Search Notes by either title or content

## Tech stack & Open-source libraries
- Minimum SDK level 21
- [Kotlin](https://kotlinlang.org/) based, [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) together with [Flow](https://developer.android.com/kotlin/flow) for asynchronous streams and one side viewModel communication.
- [Dagger Hilt](https://developer.android.com/training/dependency-injection/hilt-android) for dependency injection.
- [DateTimeFormatter](https://developer.android.com/reference/java/time/format/DateTimeFormatter) for parsing date-time objects.
  
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
