package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import controller.UserController;

public class LoginForm extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginForm() {
        // Window setup
        setTitle("Login Form");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        // Email
        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        // Password
        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        // Login button
        loginButton = new JButton("Login");
        panel.add(loginButton);

        // Add the register button here
        JButton registerButton = new JButton("Go to Register");
        panel.add(registerButton);

        // Add panel to frame
        add(panel);

     // Login button action
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                UserController uc = new UserController();
                boolean success = uc.login(email, password);

                if (success) {
                    dispose(); // close login window
                    new Dashboard(email).setVisible(true); // open dashboard window
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid email or password.");
                }
            }
        });

        // Register button action
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // close login window
                new RegistrationForm().setVisible(true); // open registration window
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}
