package UIs;

import Game.*;
import Players.User;
import Utils.Colors;
import Utils.Main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainMenuUi extends JFrame {
    Color mainColor= new Color(236, 72, 153);
    Color secondaryColor = new Color(34, 197, 94);
    Color accent = new Color(190, 24, 93);
    Color bgColor=new Color(250, 220, 238);
    JLabel emailLabel;

    public MainMenuUi() {
        setTitle("Chess Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //full hd
        setMinimumSize(new Dimension(1920, 1080));
        getContentPane().setBackground(bgColor);

        JPanel panel = new JPanel(new BorderLayout()) {
            Image bg = new ImageIcon("./assets/backgroundPisicos.jpg").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();

                g2.drawImage(bg, 0, 0, getWidth(), getHeight(), this);

                // adaug un chenar pe deasupra cu transparenta
                g2.setColor(new Color(0, 0, 0, 100));
                g2.fillRect(0, 0, getWidth(), getHeight());

                g2.dispose();
            }
        };
        add(panel);

        JPanel topPanel=new JPanel(new BorderLayout());
        topPanel.setBackground(bgColor);
        topPanel.setBorder(new EmptyBorder(15, 20, 10, 20));

        JPanel leftPanelInfo=new JPanel();
        leftPanelInfo.setBackground(bgColor);
        leftPanelInfo.setLayout(new BoxLayout(leftPanelInfo, BoxLayout.Y_AXIS));
        topPanel.add(leftPanelInfo, BorderLayout.WEST);

        JLabel title=new JLabel("Cat Chess");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 60));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanelInfo.add(title);

        JLabel mainMenu=new JLabel("Main Menu");
        mainMenu.setForeground(Color.WHITE);
        mainMenu.setFont(new Font("Arial", Font.PLAIN, 40));
        mainMenu.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanelInfo.add(mainMenu);

        leftPanelInfo.add(Box.createVerticalStrut(10));


        User u= Main.getInstance().getCurrentUser();

        int points=0;
        int activeGames=0;

        if(u!=null){
            points=u.getPoints();
            activeGames=u.getActiveGames().size();
        }

        JLabel pointsLabel=new JLabel("\u2605 Total points: "+points);
        pointsLabel.setForeground(Color.WHITE);
        pointsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel activeGamesLabel=new JLabel("\u2764 Active Games: "+activeGames);
        activeGamesLabel.setForeground(Color.WHITE);
        activeGamesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        //imposibil sa ajungem aici dar pusa ca sa testez interfata grafica fara logare
        if(u==null){
            emailLabel=new JLabel("guest \u263A ");
        } else{
            String email=u.getEmail();
            emailLabel=new JLabel(""+email+"\u263A" );
        }

        emailLabel.setForeground(Color.WHITE);
        emailLabel.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 30));
        topPanel.add(emailLabel,BorderLayout.EAST);
        panel.add(topPanel,BorderLayout.NORTH);

        JPanel centerPanel=new JPanel(new GridBagLayout());
        centerPanel.setBackground(bgColor);
        panel.add(centerPanel,BorderLayout.CENTER);

        JPanel contentPanel=new JPanel();
        contentPanel.setBackground(bgColor);
        contentPanel.setLayout(new BoxLayout(contentPanel,BoxLayout.Y_AXIS));
        centerPanel.add(contentPanel);

        JButton newGameButton=new JButton("\u25B6 New Game");
        JButton resumeGameButton=new JButton("\u23F5 Resume Game");
        JButton logoutGameButton=new JButton("\u21A9 Logout");

        newGameButton.setBackground(accent);
        resumeGameButton.setBackground(accent);
        logoutGameButton.setBackground(accent);

        newGameButton.setForeground(Color.white);
        resumeGameButton.setForeground(Color.white);
        logoutGameButton.setForeground(Color.white);

        newGameButton.setFont(new Font("Segoe UI Symbol",Font.BOLD,20));
        resumeGameButton.setFont(new Font("Segoe UI Symbol",Font.BOLD,20));
        logoutGameButton.setFont(new Font("Segoe UI Symbol",Font.BOLD,20));

        newGameButton.setPreferredSize(new Dimension(800, 50));
        resumeGameButton.setPreferredSize(new Dimension(800, 50));
        logoutGameButton.setPreferredSize(new Dimension(800, 50));

        newGameButton.setMaximumSize(new Dimension(800, 50));
        resumeGameButton.setMaximumSize(new Dimension(800, 50));
        logoutGameButton.setMaximumSize(new Dimension(800, 50));

        newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resumeGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        pointsLabel.setFont(new Font("Segoe UI Symbol", Font.BOLD, 25));
        pointsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        activeGamesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        activeGamesLabel.setFont(new Font("Segoe UI Symbol", Font.BOLD, 25));
        contentPanel.add(pointsLabel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(activeGamesLabel);
        contentPanel.add(Box.createVerticalStrut(50));
        contentPanel.add(newGameButton);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(resumeGameButton);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(logoutGameButton);

        newGameButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JRadioButton whiteButton=new JRadioButton("White");
                JRadioButton blackButton=new JRadioButton("Black");
                whiteButton.setSelected(true);

                ButtonGroup buttonGroup=new ButtonGroup();
                buttonGroup.add(whiteButton);
                buttonGroup.add(blackButton);

                JPanel buttonPanel=new JPanel();
                buttonPanel.setBackground(bgColor);
                buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.Y_AXIS));
                buttonPanel.add(new JLabel("Choose ur color:"));
                buttonPanel.add(whiteButton);
                buttonPanel.add(blackButton);

                int choice=JOptionPane.showConfirmDialog(MainMenuUi.this, buttonPanel,"New Game",JOptionPane.OK_CANCEL_OPTION);
                if(choice != JOptionPane.OK_OPTION){
                    return;
                }
                Colors chosenColor=Colors.WHITE;
                if(whiteButton.isSelected()){
                    chosenColor=Colors.WHITE;
                } else{
                    chosenColor=Colors.BLACK;
                }

                Game game=Main.getInstance().startNewGameForUI(chosenColor);
                new GameUI(game).setVisible(true);
                dispose();
            }
        });

        resumeGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User user = Main.getInstance().getCurrentUser();
                List<Game> activeGames=user.getActiveGames();

                if(activeGames.size()==0){
                    JOptionPane.showMessageDialog(MainMenuUi.this,"There are no active Games");
                    return;
                }

                DefaultListModel<String> jocuri=new DefaultListModel<String>();

                for(Game game:activeGames){
                    jocuri.addElement("Game id:" + game.getGameID());
                }

                JList<String> listaJocuriSelectabila = new JList<String>(jocuri);
                listaJocuriSelectabila.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                listaJocuriSelectabila.setSelectedIndex(0);

                JScrollPane jScrollPane=new JScrollPane(listaJocuriSelectabila);
                jScrollPane.setPreferredSize(new Dimension(400,400));

                int choice = JOptionPane.showConfirmDialog(MainMenuUi.this,jScrollPane,"Resume Game",JOptionPane.OK_CANCEL_OPTION);

                if(choice != JOptionPane.OK_OPTION){
                    return;
                }
                int index= listaJocuriSelectabila.getSelectedIndex();
                Game selected=activeGames.get(index);

                new GameUI(selected).setVisible(true);
                dispose();
            }
        });

        logoutGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.getInstance().setCurrentUser(null);
                new LoginUI().setVisible(true);
                dispose();
            }
        });

        topPanel.setOpaque(false);
        leftPanelInfo.setOpaque(false);
        centerPanel.setOpaque(false);
        contentPanel.setOpaque(false);

        pack();

    }

}
