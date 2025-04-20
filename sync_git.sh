#!/bin/bash

echo "Pull depuis GitHub (origin)..."
git pull origin main

echo "Ajout des fichiers modifiés..."
git add .

echo -n "Message du commit : "
read commit_message

if [ -z "$commit_message" ]; then
  echo "Le message de commit ne peut pas être vide."
  exit 1
fi

echo "Création du commit..."
git commit -m "$commit_message" || { echo "Échec du commit"; exit 1; }

echo "Push vers GitHub..."
git push origin main || { echo "Échec du push vers GitHub."; exit 1; }

echo "Push vers GitLab"
git push gitlab main || { echo "Échec du push vers GitLab (VPN actif ?)."; exit 1; }

echo "Synchro complète."
