# How to do the release

* Update `deploy/settings.xml` with :

`${env.SONATYPE_USER}` - With your sonatype User ID
`${env.SONATYPE_PASSWORD}` - With your sonatype password
`${your_gpg_passphrase}` - With your GPG passphrase

**Note:** The altered `deploy/settings.xml` SHOULD NOT be checked in.

* Update `pom.xml` and change version to a released version

* Now from command prompt the following three commands can be executed to do a release:

```
mvn clean
mvn --settings deploy/settings.xml release:prepare
mvn --settings deploy/settings.xml release:perform
```
