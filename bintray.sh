mkdir $HOME/.bintray/
printf "realm = %s\nhost = %s\nuser = %s\npassword = %s" "$BINTRAY_REALM" "$BINTRAY_HOST" "$BINTRAY_USER" "$BINTRAY_API_KEY" > $HOME/.bintray/.credentials
./sbt -Dtag=${TRAVIS_TAG} +publish
