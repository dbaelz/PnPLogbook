# PnP Logbook
Provides a logbook for Pen & Paper games. Intended feature are
* Experience point and currency tracking
* Logbook for sessions to track story relevant events
* Persons, groups and places directory

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
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, 
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

* `/server` is for the Ktor server application.

* `/shared` is for the code that will be shared between all targets in the project.
  The most important subfolder is `commonMain`. If preferred, you can add code to the platform-specific folders here too.

## Contribution
Feel free to contribute via pull requests.

## License
The project is licensed by the [Apache 2 license](LICENSE).