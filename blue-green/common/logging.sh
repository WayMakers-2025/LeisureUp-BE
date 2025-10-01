#!/usr/bin/env bash

set -e

# ==============================
# parse arguments
#
# ./logging.sh -m "hello" -t info
#
# ==============================
MSG=''        # required  -m, --msg
TYPE='text'   # default   -t, --type  [text|info|success|warn|error]

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
CYAN='\033[0;36m'
YELLOW='\033[0;33m'
RESET='\033[0m'

# functions
log_text() {
  echo -e "$(date "+%Y-%m-%d %H:%M:%S") - ${1}"
}

log_info() {
  echo -e "${CYAN}$(date "+%Y-%m-%d %H:%M:%S") - ${1}${RESET}"
}

log_success() {
  echo -e "${GREEN}$(date "+%Y-%m-%d %H:%M:%S") - ${1}${RESET}"
}

log_warn() {
  echo -e "${YELLOW}$(date "+%Y-%m-%d %H:%M:%S") - ${1}${RESET}"
}

log_error() {
  echo -e "${RED}$(date "+%Y-%m-%d %H:%M:%S") - ${1}${RESET}"
}

while [[ "${#}" -gt 0 ]]; do
  case "${1}" in
    -m|--msg)
      MSG="${2}"
      shift 2
      ;;
    -t|--type)
      TYPE="${2}"
      shift 2
      ;;
    *)
      log_error "Unknown argument: ${1}"
      exit 1
      ;;
  esac
done

# no msg given
if [[ -z "${MSG}" ]]; then
  log_error "No message provided"
  exit 1
fi


# ==============================
# log message
# ==============================
case "${TYPE}" in
  text)     log_text    "${MSG}"  ;;
  info)     log_info    "${MSG}"  ;;
  success)  log_success "${MSG}"  ;;
  warn)     log_warn    "${MSG}"  ;;
  error)    log_error   "${MSG}"  ;;
  *)
    # type mismatch
    log_error "Log type must be [text|info|success|warn|error]"
    exit 1
    ;;
esac
