@echo off
setlocal enabledelayedexpansion
set jarName=Markov_covid.jar

:: Imposta il percorso della cartella in cui si trova il file batch
set jarPath=%~dp0%jarName%

:: Verifica se il file JAR esiste nella cartella corrente
if exist "!jarPath!" (
    java -jar "!jarPath!"
) else (
    echo DEBUG: JAR non trovato in %~dp0
)