#!/bin/bash

set -euv

test -d app/build/spoon
tar zcf app/build/spoon.tgz app/build/spoon
ncftpput \
	-u $FTP_USER \
   	-p $FTP_PASSWORD \
	$FTP_HOST \
	/home/$FTP_USER app/build/spoon.tgz
