#!/usr/bin/env bash

KAREOL_LIBRETTO_URLS_PATH="../corpus/crawl/kareol_libretto_urls.txt"
KAREOL_LIBRETTO_CRAWL_PATH="../corpus/crawl/html/kareol_libretto"
# Fetch a list of URLs to Kareol libretti.
curl --location --silent "http://www.kareol.es/obra.htm" \
    | hxnormalize -x \
    | hxselect -s "\n" 'a[href*="obras"]::attr(href)' \
    | sed -e 's/^href="//' -e 's/"$//' \
    | sed "/^http:/b; /^https:/b; /^ftp:/b; s/^/http:\/\/www.kareol.es\//" \
    | sed "s/www.supercable.es\/~ealmagro\/kareol\//www.kareol.es\//" \
    > ${KAREOL_LIBRETTO_URLS_PATH}
# Download Kareol libretto corpus.
wget --input-file ${KAREOL_LIBRETTO_URLS_PATH} \
    --directory-prefix ${KAREOL_LIBRETTO_CRAWL_PATH} \
    --recursive --convert-links --relative --no-parent \
    --wait 3 --random-wait # Fairness policy
