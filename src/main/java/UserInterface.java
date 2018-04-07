import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class UserInterface {
    private JFrame mainFrame;
    private JPanel controlPanel;
    private String[] icons;
    private String[] names;
    private final String BASE = "C:\\Users\\mdpun\\Documents\\HQApptitude\\src\\main\\resources\\hq-trivia-question-";

    public UserInterface(){
        prepareGUI();
    }
    public static void main(String[] args){
        UserInterface  swingControl = new UserInterface();
        swingControl.showComboDemo();
    }
    private void prepareGUI(){
        mainFrame = new JFrame("HQ Apptitude");
        mainFrame.setSize(400,550);
        mainFrame.setLayout(new GridLayout(1, 1));

        names = new String[]{"01.jpg", "02.jpg", "03.jpg", "04.jpg", "05.jpg"};
        icons = new String[names.length];
        for (int i = 0; i < names.length; i++) {
            icons[i] = BASE + names[i];
        }

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                System.exit(0);
            }
        });

        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        mainFrame.add(controlPanel);
        mainFrame.setVisible(true);
    }

    private void showComboDemo(){
        //resources folder should be inside SWING folder.
        ImageIcon icon = new ImageIcon(icons[0]);
        JLabel pic = new JLabel(icon);
        JComboBox<String> box = new JComboBox<>(names);
        controlPanel.add(pic);

        box.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageIcon icon = new ImageIcon(icons[box.getSelectedIndex()]);
                pic.setIcon(icon );
                controlPanel.updateUI();
            }
        });

        JButton compute = new JButton("Compute");
        compute.setHorizontalTextPosition(SwingConstants.LEFT);

        compute.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TextRecognition textRecognition = null;
                try {
                    textRecognition = new TextRecognition(icons[box.getSelectedIndex()], 4);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                try {
                    JOptionPane.showMessageDialog(mainFrame, textRecognition.getAnswers() , "Results",  JOptionPane.PLAIN_MESSAGE);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        controlPanel.add(compute);
        controlPanel.add(box);

        mainFrame.setVisible(true);
    }
}