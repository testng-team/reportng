# How to do the release

* Update `~/.gradle/gradle/properties` with :

```
sonatypeUser=<user id>
sonatypePassword=<password>
```

* Update `build.gradle.kts` and change `version` to a released version

* Now from command prompt the following three commands can be executed to do a release:

```
./gradlew clean assemble publish
```

Then go to [the repository](https://oss.sonatype.org/index.html#stagingRepositories) to close and release the distribution.

