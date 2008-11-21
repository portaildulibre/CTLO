#!/bin/sh
target="target"
original_file="../../Work/liste.iso8859.1-141108.txt"
updated_file="${target}/liste.txt"
tmp_file="${target}/tmp"
final_file="${target}/diff.iso8859.1.txt"
./tools/diff-of-2 ${original_file} ${updated_file} | sed -e '//d' > ${tmp_file}
iconv -f 'UTF-8' -t 'ISO-8859-1' ${tmp_file} > ${final_file}
unix2dos ${final_file}
