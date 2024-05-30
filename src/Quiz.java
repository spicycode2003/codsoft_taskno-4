import java.util.*;
import java.util.concurrent.*;

class QuizQuestion {
    private String question;
    private List<String> options;
    private int correctOptionIndex;

    public QuizQuestion(String question, List<String> options, int correctOptionIndex) {
        this.question = question;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getCorrectOptionIndex() {
        return correctOptionIndex;
    }
}

class QuizApp {
    private List<QuizQuestion> questions;
    private int score;
    private Timer timer;

    public QuizApp(List<QuizQuestion> questions) {
        this.questions = questions;
        this.score = 0;
        this.timer = new Timer();
    }

    public void startQuiz() {
        Scanner scanner = new Scanner(System.in);
        for (QuizQuestion question : questions) {
            System.out.println(question.getQuestion());
            List<String> options = question.getOptions();
            for (int i = 0; i < options.size(); i++) {
                System.out.println((i + 1) + ". " + options.get(i));
            }
            System.out.print("Your answer (1-" + options.size() + "): ");
            int userChoice = -1;
            final int[] selectedOption = {0};
            final CountDownLatch latch = new CountDownLatch(1);
            TimerTask task = new TimerTask() {
                public void run() {
                    if (selectedOption[0] == 0) {
                        System.out.println("\nTime's up! You didn't select an option.");
                        latch.countDown();
                    }
                }
            };
            timer.schedule(task, 30000); // 30 seconds timer
            try {
                selectedOption[0] = scanner.nextInt();
                latch.countDown();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // clear invalid input
            }
            task.cancel();
            if (selectedOption[0] == question.getCorrectOptionIndex() + 1) {
                System.out.println("Correct!");
                score++;
            } else {
                System.out.println("Incorrect!");
            }
        }
        System.out.println("Quiz ended. Your score: " + score + "/" + questions.size());
        timer.cancel();
    }
}

public class Quiz {
    public static void main(String[] args) {
        List<QuizQuestion> questions = new ArrayList<>();
        questions.add(new QuizQuestion("What is the capital of France?",
                Arrays.asList("London", "Paris", "Rome", "Berlin"), 1));
        questions.add(new QuizQuestion("What is the largest planet in our solar system?",
                Arrays.asList("Mars", "Venus", "Jupiter", "Saturn"), 2));
        questions.add(new QuizQuestion("Which of the following is not a primary color?",
                Arrays.asList("Red", "Blue", "Green", "Yellow"), 3));

        QuizApp quiz = new QuizApp(questions);
        quiz.startQuiz();
    }
}
