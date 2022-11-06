[home](../README.md)

For site deployments, Panopset recommends using a Linux developer workstation.
These instructions may need to be modified for other platforms.

Create a digitalocean server with this initialization script (under Advanced Options):


    #!/bin/bash
    apt-get -y update
    apt-get -y install nginx vim net-tools certbot python3-certbot-nginx openjdk-17-jre-headless default-jdk
    apt-get -y upgrade
    ufw allow OpenSSH
    ufw allow 'Nginx Full'
    ufw enable
    export HOSTNAME=$(curl -s http://169.254.169.254/metadata/v1/hostname)
    export PUBLIC_IPV4=$(curl -s http://169.254.169.254/metadata/v1/interfaces/public/0/ipv4/address)
    echo Droplet: $HOSTNAME, IP Address: $PUBLIC_IPV4 > /usr/share/nginx/html/index.html


Edit your ssh config file, on your PC


    vim ~/.ssh/config


Add the following lines, replacing anything in <> with your values:


    Host <your host name>
    HostName <your host ip address, as defined in $PANOPSET_SITE_IP%>
    User root
    IdentityFile ~/.ssh/<your private key file>


Update your PANOPSET_SITE_IP and PANOPSET_SITE_NAME (as you defined in ~/.ssh/config) environment variables, so that the deployment script will work.



    sudo vim ~/.profile



Now, ssh out there:


    ./s.sh


Now is a good time to


    sudo vim /etc/environment


... and ad the server environment variables PANOPSET_SITE_REDIS_URL and PANOPSET_SITE_REDIS_PWD.

it will then look something like this:


PATH="/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/games:/usr/local/games:/snap/bin"
EXPORT PANOPSET_SITE_REDIS_URL="<redis_url>"
EXPORT PANOPSET_SITE_REDIS_PWD="<redis_pwd>"


Make sure the script executed successfully.


    echo $HOSTNAME
    java -version
    netstat -tulpn
    ufw status


[Verify](./verify.md) results.


If you got a message, that looked like this:


    *** System restart required ***


The initial script was probably done by the time you logged in.

Once you get the expected verification results, it is safe to:


    sudo reboot 0



Short break, then right back out there to verify everything is back up:


    ./s.sh
    exit
    ./userprep.sh
    ./s.sh
    ./crtusr.sh
    exit


...back to your workstation again and update your 


    ./vc.sh


file, replacing root with your username, as defined in $PANOPSET_SITE_USR. Then:


    ./deploy.sh
    ./s.sh
    ./installservice.sh
    sudo reboot 0


short break and then...


    ./s.sh


For subsequent deployments, you don't have to run installservice.sh. again, 
just reboot after the deployment.

Optional, if you want to hit 8080 directly:


    sudo ufw allow 8080


... troubleshooting


    sudo systemctl status web
    journalctl -u web
    