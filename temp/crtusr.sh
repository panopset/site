adduser --gecos "" --disabled-password karl
chpasswd <<<"karl:mp#kc25J"
usermod -aG sudo karl
rsync --archive --chown=karl:karl ~/.ssh /home/karl
chown -R karl:karl /var/www
ls /home/karl/.ssh
mv web.service /home/karl/
mv installservice.sh /home/karl/
chown -R karl:karl /home/karl
chmod +x /home/karl/*.sh

