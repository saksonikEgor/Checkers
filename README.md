# Checkers
A game of checkers has two game modes: person vs person (game between two users) and person vs computer (game between user and AI). 
There's a possibility to save batches and return to them by a special menu.
During the gaming process each move is displayed in a special list located to the right of game board.
Provided return possibility to last move during the game by clicking to correcpoding button.

## Running

### 1. Clone the application
```
git clone https://github.com/saksonikEgor/Checkers.git
cd Checkers
```
### 2. Compile
```
javac -sourcepath ./src -d bin src/checkers/view/MainWindow.java
```
### 3. Run
```
java -classpath ./bin checkers/view/MainWindow
```
