#!/bin/bash
apt-get install -y hwinfo
mkdir /etc/NPL
cp -r client/* /etc/NPL
cd /etc/NPL
chmod 777 runcron.sh script.sh
(crontab -l 2>/dev/null; echo "*/10 * * * * /etc/NPL/runcron.sh") | crontab -
(crontab -l 2>/dev/null; echo "@reboot /etc/NPL/runcron.sh") | crontab -
