set -e
set -o pipefail
./deploylocal.sh
scp ./build/libs/site.jar $PANOPSET_SITE_USR@$PANOPSET_SITE_NAME:/home/$PANOPSET_SITE_USR/
rsync -r raw/* $PANOPSET_SITE_USR@$PANOPSET_SITE_NAME:/var/www/$PANOPSET_SITE_DN/html/
echo $PANOPSET_SITE_PWD | ssh -tt $PANOPSET_SITE_NAME "sudo reboot 0"

