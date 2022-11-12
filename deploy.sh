gradle build
scp ./build/libs/site.jar $PANOPSET_SITE_USR@$PANOPSET_SITE_NAME:/home/$PANOPSET_SITE_USR/
echo $PANOPSET_SITE_PWD | ssh -tt $PANOPSET_SITE_NAME "sudo reboot 0"
