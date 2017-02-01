#!/bin/bash

mysql CS144 < drop.sql

mysql CS144 < create.sql

ant
ant run-all

mysql CS144 < load.sql

rm item.csv
rm bid.csv
rm category.csv
rm user.csv
rm -r bin
