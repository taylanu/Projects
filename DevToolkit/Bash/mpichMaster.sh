#!/bin/bash
echo -e "Starting MPICH setup"
sudo su 
mv hosts /etc/hosts
apt-get update && apt-get upgrade
echo "Installing Dependencies..."
apt-get install nfs-server clusterssh openssh-server build-essential mpich2
# apt-get install nfs-client for slave nodes
cd 
mkdir /mirror
echo "/mirror *(rw,sync)" | tee -a /etc/exports
service nfs-kernel-server restart
mount ub0:/mirror /mirror
mount ub1:/mirror /mirror
mount ub2:/mirror /mirror
mount ub3:/mirror /mirror
chown mpiu /mirror
exit
echo "Moving to Master User"
su - mpiu
ssh-keygen ­-t rsa
cd .ssh
cat id_rsa.pub >> authorized_keys
## https://help.ubuntu.com/community/MpichCluster