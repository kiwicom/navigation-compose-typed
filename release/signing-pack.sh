read -sp "Enter the encrypt key: " ENCRYPT_KEY
echo

if [[ -n "$ENCRYPT_KEY" ]]; then
  openssl enc -aes-256-cbc -md sha512 -pbkdf2 -iter 100000 -salt -in release/secring.gpg -out release/secring.gpg.aes -k ${ENCRYPT_KEY}
  openssl enc -aes-256-cbc -md sha512 -pbkdf2 -iter 100000 -salt -in release/signing.properties -out release/signing.properties.aes -k ${ENCRYPT_KEY}

else
  echo "Encrypt key is empty"
fi
