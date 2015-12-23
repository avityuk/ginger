#!/bin/sh

VERSION=0.3.0
MODULES=(core servlet spring)

for module in "${MODULES[@]}"
do 
   cd target/checkout/$module/target

   mvn gpg:sign-and-deploy-file -Dgpg.passphrase= -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=sonatype-nexus-staging -DpomFile=ginger-$module-$VERSION.pom -Dfile=ginger-$module-$VERSION-sources.jar -Dclassifier=sources
   mvn gpg:sign-and-deploy-file -Dgpg.passphrase= -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=sonatype-nexus-staging -DpomFile=ginger-$module-$VERSION.pom -Dfile=ginger-$module-$VERSION-javadoc.jar -Dclassifier=javadoc
  
   cd ../../../../
done

