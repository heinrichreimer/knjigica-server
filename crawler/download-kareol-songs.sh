#!/usr/bin/env bash

KAREOL_SONG_URLS_PATH="../corpus/web_crawl/kareol_song_urls.txt"
KAREOL_SONG_CRAWL_PATH="../corpus/web_crawl/html/kareol_song"
# Fetch a list of URLs to Kareol songs.
curl --location --silent "http://www.kareol.es/canciones.htm" \
    | pup 'a[href*="obras"] attr{href}' \
    | sed "/^http:/b; /^https:/b; /^ftp:/b; s/^/http:\/\/www.kareol.es\//" \
    | sed "s/www.supercable.es\/~ealmagro\/kareol\//www.kareol.es\//" \
    > ${KAREOL_SONG_URLS_PATH}
# Download Kareol song corpus.
wget --input-file ${KAREOL_SONG_URLS_PATH} \
    --directory-prefix ${KAREOL_SONG_CRAWL_PATH} \
    --recursive --convert-links --relative --no-parent \
    --wait 3 --random-wait # Fairness policy
