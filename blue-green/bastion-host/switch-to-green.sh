#!/usr/bin/env bash

set -e

# ==============================
# constnats
#
# in default nginx config, it loads '*.conf' files in '/etc/nginx/conf.d' directory
#
# ==============================
CONF_DIR='/etc/nginx/conf.d'
CONF_FILE='deployment.conf'
CONF_PATH="${CONF_DIR}/${CONF_FILE}"

# ==============================
# parse arguments
#
# ./switch-to-green.sh -rd ./temp -rn rollback
#
# ==============================
ROLLBACK_DIR=''       # required  -rd, --rollback-dir
ROLLBACK_NAME=''      # required  -rn, --rollback-name

# logging utility
LOGGING="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/./logging.sh"
log() {
  bash "${LOGGING}" -m "${1}" -t "${2}"
}

while [[ "${#}" -gt 0 ]]; do
  case "${1}" in
    -rd|--rollback-dir)
      ROLLBACK_DIR="${2}"
      shift 2
      ;;
    -rn|--rollback-name)
      ROLLBACK_NAME="${2}"
      shift 2
      ;;
    *)
      log "Unknown argument: ${1}" error
      exit 1
  esac
done

# no arguments given
if [[ -z "${ROLLBACK_DIR}" || -z "${ROLLBACK_NAME}" ]]; then
  log "Unfulfilled arguments" error
  exit 1
fi

# prepending utility
PREPEND="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/../common/prepend.sh"
prepend() {
  bash "${PREPEND}" -c "${1}" -d "${ROLLBACK_DIR}" -f "${ROLLBACK_NAME}"
}

# rollback recording utility
record_rollback() {
  prepend "${1}" || {
    log "CRITICAL: Failed to record rollback" error
    exit 1
  }
}

# ==============================
# replace config to green
# ==============================

record_rollback "sudo nginx -s reload"    # record nginx reload in case for rollback

if ! (echo "set \$deployment green;" | sudo tee "${CONF_PATH}" >/dev/null); then
  log "Failed to update nginx config" error
  exit 1
fi

# record rollback
record_rollback "echo 'set \$deployment blue;' | sudo tee '${CONF_PATH}' >/dev/null"

# ==============================
# test & reload nginx
# ==============================
if ! (sudo nginx -t); then
  log "Invalid nginx config" error
  exit 1
fi

if ! (sudo nginx -s reload); then
  log "Failed to reload nginx" error
  exit 1
fi

log "Switched to green successfully" success
