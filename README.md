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

1. Ensure your system is up-to-date by running: 
 * **Red Hat/Centos/Fedora: ** `sudo yum update` or `sudo dnf update`
 * **Ubuntu/Debian/Mint: ** `sudo apt update` then `sudo apt upgrade`
 * **OpenSuse: ** `sudo zypper update`
 * **Arch: ** `sudo pacman -Syu`
2. Enable the Community Repository. If not already enabled, ensure that the `[community]` repository is enabled in your `/etc/pacman.conf` file. It usually is by default.
3. Install Elasticsearch using Pacman: `yay -S elasticsearch7`
4. Then start the service with `sudo systemctl start elasticsearch.service`
5. Enable the Elasticsearch service to start automatically on boot with `sudo systemctl enable elasticsearch.service`
6. The default configuration file is located at `/etc/elasticsearch/elasticsearch.yml`, and one can configure it here
7. After Elasticsearch starts, you can verify that it is running properly by sending an HTTP request to its API:
```bash
curl -X GET "localhost:9200/"
```
This should return a JSON response with information about the Elasticsearch instance.

- **NB**: Elasticsearch might take a few moments to start up. If you encounter any issues, check the logs in `/var/log/elasticsearch/` for more information. Also make sure you have jdk19-openjdk installed for v7.17.15, as it is

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

**NB**: Make sure you change the uernames and passwords. So far I have hardcoded the username and password to `elastic` and `changeme` for now.  I will change it to check for environment variables, so that one can specify another username and password, and not a hardcoded one.

## 3. Compatibility Check

- Ensure that the version of Elasticsearch you are installing is compatible with Java 21
- You can refer to the Elasticsearch [support matrix](https://www.elastic.co/support/matrix#matrix_jvm) to check compatibility
- If there's an incompatibility issue, either change the Elasticsearch version to one that supports Java 21 or use a different JDK version that's compatible with your Elasticsearch version

## Adding Elasticsearch SSL Certificate to Java Keystore

1. **Export the SSL Certificate**:
   - Use a browser or `openssl` to export the SSL certificate from your Elasticsearch server.
   - Command example with `openssl`:
     ```
     openssl s_client -connect localhost:9200 </dev/null | openssl x509 -outform PEM > elasticsearch-cert.pem
     ```
2. **Import the Certificate into Java's Keystore**:
   - Use the `keytool` utility included with the JDK:
     ```
     keytool -import -alias elasticsearch -keystore [Path-To-Keystore] -file elasticsearch-cert.pem
     ```
   - The default path for Java's keystore is usually `$JAVA_HOME/lib/security/cacerts`
   - Default keystore password is typically `changeit`

## Building the Project

1. Clone the repository to your local machine
2. Navigate to the project directory
3. Run `gradle build` to build the project

## Running the Application

1. After building the project, start the Spring Boot application by running `gradle bootRun`
2. The application will start and be accessible on `http://localhost:8080`

## Usage

### Create an Index

```bash
curl -X POST "http://localhost:8080/elasticsearch/createIndex?indexName=indexname"
```
Replace indexname with your desired index name (in lowercase)

### Create a Document
```bash
curl -X POST "http://localhost:8080/elasticsearch/createDocument?indexName=indexname" -H "Content-Type: application/json" -d '{"field1": "value1", "field2": "value2"}'
```
Replace indexname with the name of the index where you want to create the document (in  lowecase)

### Get a Document
```bash
curl "http://localhost:8080/elasticsearch/getDocument?indexName=indexname&id=documentId"
```
Replace indexname with the name of your index and documentId with the ID of the document you want to retrieve


# Quick Start with ''Make' Guide

This guide provides instructions on how to quickly set up and run the project using the provided `Makefile`. Please follow these steps to ensure a smooth setup

### Prerequisites

- Ensure you have make installed on your system
- For Linux users, pacman and yay should be available for installing packages
- For macOS users, brew should be installed and available
- Make sure you have sufficient permissions to execute sudo commands

### Setting Up and Running the Application

1. **Clone the Repository**:

First, clone the repository to your local machine:
```bash
git clone https://github.com/APoniatowski/java-api-learning
cd java-api-learning
```

2. **Run the Setup**:

To set up the project environment, run the following command in your terminal:
```bash
make install-elasticsearch-linux  # For Linux users
make install-elasticsearch-macos  # For macOS users (untested)
# For Windows users, follow manual installation instructions.
```
After this completes, run the following:
```bash
make install-java-linux  # For Linux users
make install-java-macos  # For macOS users (untested)
# For Windows users, follow manual installation instructions.
```

3. **Start the Application**:

Start the application by running:
```bash
make run
```

This command will start the application in the foreground. To continue with the next steps, open a new terminal or tab

### Running Tests

Once the application is running, you can execute tests in a separate terminal or tab

1. **Open a New Terminal/Tab**:

While keeping the application running in the first terminal, open a new terminal or tab in your command-line interface

2. **Execute Tests**:

In the new terminal, run:
```bash
make test-curl
```

This will execute a series of `curl` commands to test various functionalities of the running application. There will be instructions on how to execute it manually, if it were to fail

### Stopping the Application
After you have finished testing or using the application, you can stop it by going back to the original terminal where the application is running and pressing `Ctrl+C`

### Cleanup

To clean up the environment after you're done, run:
```bash
make clean
```

This will remove any build artifacts and revert some of the installation changes


**NB**: The provided `Makefile` is designed for ease of use and quick setup. However, it's always good practice to review the `Makefile` commands to understand what each command does, especially those that require `sudo` privileges. For Windows users, as some commands are untested, please follow the manual setup instructions provided in the `Makefile`

[![Watch the video](https://img.youtube.com/vi/QrnQjOQmuNY/maxresdefault.jpg)](https://youtu.be/QrnQjOQmuNY)

