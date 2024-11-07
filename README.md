# PnP Logbook
Provides a logbook for Pen & Paper games. Current features are:
* Logbook: Capture every plot twist and crucial clue along your journey
* Experience: Keep a record of the gained experience points. Never miss the level up
* Currency: Track every coin you earn and spend. Even one copper counts!
* Persons, groups and places: A catalog of everyone you've met, every faction you've crossed, and every place you've been

The application is built with Ktor and Kotlin Multiplatform.
It consists of a server providing a REST API and clients for Android, iOS, web and desktop.

## Disclaimer
The features work and can be used as intended. Nevertheless, it's primarily a demo to showcase the
client and server-side components of Ktor and Kotlin Multiplatform.
Therefore, some features might exist to enhance the showcase and aren't intended for production use.
For example, there is an easy option to shut down the server from the client.
Also, the security of the client and server should be improved before deploying it in production.
Please keep those limitations in mind when using the project.

## Project structure
This is a Kotlin Multiplatform project targeting Android, iOS, web, desktop and server.

* `/client` is for code that will be shared across your Compose Multiplatform client applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want specific code for iOS then put it in `iosMain`.

* `/iosApp` contains iOS applications/XCode project. Even if you’re sharing your UI with Compose Multiplatform, 
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

* `/server` is for the Ktor server application.

* `/shared` is for the code that will be shared between all targets in the project.
  The most important subfolder is `commonMain`. If preferred, you can add code to the platform-specific folders here too.

## Contribution
Feel free to contribute via pull requests.

## License
The project is licensed by the [Apache 2 license](LICENSE).