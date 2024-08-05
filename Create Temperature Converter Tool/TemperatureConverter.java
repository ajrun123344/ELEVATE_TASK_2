import javax.swing.*; // Importing Swing components for GUI
import java.awt.*; // Importing AWT classes for GUI components
import java.awt.event.ActionEvent; // Importing ActionEvent class for handling events
import java.awt.event.ActionListener; // Importing ActionListener interface for event listeners

public class TemperatureConverter {
    private JFrame frame; // Declaring JFrame for the main window
    private JTextField inputField; // Declaring JTextField for user input
    private JComboBox<String> fromComboBox; // Declaring JComboBox for selecting the 'from' unit
    private JComboBox<String> toComboBox; // Declaring JComboBox for selecting the 'to' unit
    private JTextField resultField; // Declaring JTextField for displaying the result
    private JButton convertButton; // Declaring JButton for triggering the conversion

    public TemperatureConverter() {
        frame = new JFrame("Temperature Converter"); // Initializing the frame with a title
        frame.setSize(400, 200); // Setting the size of the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Setting the default close operation
        frame.setLayout(null); // Setting the layout manager to null for absolute positioning

        JLabel inputLabel = new JLabel("Input:"); // Creating a label for input
        inputLabel.setBounds(20, 20, 100, 20); // Setting position and size of the label
        frame.add(inputLabel); // Adding the label to the frame

        inputField = new JTextField(); // Creating a text field for input
        inputField.setBounds(130, 20, 100, 20); // Setting position and size of the text field
        frame.add(inputField); // Adding the text field to the frame

        JLabel fromLabel = new JLabel("From:"); // Creating a label for the 'from' unit
        fromLabel.setBounds(20, 50, 100, 20); // Setting position and size of the label
        frame.add(fromLabel); // Adding the label to the frame

        String[] units = { "Celsius", "Fahrenheit", "Kelvin" }; // Array of temperature units
        fromComboBox = new JComboBox<>(units); // Creating a combo box with the units array
        fromComboBox.setBounds(130, 50, 100, 20); // Setting position and size of the combo box
        frame.add(fromComboBox); // Adding the combo box to the frame

        JLabel toLabel = new JLabel("To:"); // Creating a label for the 'to' unit
        toLabel.setBounds(20, 80, 100, 20); // Setting position and size of the label
        frame.add(toLabel); // Adding the label to the frame

        toComboBox = new JComboBox<>(units); // Creating a combo box with the units array
        toComboBox.setBounds(130, 80, 100, 20); // Setting position and size of the combo box
        frame.add(toComboBox); // Adding the combo box to the frame

        JLabel resultLabel = new JLabel("Result:"); // Creating a label for the result
        resultLabel.setBounds(20, 110, 100, 20); // Setting position and size of the label
        frame.add(resultLabel); // Adding the label to the frame

        resultField = new JTextField(); // Creating a text field for the result
        resultField.setBounds(130, 110, 100, 20); // Setting position and size of the text field
        resultField.setEditable(false); // Making the text field non-editable
        frame.add(resultField); // Adding the text field to the frame

        convertButton = new JButton("Convert"); // Creating a button for conversion
        convertButton.setBounds(250, 50, 100, 50); // Setting position and size of the button
        frame.add(convertButton); // Adding the button to the frame

        // Adding an action listener to the button to handle the conversion
        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                convertTemperature(); // Calling the method to convert temperature when button is clicked
            }
        });

        frame.getContentPane().setBackground(Color.LIGHT_GRAY); // Setting background color of the frame
        frame.setVisible(true); // Making the frame visible
    }

    private void convertTemperature() {
        try {
            double inputTemp = Double.parseDouble(inputField.getText()); // Parsing input temperature
            String fromUnit = (String) fromComboBox.getSelectedItem(); // Getting the selected 'from' unit
            String toUnit = (String) toComboBox.getSelectedItem(); // Getting the selected 'to' unit
            double resultTemp = 0; // Initializing result temperature

            // Converting from Celsius to other units
            if (fromUnit.equals("Celsius")) {
                if (toUnit.equals("Celsius")) {
                    resultTemp = inputTemp; // Celsius to Celsius
                } else if (toUnit.equals("Fahrenheit")) {
                    resultTemp = (inputTemp * 9 / 5) + 32; // Celsius to Fahrenheit
                } else if (toUnit.equals("Kelvin")) {
                    resultTemp = inputTemp + 273.15; // Celsius to Kelvin
                }
            }
            // Converting from Fahrenheit to other units
            else if (fromUnit.equals("Fahrenheit")) {
                if (toUnit.equals("Celsius")) {
                    resultTemp = (inputTemp - 32) * 5 / 9; // Fahrenheit to Celsius
                } else if (toUnit.equals("Fahrenheit")) {
                    resultTemp = inputTemp; // Fahrenheit to Fahrenheit
                } else if (toUnit.equals("Kelvin")) {
                    resultTemp = (inputTemp - 32) * 5 / 9 + 273.15; // Fahrenheit to Kelvin
                }
            }
            // Converting from Kelvin to other units
            else if (fromUnit.equals("Kelvin")) {
                if (toUnit.equals("Celsius")) {
                    resultTemp = inputTemp - 273.15; // Kelvin to Celsius
                } else if (toUnit.equals("Fahrenheit")) {
                    resultTemp = (inputTemp - 273.15) * 9 / 5 + 32; // Kelvin to Fahrenheit
                } else if (toUnit.equals("Kelvin")) {
                    resultTemp = inputTemp; // Kelvin to Kelvin
                }
            }

            resultField.setText(String.format("%.2f", resultTemp)); // Displaying the result
        } catch (NumberFormatException e) {
            resultField.setText("Invalid input"); // Handling invalid input
        }
    }

    public static void main(String[] args) {
        new TemperatureConverter(); // Creating an instance of the TemperatureConverter
    }
}
