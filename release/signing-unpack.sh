read -sp "Enter the encrypt key: " ENCRYPT_KEY
echo

if [[ -n "$ENCRYPT_KEY" ]]; then
  openssl enc -d -aes-256-cbc -md sha512 -pbkdf2 -iter 100000 -salt -in release/secring.gpg.aes -out release/secring.gpg -k ${ENCRYPT_KEY}
  openssl enc -d -aes-256-cbc -md sha512 -pbkdf2 -iter 100000 -salt -in release/signing.properties.aes -out release/signing.properties -k ${ENCRYPT_KEY}

else
  echo "Encrypt key is empty"
fi
