#! /bin/bash
# stupid ipgw login
# range=2 means free permission, and 1 means non-free

wget --no-check-certificate -Y off -T 10 -t 3 -O /dev/null \
"https://its.pku.edu.cn:5428/ipgatewayofpku?uid=YOUR_STUDENT_ID&password=YOUR_PASSWD&range=2&operation=disconnectall&timeout=1"

sleep 1s

wget --no-check-certificate -Y off -T 10 -t 3 -O /dev/null \
"https://its.pku.edu.cn:5428/ipgatewayofpku?uid=YOUR_STUDENT_ID&password=YOUR_PASSWD&range=2&operation=connect&timeout=1"