import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

// Represents a student with a name and a list of courses
class Student {
    private String name;
    private List<Course> courses;

    public Student(String name) {
        this.name = name;
        this.courses = new ArrayList<>();
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public String getName() {
        return name;
    }

    public List<Course> getCourses() {
        return courses;
    }
}

// Represents a course with a name, score, and calculated grade
class Course {
    private String name;
    private double score;
    private char grade;

    public Course(String name, double score) {
        this.name = name;
        this.score = score;
        this.grade = calculateGrade(score);
    }

    // Calculate grade based on score
    private char calculateGrade(double score) {
        if (score >= 90) return 'A';
        else if (score >= 80) return 'B';
        else if (score >= 70) return 'C';
        else if (score >= 60) return 'D';
        else return 'F';
    }

    public String getName() {
        return name;
    }

    public double getScore() {
        return score;
    }

    public char getGrade() {
        return grade;
    }
}

// Main class for the grading system application
public class GradingSystem extends JFrame {
    private List<Student> students;
    private JTextField nameField, courseField, scoreField;
    private JTextArea outputArea;
    private JComboBox<String> studentComboBox;

    public GradingSystem() {
        students = new ArrayList<>();
        initUI();
    }

    // Initialize the user interface
    private void initUI() {
        setTitle("Grading System");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Student Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Course Name:"));
        courseField = new JTextField();
        inputPanel.add(courseField);

        inputPanel.add(new JLabel("Score:"));
        scoreField = new JTextField();
        inputPanel.add(scoreField);

        JButton addButton = new JButton("Add Course");
        addButton.addActionListener(e -> addCourse());
        inputPanel.add(addButton);

        studentComboBox = new JComboBox<>();
        inputPanel.add(studentComboBox);

        add(inputPanel, BorderLayout.NORTH);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane, BorderLayout.CENTER);

        JButton viewButton = new JButton("View Grades");
        viewButton.addActionListener(e -> viewGrades());
        add(viewButton, BorderLayout.SOUTH);
    }

    // Add a course for a student
    private void addCourse() {
        String name = nameField.getText().trim();
        String courseName = courseField.getText().trim();
        String scoreText = scoreField.getText().trim();

        if (name.isEmpty() || courseName.isEmpty() || scoreText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double score;
        try {
            score = Double.parseDouble(scoreText);
            if (score < 0 || score > 100) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid score. Please enter a number between 0 and 100.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Student student = findStudent(name);
        if (student == null) {
            student = new Student(name);
            students.add(student);
            studentComboBox.addItem(name);
        }

        student.addCourse(new Course(courseName, score));
        JOptionPane.showMessageDialog(this, "Course added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

        nameField.setText("");
        courseField.setText("");
        scoreField.setText("");
    }

    // View grades for the selected student
    private void viewGrades() {
        String selectedName = (String) studentComboBox.getSelectedItem();
        if (selectedName == null) {
            JOptionPane.showMessageDialog(this, "No student selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Student student = findStudent(selectedName);
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Student not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Course> courses = student.getCourses();
        if (courses.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No courses added for this student.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        StringBuilder output = new StringBuilder("Grades for " + student.getName() + ":\n\n");
        double totalScore = 0;
        for (Course course : courses) {
            output.append(String.format("%s: %.2f (%c)\n", course.getName(), course.getScore(), course.getGrade()));
            totalScore += course.getScore();
        }

        double averageScore = totalScore / courses.size();
        output.append(String.format("\nAverage Score: %.2f\n", averageScore));
        output.append("Overall Grade: " + calculateOverallGrade(averageScore));

        outputArea.setText(output.toString());
    }

    // Find a student by name
    private Student findStudent(String name) {
        for (Student student : students) {
            if (student.getName().equalsIgnoreCase(name)) {
                return student;
            }
        }
        return null;
    }

    // Calculate overall grade based on average score
    private char calculateOverallGrade(double averageScore) {
        if (averageScore >= 90) return 'A';
        else if (averageScore >= 80) return 'B';
        else if (averageScore >= 70) return 'C';
        else if (averageScore >= 60) return 'D';
        else return 'F';
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GradingSystem gs = new GradingSystem();
            gs.setVisible(true);
        });
    }
}
