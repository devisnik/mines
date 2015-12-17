### Prerequisites

- CocoaPods [https://cocoapods.org/], used version 0.39.0
- j2objc [https://github.com/google/j2objc], used version 0.9.8.2.1

### Setup for iOS project

- set-up j2objc installation via `local.properties`
- run `./gradlew -p model clean build` from root-folder to generate the iOS model
- run `pod update` from ios-folder to update pods
- open project via `Mines.xcworkspace`

### Troubleshooting

- close Xcode project
- remove `Podfile`, `Podfile.lock` and `Pods/`
- run `pod init` from ios folder
- continue with usual setup

