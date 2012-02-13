#!/bin/sh
#Usage : ./script folder_with_files


mkdir done
files=`ls $1`
for var in $files; do
temp="files";
file=$temp/$var

opfile="done/"$var
cat $file | grep "http" | cut -d"/" -f 5 > $opfile
done 
