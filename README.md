# Java API Learning

## Project Setup Guide

This README provides instructions for setting up Java/JDK v21 and Elasticsearch for the backend project

## 1. Installing Java/JDK v21
### On Linux:

1. Update your package repository with the command `sudo apt update`
2. Install JDK 21 using `sudo pacman -S jdk-openjdk`. If this package is not available in your distribution's repository (which I would highly doubt, if you're using Arch or NixOS), please refer to the [Oracle website](https://www.oracle.com/java/technologies/javase-jdk21-downloads.html) for alternative installation methods
3. Verify the installation with `java -version`

### On Windows (untested):

1. Download JDK 21 from the [Oracle website](https://www.oracle.com/java/technologies/javase-jdk21-downloads.html) or an alternative Java provider
2. Run the installer and follow the instructions to install JDK on your system
3. After installation, set the `JAVA_HOME` environment variable to the path where JDK is installed
4. Add `%JAVA_HOME%\bin` to your system's `Path` variable
5. Verify the installation by opening a command prompt and typing `java -version`


### On macOS (untested):

1. You can use [Homebrew](https://brew.sh/): `brew tap homebrew/cask-versions` then `brew cask install java21`
2. Alternatively, download and install JDK 21 from the [Oracle website](https://www.oracle.com/java/technologies/javase-jdk21-downloads.html)
3. Verify the installation in the terminal: `java -version`

## 2. Installing Elasticsearch

### On Linux:

1. Ensure your system is up-to-date by running: `sudo pacman -Syu`
2. Enable the Community Repository. If not already enabled, ensure that the `[community]` repository is enabled in your `/etc/pacman.conf` file. It usually is by default.
3. Install Elasticsearch using Pacman: `sudo pacman -S elasticsearch`
4. Then start the service with `sudo systemctl start elasticsearch.service`
5. Enable the Elasticsearch service to start automatically on boot with `sudo systemctl enable elasticsearch.service`
6. The default configuration file is located at `/etc/elasticsearch/elasticsearch.yml`, and one can configure it here
7. After Elasticsearch starts, you can verify that it is running properly by sending an HTTP request to its API:
```bash
curl -X GET "localhost:9200/"
```
This should return a JSON response with information about the Elasticsearch instance.

- **NB**: Elasticsearch might take a few moments to start up. If you encounter any issues, check the logs in `/var/log/elasticsearch/` for more information.

- **TIP**: You can enable and start the service with `sudo systemctl enable --now elasticsearch.service`

### On Windows (untested):
1. Download Elasticsearch from the [official Elasticsearch website](https://www.elastic.co/downloads/elasticsearch)
2. Choose the version that is compatible with JDK 21 (refer to the Elasticsearch support matrix for this information)
3. Unzip the downloaded file to your desired location
4. Navigate to the Elasticsearch directory in the command line and start Elasticsearch with `./bin/elasticsearch` (WSL) or `.\bin\elasticsearch.bat` (Command Prompt)

### On macOS (untested):

1. Update Homebrew to ensure you have the latest package listings. Open the Terminal and run `brew update`
2. Install Elasticsearch directly through Homebrew by running `brew install elasticsearch`
3. After the installation is complete, you can start Elasticsearch with `brew services start elasticsearch`
4. Configure Elasticsearch by editing `/usr/local/etc/elasticsearch/elasticsearch.yml`
5. To make sure that Elasticsearch is running properly, you can send a request to its HTTP API by runingi:
```bash
curl -X GET "localhost:9200/"
```
If Elasticsearch is running, you'll receive a JSON response with its status


## 3. Compatibility Check

- Ensure that the version of Elasticsearch you are installing is compatible with Java 21
- You can refer to the Elasticsearch [support matrix](https://www.elastic.co/support/matrix#matrix_jvm) to check compatibility
- If there's an incompatibility issue, either change the Elasticsearch version to one that supports Java 21 or use a different JDK version that's compatible with your Elasticsearch version


