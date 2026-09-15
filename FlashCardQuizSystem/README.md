# FlashCard Quiz System

A console-based Java flashcard learning and quiz system.

## Features

- Automatically discovers `.txt` topic files
- Loads flashcards from text files
- Randomized quizzes without repeating questions
- Case-insensitive answer checking
- Add new flashcards
- Track correct and incorrect answers
- Custom exception handling
- Uses Java Collections such as `ArrayList` and `HashMap`

## Flashcard File Format

Each line uses:

`Question|Answer`

Example:

`What is a class?|A blueprint for creating objects`

## OOP Concepts Demonstrated

- Classes and objects
- Encapsulation
- Inheritance
- Polymorphism
- Abstraction
- Exception handling

## Data Structures

- `ArrayList` / `List` for storing flashcards
- `HashMap` / `Map` for mapping menu numbers to topic files
- Linear searching to avoid repeating quiz questions

## How to Run

Compile:

```bash
javac *.java
```

Run:

```bash
java Main
```

Make sure the `.txt` topic files are in the same folder as the Java files.
