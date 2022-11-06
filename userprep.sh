if [ "$PANOPSET_SITE_NAME" = "" ]; then
 echo 'Please define required environment variables, see documentation in the README.md.'
 exit 2
fi

if [ "$PANOPSET_SITE_IP" = "" ]; then
 echo 'Please define required environment variables, see documentation in the README.md.'
 exit 2
fi

if [ "$PANOPSET_SITE_USR" = "" ]; then
 echo 'Please define required environment variables, see documentation in the README.md.'
 exit 2
fi

if [ "$PANOPSET_SITE_PWD" = "" ]; then
 echo 'Please define required environment variables, see documentation in the README.md.'
 exit 2
fi

# Create the adduser.sh command for your new server

java -cp ~/panopset.jar -D$PANOPSET_SITE_USR=$PANOPSET_SITE_USR -D$PANOPSET_SITE_PWD=$PANOPSET_SITE_PWD com.panopset.flywheel.Flywheel ./docs/templates/crtusr.txt ./temp
chmod +x ./temp/*.sh
scp ./temp/* root@$PANOPSET_SITE_NAME:/root/
