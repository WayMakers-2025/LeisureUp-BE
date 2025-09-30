#!/usr/bin/env bash

set -e

# ==============================
# parse arguments
#
# ./health-check.sh -u http://localhost:8080/health -m 3 -t 15
#
# ==============================
HEALTH_CHECK_URL='' # required    -u, --url
MAX_ATTEMPTS=3      # default     -m, --max
TIMEOUT=30          # default     -t, --timeout

# URL regex with port
URL_REGEX='^https?://[a-zA-Z0-9.-]+:[0-9]+/[-a-zA-Z0-9._~:/?#@!$&'"'"'()*+,;=]*$'
NUMBER_REGEX='^[0-9]+$'

while [[ $# -gt 0 ]]; do
  case "$1" in
    -u|--url)
      HEALTH_CHECK_URL="$2"
      shift 2
      ;;
    -m|--max)
      MAX_ATTEMPTS="$2"
      shift 2
      ;;
    -t|--timeout)
      TIMEOUT="$2"
      shift 2
      ;;
    *)
      echo "Unknown argument: $1"
      exit 1
  esac
done

# no arguments given
if [[ -z "${HEALTH_CHECK_URL}" || -z "${MAX_ATTEMPTS}" || -z "${TIMEOUT}" ]]; then
  echo "Unfulfilled arguments"
  exit 1

# URL regex mismatch
elif ! [[ "${HEALTH_CHECK_URL}" =~ ${URL_REGEX}  ]]; then
  echo "HEALTH_CHECK_URL must match with regex: [${URL_REGEX}]"
  exit 1

# non decimal given
elif ! [[ "${MAX_ATTEMPTS}" =~ ${NUMBER_REGEX} ]] || ! [[ "${TIMEOUT}" =~ ${NUMBER_REGEX} ]]; then
  echo "MAX_ATTEMPTS & TIMEOUT must be decimal"
  exit 1

# must be positive
elif [[ ${MAX_ATTEMPTS} -le 0 || ${TIMEOUT} -le 0 ]]; then
  echo "MAX_ATTEMPTS & TIMEOUT must be positive"
  exit 1
fi


# ==============================
# do health check
# ==============================
SUCCESS=false
TYPE="UNKNOWN"

URL="${HEALTH_CHECK_URL}"

for (( i = 1; i <= MAX_ATTEMPTS; i++ )); do

  # get resp & code
  RESPONSE=$(curl -sk -m "${TIMEOUT}" "${URL}" || true)
  HTTP_CODE=$(curl -sk -o /dev/null -w "%{http_code}" -m "${TIMEOUT}" "${URL}" || echo 0)

  # received response with HTTP OK status
  if [[ "${HTTP_CODE}" -eq 200 && ! (-z "${RESPONSE}") ]]; then

    # parse app type from response
    TYPE=$(echo "${RESPONSE}" | jq -r '.data.type // "UNKNOWN"')

    # failed to inspect type
    if [[ "${TYPE}" == "UNKNOWN" || -z "${TYPE}" ]]; then
      echo "Received OK status, but failed to inspect type. Retrying request"
      continue
    fi

    # health check succedded
    SUCCESS=true
    echo "Health check succeeded: ${TYPE}"
    break
  fi

  echo "Attempt [${i}/${MAX_ATTEMPTS}] failed (HTTP ${HTTP_CODE}). Retrying..."
  sleep 1
done

# ==============================
# echo result
# ==============================
echo "success=${SUCCESS}"
echo "type=${TYPE}"
