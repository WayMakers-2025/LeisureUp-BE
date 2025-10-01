#!/usr/bin/env bash

set -e

# ==============================
# parse arguments
#
# ./assert-health-check.sh -u http://localhost:8080/health -m 3 -t 15
#
# ==============================
HEALTH_CHECK_URL='' # required    -u, --url
MAX_ATTEMPTS=3      # default     -m, --max
TIMEOUT=30          # default     -t, --timeout

# URL regex with port
URL_REGEX='^https?://[a-zA-Z0-9.-]+:[0-9]+/[-a-zA-Z0-9._~:/?#@!$&'"'"'()*+,;=]*$'
NUMBER_REGEX='^[0-9]+$'

# logging utility
LOGGING="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/../common/logging.sh"
log() {
  bash "${LOGGING}" -m "${1}" -t "${2}"
}

while [[ $# -gt 0 ]]; do
  case "${1}" in
    -u|--url)
      HEALTH_CHECK_URL="${2}"
      shift 2
      ;;
    -m|--max)
      MAX_ATTEMPTS="${2}"
      shift 2
      ;;
    -t|--timeout)
      TIMEOUT="${2}"
      shift 2
      ;;
    *)
      log "Unknown argument: ${1}" error
      exit 1
  esac
done

# no arguments given
if [[ -z "${HEALTH_CHECK_URL}" || -z "${MAX_ATTEMPTS}" || -z "${TIMEOUT}" ]]; then
  log "Unfulfilled arguments" error
  exit 1

# URL regex mismatch
elif ! [[ "${HEALTH_CHECK_URL}" =~ ${URL_REGEX}  ]]; then
  log "HEALTH_CHECK_URL must match with regex: [${URL_REGEX}]" error
  exit 1

# non decimal given
elif ! [[ "${MAX_ATTEMPTS}" =~ ${NUMBER_REGEX} ]] || ! [[ "${TIMEOUT}" =~ ${NUMBER_REGEX} ]]; then
  log "MAX_ATTEMPTS & TIMEOUT must be decimal" error
  exit 1

# must be positive
elif [[ ${MAX_ATTEMPTS} -le 0 || ${TIMEOUT} -le 0 ]]; then
  log "MAX_ATTEMPTS & TIMEOUT must be positive" error
  exit 1
fi

# ==============================
# do health check
# ==============================
URL="${HEALTH_CHECK_URL}"

for (( i = 1; i <= MAX_ATTEMPTS; i++ )); do

  # get code
  HTTP_CODE=$(curl -sk -o /dev/null -w "%{http_code}" -m "${TIMEOUT}" "${URL}" || echo 0)

  # good
  if [[ "${HTTP_CODE}" -eq 200 ]]; then
    log "Health check success" success
    exit 0

  # received response, but not 200
  elif [[ "${HTTP_CODE}" -ne 0 ]]; then
    log "Received response, but not 200 code" warn

  # received no response
  else
    log "Received no response" warn
  fi

  log "Attempt [${i}/${MAX_ATTEMPTS}] failed (HTTP ${HTTP_CODE}). Retrying..." warn
  sleep 1
done

log "Failed to assert health check" error
exit 1
