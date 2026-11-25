package UIs;

import Utils.Main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EndUI extends JFrame {
    Color mainColor= new Color(236, 72, 153);
    Color secondaryColor = new Color(34, 197, 94);
    Color accent = new Color(190, 24, 93);
    Color bgColor=new Color(250, 220, 238);

    public EndUI(String result, int puncteCastigate, int puncteTotal){
        setTitle("Cat Chess - Result");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 1080);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));
        add(panel);

        String title="";
        if(result.equals("WIN")){
            title="VICTORY";
        } else if(result.equals("LOSE")){
            title="DEFEAT";
        } else if(result.equals("PAT")){
            title="PAT";
        }
        else{
            title="DRAW";
        }

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 60));
        titleLabel.setForeground(accent);
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setBackground(bgColor);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        panel.add(center, BorderLayout.CENTER);

        center.add(Box.createVerticalStrut(80));

        String puncteCastigateString="";
        if(puncteCastigate >=0){
            puncteCastigateString="+"+puncteCastigate;
        } else{
            puncteCastigateString=""+puncteCastigate;
        }

        JLabel puncteCastigateLabel = new JLabel(puncteCastigateString);
        puncteCastigateLabel.setForeground(mainColor);
        puncteCastigateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        puncteCastigateLabel.setFont(new Font("Arial", Font.BOLD, 30));

        center.add(puncteCastigateLabel);

        JLabel puncteTotalLabel = new JLabel("Total Points:"+ puncteTotal+"!");
        puncteTotalLabel.setForeground(mainColor);
        puncteTotalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        puncteTotalLabel.setFont(new Font("Arial", Font.BOLD, 30));

        center.add(Box.createVerticalStrut(20));
        center.add(puncteTotalLabel);

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.setBackground(bgColor);

        JButton playButton = new JButton("Play Again");
        JButton exitButton = new JButton("Exit");

        playButton.setFont(new Font("Arial", Font.BOLD, 18));
        exitButton.setFont(new Font("Arial", Font.BOLD, 18));

        playButton.setBackground(secondaryColor);
        exitButton.setBackground(mainColor);

        playButton.setForeground(Color.WHITE);
        exitButton.setForeground(Color.WHITE);

        buttons.add(playButton);
        buttons.add(exitButton);

        panel.add(buttons, BorderLayout.SOUTH);

        playButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new MainMenuUi().setVisible(true);
                dispose();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.getInstance().write();
                System.exit(0);
            }
        });
    }
}
