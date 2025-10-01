#!/usr/bin/env bash

set -e

# ==============================
# parse arguments
#
# ./prepend.sh -c "echo hello" -d ./temp -f rollbacks.txt
#
# ==============================
COMMAND=''        # required  -c, --command
DST_DIR=''        # required  -d, --dir
DST_FILE=''       # required  -f, --file

# logging utility
LOGGING="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/./logging.sh"
log() {
  bash "${LOGGING}" -m "${1}" -t "${2}"
}

while [[ "${#}" -gt 0 ]]; do
  case "${1}" in
    -c|--command)
      COMMAND="${2}"
      shift 2
      ;;
    -d|--dir)
      DST_DIR="${2}"
      shift 2
      ;;
    -f|--file)
      DST_FILE="${2}"
      shift 2
      ;;
    *)
      log "Unknown argument: ${1}" error
      exit 1
  esac
done

# no arguments given
if [[ -z "${COMMAND}" || -z "${DST_DIR}" || -z "${DST_FILE}" ]]; then
  log "Unfulfilled arguments" error
  exit 1
fi


# ==============================
# create dir & file if not exists
# ==============================
if [[ ! -d "${DST_DIR}" ]]; then
  log "Creating destination directory" info
  mkdir -p "${DST_DIR}" || {
    log "Failed to create directory" error
    exit 1
  }
fi

DST_PATH="${DST_DIR}/${DST_FILE}"

if [[ ! -f "${DST_PATH}"  ]]; then
  log "Creating destination file" info
  touch "${DST_PATH}" || {
    log "Failed to create file" error
    exit 1
  }
fi

# ==============================
# prepend given
# ==============================
TMP_FILE=$(mktemp)

{
  echo "${COMMAND}"
  cat "${DST_PATH}"
} > "${TMP_FILE}" || {
  log "Failed to append command" error
  exit 1
}

mv "${TMP_FILE}" "${DST_PATH}" || {
  log "Failed to append command" error
  exit 1
}

log "Prepended command successfully: ${COMMAND}" success
