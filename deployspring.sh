git pull
gradle build
scp ./build/libs/site.jar $PANOPSET_SITE_USR@$PANOPSET_SITE_NAME:/home/$PANOPSET_SITE_USR/
