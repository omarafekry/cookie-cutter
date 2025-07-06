#!/bin/bash
# Build the project if target/cookie-cutter.jar does not exist
if [ ! -f target/cookie-cutter.jar ]; then
  if ! command -v mvn &> /dev/null; then
    echo "Maven is not installed. Please install Maven to build the project."
    read -r -p "Press Enter to exit..."
    exit 1
  fi
  echo "Compiling project with Maven..."
  mvn clean package || { echo "Maven build failed."; read -r -p "Press Enter to exit..."; exit 1; }
fi

java -jar target/cookie-cutter.jar "$@" 2>&1
read -r -p "Press Enter to exit..."

