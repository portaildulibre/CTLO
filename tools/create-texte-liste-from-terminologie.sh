#! /bin/sh
TARGET="target/liste.txt"
grep -e '<anglicisme id="' target/terminologie.xml  | sed -e 's/^<anglicisme id="\(.*\)\(">.*$\)/\1/g' > ${TARGET}
iconv -f 'UTF-8' -t 'ISO-8859-1'  ${TARGET}  > target/liste.iso8859.1.txt
unix2dos target/liste.iso8859.1.txt
