#!/usr/bin/env bash

set -e

# ==============================
# parse arguments
#
# ./rollback.sh -r ./rollback/commands
#
# ==============================
ROLLBACKS=''    # required  -r,--rollbacks

# logging utility
LOGGING="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/./common/logging.sh"
log() {
  bash "${LOGGING}" -m "${1}" -t "${2}"
}

while [[ "${#}" -gt 0 ]]; do
  case "${1}" in
    -r|--rollbacks)
      ROLLBACKS="${2}"
      shift 2
      ;;
    *)
      log "Unknown argument: ${1}" error
      exit 1
  esac
done

# no arguments given
if [[ -z "${ROLLBACKS}" ]]; then
  log "Unfulfilled arguments" error
  exit 1
fi

# if no rollback has been found, just exit
if [[ ! -f "${ROLLBACKS}" ]]; then
  log "No rollback has been found. Aborting rollback..." warn
  exit 0
fi

# ==============================
# execute rollback commands
# ==============================
while IFS= read -r CMD || [[ -n "${CMD}" ]]; do
  # skip empty lines
  [[ -z "${CMD}" ]] && continue

  log "Executing rollback: ${CMD}" info
  if ! bash -c "${CMD}"; then
    log "FATAL ERROR - Rollback command failed: ${CMD}" error
    exit 1
  fi
done < "${ROLLBACKS}"

log "Rollback completed successfully" success
