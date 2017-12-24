#!/bin/bash
echo "" > info.txt
hwinfo --short >> info.txt
awk '/MemTotal/ { printf "%.1f \n", $2/1024/1024 }' /proc/meminfo >> info.txt
lsb_release -d >> info.txt
cat /proc/cpuinfo | grep 'model name' | uniq >> info.txt


