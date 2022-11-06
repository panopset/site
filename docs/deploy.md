[home](../README.md)

Once you have your development system all set up with the environment variables 
dscribed in the [README.md](../README.md), you're ready to deploy.

These instructions were tested using [Digitalocean](https://digitalocean.com)* Ubuntu Linux.

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


Optional, if you want to hit 8081 directly, also include:


    ufw allow 8081


Update src/main/resources/application.properties to change the port.

... but normally you'd proxy it, here are some good articles about how to do that:



* https://www.digitalocean.com/community/tutorials/how-to-secure-nginx-with-let-s-encrypt-on-ubuntu-20-04
* https://www.digitalocean.com/community/tutorials/how-to-configure-nginx-as-a-reverse-proxy-on-ubuntu-22-04


<sub>Note that some steps in the articles above are taken care of in the initalization script above.</sub>

Edit your ssh config file, on your PC


    ./vc.sh


Add the following lines, replacing anything in <> with your values:


    Host <your host name>
    HostName <your host ip address, as defined in $PANOPSET_SITE_IP%>
    User root
    IdentityFile ~/.ssh/<your private key file>


Update your PANOPSET_SITE_IP and PANOPSET_SITE_NAME (as you defined in ~/.ssh/config) environment variables, so that the deployment script will work.



    sudo vim ~/.profile


exit and launch a new shell to pick up your new .profile environment variables.


Now, ssh out there (after cd'ing back into this project's directory):


    ./s.sh


Make sure the digitalocean initialization script executed successfully:


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



Short break, then right back in and out there to verify everything is back up, 
and run some scripts:


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



... troubleshooting


    sudo systemctl status panopsetweb
    journalctl -u panopsetweb


<sub><sup>* Disclaimer, the author owns stock in DOCN.</sub></sup>