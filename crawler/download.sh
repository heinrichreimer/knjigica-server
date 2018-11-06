#!/bin/bash

# Fetch a list of links to Kareol libretti.
curl --location --silent http://www.kareol.es/obra.htm \
  | pup 'a[href*="obras"] attr{href}' \
  | sed "/^http:/b; /^https:/b; /^ftp:/b; s/^/http:\/\/www.kareol.es\//" \
  | sed "s/www.supercable.es\/~ealmagro\/kareol\//www.kareol.es\//" \
  > kareol_libretto_urls.txt

wget --input-file kareol_libretto_urls.txt --recursive --convert-links --relative --no-parent --directory-prefix kareol_libretto

# Fetch a list of links to Kareol songs.
curl --location --silent http://www.kareol.es/canciones.htm \
  | pup 'a[href*="obras"] attr{href}' \
  | sed "/^http:/b; /^https:/b; /^ftp:/b; s/^/http:\/\/www.kareol.es\//" \
  | sed "s/www.supercable.es\/~ealmagro\/kareol\//www.kareol.es\//" \
  > kareol_song_urls.txt

wget --input-file kareol_song_urls.txt --recursive --convert-links --relative --no-parent --directory-prefix kareol_song

# Fetch a list of links to OperaLib libretti.
curl --location --silent http://www.operalib.eu/ope_alfatit_eng.html \
  | pup '.alfa .pt a attr{href}' \
  | sed "/^http:/b; /^https:/b; /^ftp:/b; s/^/http:\/\/www.operalib.eu\//" \
  | sed "s/\/\([^\/]*\).html/\/rid.html/" \
  > operalib_libretto_urls.txt

wget --input-file operalib_libretto_urls.txt --directory-prefix operalib_libretto
