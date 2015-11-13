#!/bin/bash

set -eu

tree -d

if test -d ./app/build/spoon ; then
	tar zcf ./app/build/spoon.tgz ./app/build/spoon
	ncftpput -u $FTP_USER -p $FTP_PASSWORD $FTP_HOST /home/$FTP_USER ./app/build/spoon.tgz
	echo Test report has been uploaded
else
	echo No report to upload
fi
