package UIs;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import Players.User;
import Utils.Main;

public class LoginUI extends JFrame {
    private JTextField username;
    private JPasswordField password;

    private JButton loginButton;
    private JButton registerButton;

    Color mainColor= new Color(236, 72, 153);
    Color secondaryColor = new Color(34, 197, 94);
    Color accent = new Color(190, 24, 93);
    Color bgColor=new Color(250, 220, 238);

    public LoginUI() {
        setTitle("Cat Chess");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //full hd
        setMinimumSize(new Dimension(1920, 1080));
        getContentPane().setBackground(bgColor);

        JPanel imagePanel = new JPanel(new BorderLayout());
        ImageIcon icon = new ImageIcon("./assets/loginIMG.png");
        Image img = icon.getImage();

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);

        imagePanel.add(imageLabel, BorderLayout.CENTER);

        imagePanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Image scaled = img.getScaledInstance(
                        imagePanel.getWidth(),
                        imagePanel.getHeight(),
                        Image.SCALE_SMOOTH
                );
                imageLabel.setIcon(new ImageIcon(scaled));
            }
        });


        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(bgColor);
        imagePanel.setBackground(bgColor);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(bgColor);

        rightPanel.add(panel);

        JLabel title=new JLabel("Welcome Back");
        title.setFont(new Font("Arial", Font.BOLD, 35));
        title.setForeground(accent);
        panel.add(title);

        //spatiu vertical (padding)
        panel.add(Box.createVerticalStrut(20));
        JLabel emailLabel=new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        emailLabel.setForeground(accent);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(emailLabel);

        username=new JTextField();
        username.setMaximumSize(new Dimension(300, 20));
        username.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(username);

        panel.add(Box.createVerticalStrut(20));
        JLabel passwordLabel=new JLabel("Password:");
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        passwordLabel.setForeground(accent);
        panel.add(passwordLabel);

        password=new JPasswordField();
        password.setMaximumSize(new Dimension(300, 20));
        password.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(password);

        panel.add(Box.createVerticalStrut(20));

        loginButton=new JButton("Login");
        loginButton.setForeground(Color.white);
        loginButton.setBackground(mainColor);
        panel.add(loginButton);
        panel.add(Box.createVerticalStrut(20));

// nu stiu sa-l centrez
//        JLabel or=new JLabel(" ---- OR ----");
//        or.setAlignmentX(Component.CENTER_ALIGNMENT);
//        panel.add(or);
//
//        panel.add(Box.createVerticalStrut(20));

        registerButton=new JButton("Create New Account");
        registerButton.setForeground(mainColor);
        registerButton.setBorder(BorderFactory.createLineBorder(mainColor, 5));
        registerButton.setBackground(null);

        panel.add(registerButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginUI();
            }
        });
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUI();
            }
        });


        JSplitPane splitPane=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, imagePanel, rightPanel);
        splitPane.setDividerSize(0);
        splitPane.setDividerLocation(540);
        add(splitPane);
        pack();

    }

    //functia ptr login
    private void loginUI(){
        String emailInput=username.getText();
        String passwordInput=new String(password.getPassword());

        if(emailInput.isEmpty() || passwordInput.isEmpty()){
            JOptionPane.showMessageDialog(this,"Please fill all the fields");
            return;
        }

        Main main=Main.getInstance();

        if(!main.userExists(emailInput)){
            JOptionPane.showMessageDialog(this, "User does not exists, recommend register");
            return;
        }

        User u=main.login(emailInput,passwordInput);
        if(u==null){
            JOptionPane.showMessageDialog(this, "Wrong password");
            return;
        }

        main.setCurrentUser(u);

        openMenu();
    }

    private void registerUI(){
        String emailInput=username.getText();
        String passwordInput=new String(password.getPassword());

        if(emailInput.isEmpty() || passwordInput.isEmpty()){
            JOptionPane.showMessageDialog(this,"Please fill all the fields");
            return;
        }
        Main main=Main.getInstance();
        if(main.userExists(emailInput)){
            JOptionPane.showMessageDialog(this, "Username already exists, recommend login");
            return;

        }

        main.setCurrentUser(new User(emailInput,passwordInput));
        openMenu();
    }

    void openMenu(){
        new MainMenuUi().setVisible(true);
        dispose();
    }

}
