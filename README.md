# README

## 1. Overal design:

This app is built using the following software development principles:
- Single Activity + multiple Fragments (due to the scope of the requirement, only one fragment is developed).
- MVVM architect + Repository pattern and local database as caching layer.
- Upon receiving a new search input, the forecasts for that search and the input itself will be saved to the local database. In subsequent search, if the input already exists, the corresponding forecasts will be pulled from the database to display.
- A simple refresh mechanism is employed to replace old forecasts data with new ones if the difference between current and last search of a particular input is more than 1 day.
- The entity relationship diagram for said relation is the following:

```
-----------------------          ----------------------------
| CityEntity          |          | ForecastEntity           |
|---------------------|         /|--------------------------|
| cityID: Int         |--------<-| forecastID: Int          |
| name: String        |         \| forecastCityID: Int      |
| searchInput: String |          | date: Long               |
| lastUpdate: Long    |          | temperature: Temperature |
-----------------------          | pressure: Int            |
                                 | humidity: Int            |
                                 | description: String      |
                                 ----------------------------

```

## 2. Folder structure and libraries/frameworks:

#### Structure

- `base/`: contains all the base classes for the app main components (Activity, Fragment, ViewModel) where they will handle common operation such as error handling, showing error message, displaying loading status.
- `constant/`: contains globally used constants.
- `data/mapper/`: contains class that does mapping between database Entities and network Models.
- `data/source/`: contains the repository that will bridge local and remote data sources together.
- `data/source/local/`: contains all database related stuffs including Entities and DAOs.
- `data/source/remote/`: contains everything that's related to network operation (api service client, models, responses, etc).
- `logger/`: contains everything that's required to log in app.
- `screen/`: contains the main UI of our app.
- `util/`: contains commonly used utility classes.

#### Libraries, frameworks:
- Navigation Component: because the scope of this example, this component is not fully demonstrate its benefits.
- Constraint Layout: as the main layout tool.
- Retrofit 2: as the main networking tool for API call.
- Kotlin Coroutine: to handle asynchronous processes.
- Room: as the main client for local database.
- Timber: as the main logging tool to prevent accidentally leaking sensitive debug info in release build.
- Sqlcipher: as the encryption/ decryption tool for our local database.
- NDK: to hide our secret app ID, passwords, etc.

## 3. Run instruction:

This project is built in this environment:
- Mac OS High Sierra 10.13.6.
- Android Studio 4.1.1.
- Compile SDK version 29.
- Build tool version 29.0.3.
- Target SDK version 28 (for robolectric to run).
- NDK version 22.0.7026061.
- CMake version 3.10.2.4988404.
- Android Virtual Device: x86, Android 10.

It should run fine using a similar environment.

## 4. Check list of finished items:

- [x] 1. Programming language: Kotlin is required, Java is optional.
- [x] 2. Design app's architecture (suggest MVVM)
- [x] 3. Apply LiveData mechanism
- [x] 4. UI should be looks like in attachment.
- [x] 5. Write Unit Tests
- [x] 6. Acceptance Tests
- [x] 7. Exception handling
- [x] 8. Caching handling
- [x] 9. Secure Android app from:
  - [x] a. Decompile APK
  - [x] b. Rooted device
  - [ ] c. Data transmission via network
  - [x] d. Encryption for sensitive information
- [x] 10.Accessibility for Disability Supports:
  - [x] a. Talkback: Use a screen reader.
  - [x] b. Scaling Text: Display size and font size: To change the size of items on your screen,
adjust the display size or font size.
