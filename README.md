# FlashCard Quiz System

## Description

A console-based FlashCard Quiz System developed in Java using Object-Oriented Programming and data structures.

The program allows users to:

* Select different flashcard topics
* Load flashcards from `.txt` files
* Take randomized quizzes
* Add new flashcards
* Track correct and incorrect answers
* View overall quiz statistics
* Handle errors using exception handling

## Data Structures Used

* `ArrayList` — stores flashcard objects
* `HashMap` — maps topic numbers to topic files
* `List` — manages collections of flashcards

## OOP Concepts

* Classes and Objects
* Encapsulation
* Inheritance
* Polymorphism
* Abstraction

## Exception Handling

The project uses:

* `FileNotFoundException`
* `IOException`
* `IllegalArgumentException`
* Custom `NoCardsLoadedException`

## File Format

Flashcards are stored in `.txt` files using:

```text
Question|Answer
```

Example:

```text
What is inheritance?|A mechanism where one class inherits another class
What is encapsulation?|Wrapping data and methods into a single unit
```

## How to Run

Compile the project:

```bash
javac Main.java QuizMaster.java FlashCard.java NoCardsLoadedException.java
```

Run:

```bash
java Main
```

Make sure the `.txt` flashcard files are in the same directory from which the program is run.
