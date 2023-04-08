#!/bin/bash

touch index.txt serial
chmod 666 index.txt serial
echo 01 >  serial
mkdir -p newcerts private

openssl genrsa -des3 -out ./private/cakey.pem 2048
openssl req -new -x509 -days 3650 -key ./private/cakey.pem -out ca.crt