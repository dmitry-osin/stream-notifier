#!/bin/bash

# Get the directory where the script is located
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
ENV_FILE="$SCRIPT_DIR/.env"

if [ -f "$ENV_FILE" ]; then
    # Read each line from .env file
    while IFS= read -r line || [ -n "$line" ]; do
        # Skip comments and empty lines
        if [[ $line =~ ^[^#].+=.+ ]]; then
            # Export the variable
            export "$line"
            echo "Set ${line%%=*} environment variable"
        fi
    done < "$ENV_FILE"
    echo "Environment variables loaded successfully"
else
    echo "Error: .env file not found at $ENV_FILE"
    exit 1
fi 