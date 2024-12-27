#!/bin/bash

# Укажите путь к JDK 17
export JAVA_HOME=/Users/sergejsmelev/Library/Java/JavaVirtualMachines/corretto-21.0.4/Contents/Home

# Укажите путь к JavaFX SDK
export PATH_TO_FX=/Users/sergejsmelev/Downloads/javafx-sdk-23.0.1/lib

# Добавьте JDK в PATH
export PATH=$JAVA_HOME/bin:$PATH

# Запустите приложение
java --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.fxml -jar target/todo-list-1.0-SNAPSHOT.jar