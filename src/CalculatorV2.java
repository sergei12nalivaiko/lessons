import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculatorV2 extends JFrame implements ActionListener {
    private final JTextField display;
    private boolean isResultShown = false;

    public CalculatorV2() {
        setTitle("Калькулятор");
        setSize(340, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));

        display = new JTextField("0");
        display.setFont(new Font("Arial", Font.BOLD, 24));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        add(display, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 4, 5, 5));

        String[] buttons = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "C", "0", "=", "+"
        };

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.BOLD, 20));
            button.addActionListener(this);
            buttonPanel.add(button);
        }

        add(buttonPanel, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        String currentText = display.getText();

        if ("0123456789".contains(command)) {
            if (currentText.equals("0") || currentText.equals("Ошибка") || isResultShown) {
                display.setText(command);
                isResultShown = false;
            } else {
                display.setText(currentText + command);
            }
        } else if ("+-*/".contains(command)) {
            if (currentText.equals("Ошибка")) {
                return;
            }
            if (isResultShown) {
                String resultPart = currentText.split(" = ")[1];
                display.setText(resultPart + " " + command + " ");
                isResultShown = false;
            } else if (currentText.endsWith(" ")) {
                display.setText(currentText.substring(0, currentText.length() - 3) + " " + command + " ");
            } else if (currentText.contains(" ")) {
                String res = evaluateExpression(currentText);
                if (res.equals("Ошибка")) {
                    display.setText(res);
                    isResultShown = true;
                } else {
                    display.setText(res + " " + command + " ");
                }
            } else {
                display.setText(currentText + " " + command + " ");
            }
        } else if (command.equals("=")) {
            if (isResultShown || !currentText.contains(" ") || currentText.endsWith(" ")) {
                return;
            }
            String result = evaluateExpression(currentText);
            if (result.equals("Ошибка")) {
                display.setText("Ошибка");
            } else {
                display.setText(currentText + " = " + result);
            }
            isResultShown = true;
        } else if (command.equals("C")) {
            display.setText("0");
            isResultShown = false;
        }
    }

    private String evaluateExpression(String expr) {
        String[] parts = expr.split(" ");
        if (parts.length < 3) {
            return expr;
        }

        double a = Double.parseDouble(parts[0]);
        String op = parts[1];
        double b = Double.parseDouble(parts[2]);

        double result = switch (op) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> {
                if (b == 0) yield Double.NaN;
                yield a / b;
            }
            default -> b;
        };

        if (Double.isNaN(result)) {
            return "Ошибка";
        }
        return formatResult(result);
    }

    private String formatResult(double value) {
        if (value == (long) value) {
            return String.format("%d", (long) value);
        }
        return String.valueOf(value);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CalculatorV2().setVisible(true);
        });
    }
}
