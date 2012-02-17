#!/bin/bash

mkdir google_final
files=`ls google`

for var in $files; do
temp="google";
file=$temp/$var

opfile="google_final/"$var
cat $file | grep "<id>"| grep "http" |cut -d">" -f2 | cut -d"<" -f1 | sed 's/.*wiki\///g' | sed 's/%E2%80%93/-/g' | sed 's/%3A/:/g' | sed 's/%26/&/g'  | sed 's/%C3%A1/á/g' | sed 's/%C3%A9/é/g' | sed 's/%C3%A9/é/g'> $opfile
done 

