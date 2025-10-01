#!/usr/bin/env bash

set -e

# ==============================
# parse arguments
#
# ./up-container.sh -c "blue-was" -cp ./docker-compose.yaml -rd ./temp -rn rollback
#
# ==============================
CONTAINER_NAME=''     # required  -c, --container
COMPOSE_PATH=''       # required  -cp, --compose-path
ROLLBACK_DIR=''       # required  -rd, --rollback-dir
ROLLBACK_NAME=''      # required  -rn, --rollback-name

# logging utility
LOGGING="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/../common/logging.sh"
log() {
  bash "${LOGGING}" -m "${1}" -t "${2}"
}

while [[ "${#}" -gt 0 ]]; do
  case "${1}" in
    -c|--container)
      CONTAINER_NAME="${2}"
      shift 2
      ;;
    -cp|--compose-path)
      COMPOSE_PATH="${2}"
      shift 2
      ;;
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
if [[ -z "${CONTAINER_NAME}" || -z "${COMPOSE_PATH}" || -z "${ROLLBACK_DIR}" || -z "${ROLLBACK_NAME}" ]]; then
  log "Unfulfilled arguments" error
  exit 1

# compose file not found
elif [[ ! -f "${COMPOSE_PATH}" ]]; then
  log "Cannot find compose file on ${COMPOSE_PATH}" error
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
# up container
# ==============================
if ! (docker compose -f "${COMPOSE_PATH}" up "${CONTAINER_NAME}" -d); then
  log "Failed to up container ${CONTAINER_NAME}" error
  exit 1
fi

record_rollback "docker compose -f '${COMPOSE_PATH}' down '${CONTAINER_NAME}'"

log "Successfully up container: ${CONTAINER_NAME}" success
