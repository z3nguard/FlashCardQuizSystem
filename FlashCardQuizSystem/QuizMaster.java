import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;

public class QuizMaster {

    private final Map<String, String> topics;
    private List<FlashCard> cards;
    private Scanner consoleScanner;
    private int correctCount = 0;
    private int incorrectCount = 0;
    private Random random;

    public QuizMaster() {
        this.cards = new ArrayList<>();
        this.consoleScanner = new Scanner(System.in);
        this.random = new Random();
        this.topics = loadAvailableTopics();
    }

    private Map<String, String> loadAvailableTopics() {
        Map<String, String> availableTopics = new HashMap<>();
        File currentDir = new File(".");
        File[] files = currentDir.listFiles();

        if (files == null) {
            System.out.println("Unable to access the current directory.");
            return availableTopics;
        }

        int index = 1;

        for (File file : files) {
            if (file.isFile() && file.getName().toLowerCase().endsWith(".txt")) {
                availableTopics.put(String.valueOf(index), file.getName());
                index++;
            }
        }

        if (availableTopics.isEmpty()) {
            System.out.println("Warning: No .txt topic files were found.");
        }

        return availableTopics;
    }

    public void start() {
        String input;

        do {
            System.out.println("\n===== FLASHCARD QUIZ SYSTEM =====");

            if (topics.isEmpty()) {
                System.out.println("No topics available.");
            } else {
                System.out.println("Available Topics:");

                for (String key : topics.keySet()) {
                    String displayName = topics.get(key)
                            .replace(".txt", "")
                            .toUpperCase()
                            .replace("_", " ");

                    System.out.println(key + ". " + displayName);
                }
            }

            System.out.println("S. Show Statistics");
            System.out.println("X. Exit");
            System.out.print("Choose an option: ");

            input = consoleScanner.nextLine().trim().toUpperCase();

            if (topics.containsKey(input)) {
                try {
                    loadTopicAndRun(topics.get(input));
                } catch (NoCardsLoadedException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else if (input.equals("S")) {
                showStatistics();
            } else if (input.equals("X")) {
                System.out.println("Goodbye!");
            } else {
                System.out.println("Invalid option. Please try again.");
            }

        } while (!input.equals("X"));

        consoleScanner.close();
    }

    private void loadTopicAndRun(String filename) {
        if (!loadCardsFromFile(filename)) {
            return;
        }

        if (cards.isEmpty()) {
            throw new NoCardsLoadedException(
                    "The selected topic file (" + filename + ") contains no valid flashcards."
            );
        }

        System.out.println("\nLoaded " + cards.size() + " flashcards from " + filename);

        String input;

        do {
            System.out.println("\n===== TOPIC MENU =====");
            System.out.println("1. Take Quiz");
            System.out.println("2. Add Card");
            System.out.println("S. Show Statistics");
            System.out.println("B. Back");
            System.out.print("Choose an option: ");

            input = consoleScanner.nextLine().trim().toUpperCase();

            switch (input) {
                case "1":
                    takeQuiz();
                    break;
                case "2":
                    writeNewCardToFile(filename);
                    break;
                case "S":
                    showStatistics();
                    break;
                case "B":
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }

        } while (!input.equals("B"));
    }

    private void writeNewCardToFile(String filename) {
        try {
            System.out.print("Enter question: ");
            String question = consoleScanner.nextLine().trim();

            System.out.print("Enter answer: ");
            String answer = consoleScanner.nextLine().trim();

            if (question.isEmpty() || answer.isEmpty()) {
                throw new IllegalArgumentException("Question and answer cannot be empty.");
            }

            try (PrintWriter writer = new PrintWriter(new FileWriter(filename, true))) {
                writer.println(question + "|" + answer);
            }

            cards.add(new FlashCard(question, answer));
            System.out.println("Card added successfully.");

        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private boolean loadCardsFromFile(String filename) {
        cards.clear();

        try (Scanner fileScanner = new Scanner(new File(filename))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();

                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\|");

                if (parts.length < 2) {
                    System.out.println("Skipping corrupted line: " + line);
                    continue;
                }

                String question = parts[0].trim();
                String answer = parts[1].trim();

                if (!question.isEmpty() && !answer.isEmpty()) {
                    cards.add(new FlashCard(question, answer));
                }
            }

            return true;

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
            return false;
        }
    }

    private void takeQuiz() {
        if (cards.isEmpty()) {
            throw new NoCardsLoadedException();
        }

        int numberOfQuestions;

        while (true) {
            System.out.print("How many questions would you like? (1-" + cards.size() + "): ");

            if (consoleScanner.hasNextInt()) {
                numberOfQuestions = consoleScanner.nextInt();
                consoleScanner.nextLine();

                if (numberOfQuestions >= 1 && numberOfQuestions <= cards.size()) {
                    break;
                }
            } else {
                consoleScanner.nextLine();
            }

            System.out.println("Please enter a valid number.");
        }

        List<Integer> askedIndices = new ArrayList<>();
        int quizCorrect = 0;

        for (int i = 0; i < numberOfQuestions; i++) {
            int randomIndex;

            do {
                randomIndex = random.nextInt(cards.size());
            } while (askedIndices.contains(randomIndex));

            askedIndices.add(randomIndex);

            FlashCard card = cards.get(randomIndex);

            System.out.println("\nQuestion " + (i + 1) + ": " + card.getQuestion());
            System.out.print("Your answer: ");

            String userAnswer = consoleScanner.nextLine().trim();

            if (userAnswer.equalsIgnoreCase(card.getAnswer())) {
                System.out.println("Correct!");
                quizCorrect++;
                correctCount++;
            } else {
                System.out.println("Incorrect. Correct answer: " + card.getAnswer());
                incorrectCount++;
            }
        }

        System.out.println("\n===== QUIZ RESULT =====");
        System.out.println("Score: " + quizCorrect + "/" + numberOfQuestions);
    }

    private void showStatistics() {
        int totalAttempted = correctCount + incorrectCount;

        double successRate = totalAttempted == 0
                ? 0
                : (double) correctCount / totalAttempted * 100;

        System.out.println("\n===== STATISTICS =====");
        System.out.println("Current cards: " + cards.size());
        System.out.println("Total attempted: " + totalAttempted);
        System.out.println("Correct: " + correctCount);
        System.out.println("Incorrect: " + incorrectCount);
        System.out.printf("Success rate: %.2f%%%n", successRate);
    }
}
