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
    HostName <your host ip address, $PANOPSET_SITE_IP/%PANOPSET_SITE_IP%>
    User root
    IdentityFile ~/.ssh/<your private key file>


Update your PANOPSET_SITE_IP and PANOPSET_SITE_NAME environment variables, so that the deployment script will work.

Now, ssh out there:


    ./s.sh


Make sure the script executed successfully


    echo $HOSTNAME
    java -version


If you have to ask what to expect from the above, you might be a little in over your head right now.

That's okay, this is how you learn.

You should by now know when it is safe to:


    sudo reboot 0


Short break, then right back out there:
    

    ./s.sh
    netstat -tulpn


you should see nginx listening on port 80.
... next, verify the firewall settings are good:


    ufw status


you should see something like this


    root@skunkworks:~# ufw status
    Status: active
    
    To                         Action      From
    --                         ------      ----
    OpenSSH                    ALLOW       Anywhere
    Nginx Full                 ALLOW       Anywhere
    OpenSSH (v6)               ALLOW       Anywhere (v6)
    Nginx Full (v6)            ALLOW       Anywhere (v6)


now exit out and on your workstation:


    ./userprep.sh
