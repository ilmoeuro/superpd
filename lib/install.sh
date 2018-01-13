#!/bin/sh
mvn install:install-file -Dfile=jdom-1.0.jar -DgroupId=jvst -DartifactId=jdom -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=napkinlaf-1.2.jar -DgroupId=jvst -DartifactId=napkinlaf -Dversion=1.2 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=napkinlaf-swingset-1.2.jar -DgroupId=jvst -DartifactId=napkinlaf-swingset -Dversion=1.2 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=jVSTsYstem-1.0beta.jar -DgroupId=jvst -DartifactId=jVSTsYstem -Dversion=1.0beta -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=jVSTwRapper-1.0beta.jar -Djavadoc=jVSTwRapper-1.0beta-javadoc.jar -DgroupId=jvst -DartifactId=jVSTwRapper -Dversion=1.0beta -Dpackaging=jar -DgeneratePom=true