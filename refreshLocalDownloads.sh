set -e
set -o pipefail
rsync -ravuzh $PANOPSET_SITE_USR@$PANOPSET_SITE_NAME:/var/www/$PANOPSET_SITE_DN/html /var/www/


