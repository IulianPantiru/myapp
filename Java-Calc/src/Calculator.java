import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Calculator extends JFrame {

    // ================= NOD ARBORE =================
    class Node {
        String value;
        Node left, right;

        Node(String value) {
            this.value = value;
        }
    }

    // ================= BUTOANE =================
    JButton[] digits = {
            new JButton("0"), new JButton("1"),
            new JButton("2"), new JButton("3"),
            new JButton("4"), new JButton("5"),
            new JButton("6"), new JButton("7"),
            new JButton("8"), new JButton("9")
    };

    JButton[] operators = {
            new JButton("+"),
            new JButton("-"),
            new JButton("*"),
            new JButton("/"),
            new JButton("("),
            new JButton(")"),
            new JButton("="),
            new JButton("C")
    };

    JTextArea area = new JTextArea(3, 20);


    private int pos = 0;
    private String input;

    // ================= MAIN =================
    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        calculator.setSize(400, 350);
        calculator.setTitle("Java Calculator");
        calculator.setResizable(false);
        calculator.setVisible(true);
        calculator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    public Calculator() {

        add(new JScrollPane(area), BorderLayout.NORTH);

        JPanel buttonpanel = new JPanel();
        buttonpanel.setLayout(new GridLayout(5, 4, 5, 5));

        // adaugă cifre
        for (int i = 0; i < 10; i++)
            buttonpanel.add(digits[i]);

        // adaugă operatori
        for (int i = 0; i < operators.length; i++)
            buttonpanel.add(operators[i]);

        add(buttonpanel, BorderLayout.CENTER);

        area.setEditable(false);
        area.setFont(new Font("Arial", Font.BOLD, 18));

        // ================= LISTENER CIFRE =================
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            digits[i].addActionListener(e ->
                    area.append(Integer.toString(finalI))
            );
        }

        // ================= LISTENER OPERATORI =================
        for (int i = 0; i < operators.length; i++) {

            operators[i].addActionListener(e -> {

                String cmd = ((JButton)e.getSource()).getText();

                if (cmd.equals("C")) {
                    area.setText("");
                    return;
                }

                if (cmd.equals("=")) {
                    try {
                        double result = evaluateExpression(area.getText());
                        area.append(" = " + result);
                    } catch (Exception ex) {
                        area.setText("Eroare!");
                    }
                    return;
                }

                area.append(cmd);
            });
        }
    }



    private double evaluateExpression(String expression) {
        input = expression.replaceAll(" ", "");
        pos = 0;
        Node root = parseExpression();
        return evaluateTree(root);
    }

    private Node parseExpression() {
        Node node = parseTerm();

        while (pos < input.length() && (input.charAt(pos) == '+' || input.charAt(pos) == '-')) {

            char op = input.charAt(pos++);
            Node right = parseTerm();

            Node newNode = new Node(String.valueOf(op));
            newNode.left = node;
            newNode.right = right;
            node = newNode;
        }

        return node;
    }

    private Node parseTerm() {
        Node node = parseNr();

        while (pos < input.length() && (input.charAt(pos) == '*' || input.charAt(pos) == '/')) {

            char op = input.charAt(pos++);
            Node right = parseNr();

            Node newNode = new Node(String.valueOf(op));
            newNode.left = node;
            newNode.right = right;
            node = newNode;
        }

        return node;
    }

    private Node parseNr() {

        if (input.charAt(pos) == '(') {
            pos++;
            Node node = parseExpression();
            pos++; // sari peste ')'
            return node;
        }

        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {

            sb.append(input.charAt(pos++));
        }

        return new Node(sb.toString());
    }


    private double evaluateTree(Node root) {

        if (root.left == null && root.right == null) {
            return Double.parseDouble(root.value);
        }

        double left = evaluateTree(root.left);
        double right = evaluateTree(root.right);

        switch (root.value) {
            case "+": return left + right;
            case "-": return left - right;
            case "*": return left * right;
            case "/": return left / right;
        }

        return 0;
    }
}

