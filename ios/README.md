### Setup for iOS project

- run `./gradlew -p model clean build` from root-folder to generate the iOS model
- run `pod update` from ios-folder to update pods
- open project via `Mines.xcworkspace`

#### Troubleshooting

- close Xcode project
- remove `Podfile`, `Podfile.lock` and `Pods/`
- run `pod init` from ios folder
- continue with usual setup

