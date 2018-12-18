#!/usr/bin/env bash

OPERA_LIB_LIBRETTO_URLS_PATH="../corpus/crawl/opera_lib_urls.txt"
OPERA_LIB_LIBRETTO_CRAWL_PATH="../corpus/crawl/html/opera_lib"
# Fetch a list of URLs to OperaLib libretti.
curl --location --silent "http://www.operalib.eu/ope_alfatit_eng.html" \
    | hxnormalize -x \
    | hxselect -s "\n" '.alfa .pt a::attr(href)' \
    | sed -e 's/^href="//' -e 's/"$//' \
    | sed "/^http:/b; /^https:/b; /^ftp:/b; s/^/http:\/\/www.operalib.eu\//" \
    | sed "s/\/\([^\/]*\).html/\/rid.html/" \
    > ${OPERA_LIB_LIBRETTO_URLS_PATH}
# Download OperaLib libretto corpus.
wget --input-file ${OPERA_LIB_LIBRETTO_URLS_PATH} \
    --directory-prefix ${OPERA_LIB_LIBRETTO_CRAWL_PATH} \
    --wait 3 --random-wait # Fairness policy
