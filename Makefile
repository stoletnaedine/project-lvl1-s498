.DEFAULT_GOAL := compile-run
run:
	java -cp ./target/classes games.Slot
compile:
	javac -d ./target/classes ./src/main/java/games/Slot.java
clean:
	rm -rf ./target
compile: clean
	mkdir -p ./target/classes
	javac -d ./target/classes ./src/main/java/games/Slot.java
compile-run: compile run
