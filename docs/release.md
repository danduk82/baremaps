# Release

``
mvn versions:set -DnewVersion=[release_version] -DgenerateBackupPoms=false
git commit -a -m "Set release version"
git tag [release_tag]
git push origin [release_tag]
mvn clean deploy -P release
mvn versions:set -DnewVersion=[snapshot_version] -DgenerateBackupPoms=false
git commit -a -m "Set snapshot version"
git push
```
