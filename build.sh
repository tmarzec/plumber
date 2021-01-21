#!/bin/bash

rm -r cmp/
mkdir cmp
cp -r resources/* cmp
javac -d cmp --module-path ${PATH_TO_FX} --add-modules javafx.controls,javafx.fxml src/sample/*.java
