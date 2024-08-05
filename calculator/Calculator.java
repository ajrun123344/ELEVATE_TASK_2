import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Calculator {
    private JFrame frame;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField resultField;
    private JButton[] numberButtons;
    private JButton addButton;
    private JButton subtractButton;
    private JButton multiplyButton;
    private JButton divideButton;
    private boolean firstFieldActive;

    public Calculator() {
        frame = new JFrame("Simple Calculator");
        frame.setSize(400, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JLabel label1 = new JLabel("Number 1:");
        label1.setBounds(20, 20, 100, 20);
        frame.add(label1);

        textField1 = new JTextField();
        textField1.setBounds(130, 20, 100, 20);
        frame.add(textField1);

        JLabel label2 = new JLabel("Number 2:");
        label2.setBounds(20, 50, 100, 20);
        frame.add(label2);

        textField2 = new JTextField();
        textField2.setBounds(130, 50, 100, 20);
        frame.add(textField2);

        JLabel resultLabel = new JLabel("Result:");
        resultLabel.setBounds(20, 80, 100, 20);
        frame.add(resultLabel);

        resultField = new JTextField();
        resultField.setBounds(130, 80, 100, 20);
        resultField.setEditable(false);
        frame.add(resultField);

        // Creating the number buttons
        numberButtons = new JButton[10];
        for (int i = 0; i < 10; i++) {
            numberButtons[i] = new JButton(String.valueOf(i));
            numberButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton source = (JButton) e.getSource();
                    String digit = source.getText();
                    appendDigit(digit);
                }
            });
        }

        // Placing the number buttons in a grid
        int x = 20, y = 150;
        for (int i = 1; i < 10; i++) {
            numberButtons[i].setBounds(x, y, 50, 50);
            frame.add(numberButtons[i]);
            x += 60;
            if (i % 3 == 0) {
                x = 20;
                y += 60;
            }
        }
        numberButtons[0].setBounds(80, 330, 50, 50);
        frame.add(numberButtons[0]);

        // Creating the arithmetic operation buttons
        addButton = new JButton("Add");
        subtractButton = new JButton("Subtract");
        multiplyButton = new JButton("Multiply");
        divideButton = new JButton("Divide");

        // Setting bounds for arithmetic operation buttons
        addButton.setBounds(250, 150, 100, 50);
        subtractButton.setBounds(250, 210, 100, 50);
        multiplyButton.setBounds(250, 270, 100, 50);
        divideButton.setBounds(250, 330, 100, 50);

        // Adding arithmetic operation buttons to the frame
        frame.add(addButton);
        frame.add(subtractButton);
        frame.add(multiplyButton);
        frame.add(divideButton);

        // Adding action listeners for arithmetic operation buttons
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performOperation("add");
            }
        });

        subtractButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performOperation("subtract");
            }
        });

        multiplyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performOperation("multiply");
            }
        });

        divideButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performOperation("divide");
            }
        });

        textField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                firstFieldActive = true;
            }
        });

        textField2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                firstFieldActive = false;
            }
        });

        // Setting background color
        frame.getContentPane().setBackground(Color.LIGHT_GRAY);

        frame.setVisible(true);
    }

    private void appendDigit(String digit) {
        if (firstFieldActive) {
            textField1.setText(textField1.getText() + digit);
        } else {
            textField2.setText(textField2.getText() + digit);
        }
    }

    private void performOperation(String operation) {
        try {
            double num1 = Double.parseDouble(textField1.getText());
            double num2 = Double.parseDouble(textField2.getText());
            double result = 0;

            switch (operation) {
                case "add":
                    result = num1 + num2;
                    break;
                case "subtract":
                    result = num1 - num2;
                    break;
                case "multiply":
                    result = num1 * num2;
                    break;
                case "divide":
                    if (num2 == 0) {
                        resultField.setText("Error: Division by zero");
                        return;
                    }
                    result = num1 / num2;
                    break;
            }
            resultField.setText(String.valueOf(result));
        } catch (NumberFormatException e) {
            resultField.setText("Error: Invalid input");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Calculator(); // Create and show the GUI
            }
        });
    }
}
