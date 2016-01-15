#!/bin/bash
echo CONVERTING...

mkdir frames
rm frames/*

ffmpeg -i $1 -y -f image2 frames/frame%04d.png
echo DONE
