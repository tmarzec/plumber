#!/bin/bash

rm -r cmp/ 2>/dev/null
mkdir cmp
cp -r resources/* cmp
javac -d cmp --module-path javafx-sdk-16/lib --add-modules javafx.controls,javafx.fxml src/sample/*.java
