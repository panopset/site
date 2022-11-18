./deployspring.sh
./deploystatic.sh
echo $PANOPSET_SITE_PWD | ssh -tt $PANOPSET_SITE_NAME "sudo reboot 0"

