.PHONY: install-java-linux install-java-windows install-java-macos install-elasticsearch-linux install-elasticsearch-windows install-elasticsearch-macos build run test-curl help

all: help

# Default target
help:
	@echo "Usage: make [TARGET]"
	@echo ""
	@echo "Available targets:"
	@echo "  install-java-linux            - Install Java/JDK on Linux"
	@echo "  install-java-windows          - Instructions for installing Java/JDK on Windows [UNTESTED]"
	@echo "  install-java-macos            - Install Java/JDK on macOS [UNTESTED]"
	@echo "  install-elasticsearch-linux   - Install Elasticsearch on Linux"
	@echo "  install-elasticsearch-windows - Instructions for installing Elasticsearch on Windows [UNTESTED]"
	@echo "  install-elasticsearch-macos   - Install Elasticsearch on macOS [UNTESTED]"
	@echo "  build                         - Build the project using Gradle"
	@echo "  run                           - Run the application"
	@echo "  test-curl                     - Test the application with curl commands"
	@echo "  clean                         - Clean up the project"

# Java installation for different OS
install-java-linux:
	sudo pacman -S jdk-openjdk gradle --noconfirm
	sudo archlinux-java set java-21-openjdk
	java -version

install-java-windows:
	@echo "Manual installation required: Download from https://www.oracle.com/java/technologies/javase-jdk21-downloads.html and follow instructions"

install-java-macos:
	brew tap homebrew/cask-versions
	brew cask install java21
	java -version

# Elasticsearch installation for different OS
install-elasticsearch-linux:
	sudo pacman -Syu --noconfirm
	yay -S jdk11-openjdk --noconfirm
	yay -S jdk17-openjdk --noconfirm
	yay -S jdk18-openjdk --noconfirm
	sudo archlinux-java set java-18-openjdk
	yay -S elasticsearch7 --noconfirm
	sudo cp ./elasticsearch.yml /etc/elasticsearch/elasticsearch.yml
	sudo sh -c 'echo "-Xmx2g" >> /etc/elasticsearch/jvm.options'
	sudo sh -c 'echo "-Xms2g" >> /etc/elasticsearch/jvm.options'
	sudo mkdir -p /usr/share/elasticsearch/data
	sudo chown -R elasticsearch:elasticsearch /usr/share/elasticsearch/data
	sudo chmod -R 750 /usr/share/elasticsearch/data
	sudo systemctl start elasticsearch.service
	sudo systemctl enable elasticsearch.service
	@echo "Waiting for the service to start..."
	@sleep 30
	$(eval ELASTIC_PASSWORD=$(shell sudo elasticsearch-setup-passwords auto | grep "PASSWORD elastic" | awk '{print $$4}'))
	@curl -X POST "http://localhost:9200/_security/user/elastic/_password" \
      -H "Content-Type: application/json" \
      -d '{ "password" : "changeme" }' \
      -u elastic:$(ELASTIC_PASSWORD)

install-elasticsearch-windows:
	@echo "Manual installation required: Download from https://www.elastic.co/downloads/elasticsearch and follow instructions"

install-elasticsearch-macos:
	brew update
	brew install elasticsearch
	brew services start elasticsearch

# Build the project using Gradle
build:
	gradle build

# Run the application
run:
	@echo "Starting the server..."
	gradle bootRun

# Test the application with curl commands
test-curl:
	@echo "Testing 'createIndex'"
	@sleep 1
	curl -X POST "http://localhost:8080/elasticsearch/createIndex?indexName=testindex"
	@echo "Testing 'createDocument'"
	@sleep 1
	$(eval DOCUMENT_ID=$(shell curl -X POST "http://localhost:8080/elasticsearch/createDocument?indexName=testindex" -H "Content-Type: application/json" -d '{"field1": "value1", "field2": "value2"}' | grep -o '"id":"[^"]*' | grep -o '[^"]*$$'))
	@echo "Testing 'getDocument'"
	@sleep 1
	curl "http://localhost:8080/elasticsearch/getDocument?indexName=testindex&id=$(DOCUMENT_ID)"
	@echo ""
	@echo "======================================================================================================"
	@echo "If you received any errors, you can run it manually, and possibly change 'testindex' to something else"
	@echo 'curl -X POST "http://localhost:8080/elasticsearch/createIndex?indexName=testindex"'
	@echo 'curl -X POST "http://localhost:8080/elasticsearch/createDocument?indexName=testindex" -H "Content-Type: application/json" -d '\''{"field1": "value1", "field2": "value2"}'\'
	@echo 'curl "http://localhost:8080/elasticsearch/getDocument?indexName=testindex&id=DOCUMENT_ID_FROM_createDocument"'

# Cleanup
clean:
	- gradle clean
	- sudo pacman -R jdk18-openjdk --noconfirm
	- sudo pacman -R jdk17-openjdk --noconfirm
	- sudo pacman -R jdk11-openjdk --noconfirm
	- sudo systemctl disable elasticsearch.service
	- sudo systemctl stop elasticsearch.service
	- sudo pacman -R elasticsearch7 --noconfirm
	- sudo rm -rf /var/lib/elasticsearch
	- sudo rm -rf /etc/elasticsearch
	- sudo rm -rf /var/log/elasticsearch
	- sudo userdel elasticsearch
	- sudo groupdel elasticsearch
	- sudo rm /etc/elasticsearch/jvm.options.pacsave
	- sudo rm /etc/elasticsearch/elasticsearch.yml.pacsave


