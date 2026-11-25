package UIs;

import Game.Game;
import Pieces.Piece;
import Players.Player;
import Points.CapturePoints;
import Points.CapturePointsStrategy;
import Utils.Colors;
import Utils.GameObserver;
import Utils.Main;
import Utils.Position;
import Game.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GameUI extends JFrame {
    private Game game;
    private Colors playerColor;

    private Position selected=null;

    private JButton[][] tabla=new JButton[8][8];
    private JLabel status;
    private JLabel myCapturedLabel;
    private JLabel enemyCapturedLabel;

    private JLabel pointsLabel;
    private JButton resignButton;
    private JButton saveButton;
    private JButton exitButton;

    Color mainColor= new Color(236, 72, 153);
    Color secondaryColor = new Color(34, 197, 94);
    Color accent = new Color(190, 24, 93);
    Color bgColor=new Color(250, 220, 238);

    public GameUI(Game game) {
        this.game = game;
        this.playerColor=game.getPlayers()[0].getColor();

        setTitle("Cat Chess - Game " + game.getGameID());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 1080);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(mainColor);
        panel.setBorder(new EmptyBorder(10,10,10,10));
        add(panel);
        panel.setBackground(bgColor);

        //tabla UI
        status = new JLabel("YOUR TURN");
        //status.setBackground(accent);
        status.setForeground(accent);
        status.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(status,BorderLayout.NORTH);

        JPanel boardPanel = new JPanel(new GridLayout(8,8));
        panel.add(boardPanel,BorderLayout.CENTER);

        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                JButton button=new JButton("");
                button.setFont(new Font("Segoe UI Symbol", Font.BOLD, 20));

                int sum=i+j;
                if(sum%2==0){
                    button.setBackground(Color.WHITE);
                } else{
                    button.setBackground(secondaryColor);
                }

                tabla[i][j]=button;
                //rezolvare bug
                final int a=i;
                final int b=j;
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        onClick(a,b);
                    }
                });
                boardPanel.add(button);
            }
        }

        //piese capturate UI
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(mainColor);
        rightPanel.setPreferredSize(new Dimension(260, 0));
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel capTitle = new JLabel("Captured");
        capTitle.setForeground(Color.WHITE);
        capTitle.setFont(new Font("Arial", Font.BOLD, 25));
        capTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel myTitle = new JLabel("Player's captures:");
        myTitle.setForeground(Color.WHITE);
        myTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        myCapturedLabel = new JLabel("");
        myCapturedLabel.setForeground(Color.WHITE);
        myCapturedLabel.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 20));
        myCapturedLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(capTitle);
        rightPanel.add(Box.createVerticalStrut(10));

        rightPanel.add(myTitle);
        rightPanel.add(Box.createVerticalStrut(6));
        rightPanel.add(myCapturedLabel);
        rightPanel.add(Box.createVerticalStrut(20));
        JLabel enemyTitle = new JLabel("Opponent's capures:");
        enemyTitle.setForeground(Color.WHITE);
        enemyTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        enemyCapturedLabel = new JLabel("");
        enemyCapturedLabel.setForeground(Color.WHITE);
        enemyCapturedLabel.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 20));
        enemyCapturedLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(enemyTitle);
        rightPanel.add(Box.createVerticalStrut(6));
        rightPanel.add(enemyCapturedLabel);

        //butoane UI
        rightPanel.add(Box.createVerticalStrut(25));

        pointsLabel = new JLabel("Points: 0");
        pointsLabel.setForeground(Color.WHITE);
        pointsLabel.setFont(new Font("Arial", Font.BOLD, 18));
        pointsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(pointsLabel);

        rightPanel.add(Box.createVerticalStrut(25));

        resignButton = new JButton("Resign");
        saveButton = new JButton("Save");
        exitButton = new JButton("Exit");

        resignButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        saveButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        exitButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        resignButton.setBackground(accent);
        saveButton.setBackground(secondaryColor);
        exitButton.setBackground(accent);

        resignButton.setForeground(Color.WHITE);
        saveButton.setForeground(Color.WHITE);
        exitButton.setForeground(Color.WHITE);

        resignButton.setMaximumSize(new Dimension(100,50));
        exitButton.setMaximumSize(new Dimension(100,50));
        saveButton.setMaximumSize(new Dimension(100,50));

        resignButton.setPreferredSize(new Dimension(100,50));
        exitButton.setPreferredSize(new Dimension(100,50));
        saveButton.setPreferredSize(new Dimension(100,50));



        rightPanel.add(resignButton);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(saveButton);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(exitButton);
        panel.add(rightPanel, BorderLayout.EAST);

        game.addObserver(new GameObserver() {
            @Override
            public void onMoveMade(Move m) {
                makeBoard();
            }

            @Override
            public void onPieceCaptured(Piece p) {
                checkForCapture();
            }

            @Override
            public void onPlayerSwitch(Player p) {
                if (game.getPlayerIndex() == 0) status.setText("Your Turn");
                else status.setText("Computer's Turn");
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.getInstance().write();
                JOptionPane.showMessageDialog(GameUI.this,"Save Successful");
                new MainMenuUi().setVisible(true);
                dispose();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MainMenuUi().setVisible(true);
                dispose();
            }
        });

        resignButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

               Main main=Main.getInstance();
               main.pointsCalculator(game, "RESIGN");
               main.getAllGames().remove((long) game.getGameID());

               if(main.getCurrentUser()!= null){
                   main.getCurrentUser().getALLGames().remove(game);
               }

               main.write();

               new MainMenuUi().setVisible(true);
               dispose();
            }
        });


        makeBoard();

    }

    //face mutarea efectiva
    void onClick(int i, int j) {
        Board board=game.getBoard();
        Position casutaApasata=UIPositionToChessPosition(i,j);
        Piece piesaApasata= board.getPieceAt(casutaApasata);

        if(game.getPlayerIndex()==0){
            if(selected==null){
                if(piesaApasata==null||piesaApasata.getColor()!=playerColor){
                    return;
                }
                selected=casutaApasata;
                makeBoard();
                return;
            }

            //dublu click-deapasare
            if(casutaApasata.equals(selected)){
                selected=null;
                makeBoard();
                return;
            }

            if(!board.isValidMove(selected, casutaApasata)){
                JOptionPane.showMessageDialog(this, "Invalid move!");
                return;
            }

            Piece piesaDeMutat=board.getPieceAt(selected);

            char promotionType='Q';
            if(piesaDeMutat.type()=='P'&& piesaDeMutat.getColor()==playerColor){
                int y=(int) casutaApasata.getY();
                if(piesaDeMutat.getColor()==Colors.WHITE&& y==8){
                    Character pick=PromotionPrompt();
                    if(pick==null){
                        return;
                    }
                    promotionType=pick;
                } else if(piesaDeMutat.getColor()==Colors.BLACK&& y==1){
                    Character pick=PromotionPrompt();
                    if(pick==null){
                        return;
                    }
                    promotionType=pick;
                }
            }

            try{
                game.getPlayers()[0].makeMoveForUI(selected, casutaApasata, board, promotionType);

                game.addMove(game.getPlayers()[0], selected, casutaApasata, piesaApasata );

                game.switchPlayer();
            } catch(Exception e){
                JOptionPane.showMessageDialog(this, e.getMessage());
                return;
            }

            selected=null;
            makeBoard();
            checkForEnd();
        } else{ //mutarea computerului
            game.automaticComputerMoves();
            game.switchPlayer();
            makeBoard();
            checkForEnd();
        }
    }

    //verificare DRAW-WIN-LOSE
    public void checkForEnd() {
        boolean gata=game.checkForCheckMate();

        if(!gata){
           return;
        }

        Main main=Main.getInstance();
        int puncteCurente=0;
        puncteCurente=main.getCurrentUser().getPoints();

        String result;

        if(game.Remise()){
            result=game.getEndReason();
        } else if(game.getLoser()==1){
            result="WIN";
        } else{
            result="LOSE";
        }

        main.pointsCalculator(game, result);

        int puncteTotal=0;
        puncteTotal=main.getCurrentUser().getPoints();
        int puncteCastigate=puncteTotal-puncteCurente;

        if (main.getAllGames() != null) {
            main.getAllGames().remove((long) game.getGameID());
        }
        if (main.getCurrentUser() != null) {
            main.getCurrentUser().getALLGames().remove(game);
        }

        main.write();

        //in functie de result, EndUI va fi diferit
        new EndUI(result, puncteCastigate, puncteTotal).setVisible(true);
        dispose();

    }

    //ca sa afisam mesaje Your are in check/You gave check
    public void checkForcheck(){
        Board board=game.getBoard();
        Colors computerColor;
        if(playerColor==Colors.WHITE){
            computerColor=Colors.BLACK;
        } else{
            computerColor=Colors.WHITE;
        }

        if(board.isKingInCheck(playerColor)){
            status.setText("Your move - You are in check");
            return;
        }

        if(board.isKingInCheck(computerColor)){
            status.setText("Computer move - You gave check!");
            return;
        }
    }

    //f. auxiliara ptr promovare
    public Character PromotionPrompt() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(new JLabel("Promote pawn to:"));

        JRadioButton q = new JRadioButton("Queen (Q)", true);
        JRadioButton r = new JRadioButton("Rook (R)");
        JRadioButton b = new JRadioButton("Bishop (B)");
        JRadioButton n = new JRadioButton("Knight (N)");

        ButtonGroup g = new ButtonGroup();
        g.add(q);
        g.add(r);
        g.add(b);
        g.add(n);

        p.add(q);
        p.add(r);
        p.add(b);

        p.add(n);

        int choice=JOptionPane.showConfirmDialog(this, p, "Promotion", JOptionPane.OK_CANCEL_OPTION);
        if(choice != JOptionPane.OK_OPTION){
            return null;
        }
        if (r.isSelected()) return 'R';
        if (b.isSelected()) return 'B';
        if (n.isSelected()) return 'N';
        return 'Q';
    }

    //f. ptr crearea tablei - o folosim dupa fiecare mutare
    public void makeBoard(){
        Board board=game.getBoard();

//        if(game.getPlayerIndex()==1){
//            status.setText("Computer's Turn");
//        } else{
//            status.setText("Your Turn");
//        }

        //le punem "" sa dam reset la ele
        //practic redesenam piesele la fiecare mutare
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                tabla[i][j].setText("");

                int sum=i+j;
                if(sum%2==0){
                    tabla[i][j].setBackground(Color.WHITE);
                } else{
                    tabla[i][j].setBackground(secondaryColor);
                }
            }
        }

        for(int col=1; col<=8; col++){
            for(char row='A'; row<='H'; row++){
                Position pos=new Position(row,col);
                Piece p= board.getPieceAt(pos);

                if(p!=null){
                    tabla[8-col][row-'A'].setText(Unicoder(p));
                }
            }
        }

        if(selected!=null){
            Piece selectedPiece=board.getPieceAt(selected);
            if(selectedPiece!=null&& selectedPiece.getColor() == playerColor){
                int[] coordonate=ChessPositionToUIPosition(selected);
                int x=coordonate[0];
                int y=coordonate[1];

//                tabla[x][y].setBackground(Colors.GRAY);
                tabla[x][y].setBackground(Color.LIGHT_GRAY);

                List<Position> possMoves=selectedPiece.getPossibleMoves(board);

                for(Position to:possMoves){
                    if(board.isValidMove(selected, to)){
                        int [] coordonateMoves=ChessPositionToUIPosition(to);
                        int x2=coordonateMoves[0];
                        int y2=coordonateMoves[1];
                        tabla[x2][y2].setBackground(Color.LIGHT_GRAY);
                    }
                }
            }
        }

        checkForCapture();
        checkForcheck();
        revalidate();
        repaint();
    }

    //functie auxiliara ptr label-urile din dreapta
    public void checkForCapture(){
        Player human=game.getPlayers()[0];
        Player computer=game.getPlayers()[1];

        StringBuilder sb=new StringBuilder();
        List<Piece> captures=human.getCapturedPieces();
        for(Piece p:captures){
            sb.append(Unicoder(p)).append(" ");
        }

        myCapturedLabel.setText(sb.toString());

        StringBuilder sb2=new StringBuilder();
        List<Piece> captures2=computer.getCapturedPieces();
        for(Piece p:captures2){
            sb2.append(Unicoder(p)).append(" ");
        }

        enemyCapturedLabel.setText(sb2.toString());

        CapturePointsStrategy cap = new CapturePoints();
        int puncte=cap.calculateCapturePoints(human);
        pointsLabel.setText("Points: "+puncte);
    }


    //transformam piesele in unicode
    public String Unicoder(Piece p) {
        char type=p.type();
        Colors c=p.getColor();

        if (c == Colors.WHITE) {
            switch (type) {
                case 'K': return "\u2654";
                case 'Q': return "\u2655";
                case 'R': return "\u2656";
                case 'B': return "\u2657";
                case 'N': return "\u2658";
                case 'P': return "\u2659";
            }
        } else {
            switch (type) {
                case 'K': return "\u265A";
                case 'Q': return "\u265B";
                case 'R': return "\u265C";
                case 'B': return "\u265D";
                case 'N': return "\u265E";
                case 'P': return "\u265F";
            }
        }
        return p.toString();
    }

    //problema tabela de butoane incepe de la [0][0], dar casutele de sah de la [A][1]
    //practic ca sa convertim
    private Position UIPositionToChessPosition(int i, int j){
        int numar=8-i;
        char litera=(char)('A'+j);
        return new Position(litera,numar);
    }

    //practic invers
    private int[] ChessPositionToUIPosition(Position pos){
        int i=8-(int) pos.getY();
        int j=pos.getX()-'A';
        return new int[]{i,j};
    }
}
