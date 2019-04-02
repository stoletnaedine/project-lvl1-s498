.DEFAULT_GOAL := build-run
run:
	java -jar ./target/project-lvl1-s498-1.0-SNAPSHOT-jar-with-dependencies.jar
clean:
	rm -rf ./target
build-run: build run
build:
	./mvnw verify
update:
	./mvnw versions:update-properties versions:display-plugin-updates
init:
	mvn -N io.takari:maven:wrapper -Dmaven=3.6.0
	chmod +x ./mvnw
