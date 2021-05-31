import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class Main {
	static Frame frame;
    public static void main(String[] args) {
    	frame = new Frame();
    	
    }
}

class Frame extends JFrame {
    JPanel contentFrame, inputFrame;
    JTextField inputField, currentWordField, gameStatusField, correctField;
    
    public Frame() {
        this.setResizable(false);
        setTitle("Hangman Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 305, 150);
        
        contentFrame = new JPanel();
        contentFrame.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        setContentPane(contentFrame);
        contentFrame.setLayout(null);
        
        JLabel currentWordFieldName = new JLabel("현재 단어");
        currentWordFieldName.setFont(new Font("굴림", Font.PLAIN, 12));
        currentWordFieldName.setBounds(15, 5, 100, 30);
        contentFrame.add(currentWordFieldName);
        
        currentWordField = new JTextField();
        currentWordField.setBounds(75, 5, 225, 30);
        currentWordField.setEditable(false);
        contentFrame.add(currentWordField);
        
        JLabel currenStatusLabel = new JLabel("시도 횟수");
        currenStatusLabel.setFont(new Font("굴림", Font.PLAIN, 12));
        currenStatusLabel.setBounds(15, 45, 100, 30);
        contentFrame.add(currenStatusLabel);
        
        gameStatusField = new JTextField();
        gameStatusField.setBounds(75, 45, 80, 30);
        gameStatusField.setEditable(false);
        contentFrame.add(gameStatusField);
        
        JLabel correctLabel = new JLabel("맞춘 횟수");
        correctLabel.setFont(new Font("굴림", Font.PLAIN, 12));
        correctLabel.setBounds(165, 45, 100, 30);
        contentFrame.add(correctLabel);
        
        correctField = new JTextField();
        correctField.setBounds(220, 45, 80, 30);
        correctField.setEditable(false);
        contentFrame.add(correctField);
        
        JLabel inputFieldLabel = new JLabel("단어 입력");
        inputFieldLabel.setFont(new Font("굴림", Font.PLAIN, 12));
        inputFieldLabel.setBounds(15, 85, 100, 30);
        contentFrame.add(inputFieldLabel);
        
        inputField = new JTextField();
        inputField.setBounds(75, 85, 225, 30);
        contentFrame.add(inputField);
        
        setVisible(true);
    }
}
