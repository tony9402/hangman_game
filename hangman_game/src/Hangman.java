import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.*;
import java.util.*;

public class Hangman {
    ArrayList<String> words = new ArrayList<>();
    ArrayList<String> problems = new ArrayList<>();
    ArrayList<Integer> choose_word_idx = new ArrayList<Integer>();
    String currentSolution;
    int tryCount = 0;
    int correctCount = 0;
    int wrongCount = 0;
    int problemIndex = 0;
    static Frame frame = new Frame();
    
    public Hangman() { }
    public void load(String path) {
        try {
            File file = new File(path);
            Scanner sc = new Scanner(file);
            
            while(sc.hasNext()) {
                String word = sc.nextLine();
                
                word = preprocessing(word);
                // Rule 3에 의하여 3글자에서 숨길 글자가 없음
                // 그래서 3글자 이하는 사용안하도록 변경
                if (word.length() <= 3) continue; 
                words.add(word);
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        
        // 미리 뽑아놓기 15단어 (최대 12단어이므로 그 이상으로 뽑아놓음)
        while(problems.size() < 15) {
            int index = getRandomIndex();
            while (choose_word_idx.contains(index)) {
                index = getRandomIndex();
            }
            choose_word_idx.add(index);
            String problem = getRandomWordMask(words.get(index)); 
            problems.add(problem);
        }
        
        frame.Initialization(this);
        show();
    }
    
    public String preprocessing(String word) {
        StringBuilder sb = new StringBuilder();
        for(char ch : word.toCharArray()) {
            if( !Character.isLetter(ch) ) return "";
            sb.append(Character.toUpperCase(ch));
        }
        return sb.toString();
    }
    
    public int getRandomIndex() {
        int length = words.size();
        return (int)(Math.random() * length);
    }
    
    public String getRandomWordMask(String word) {
        StringBuilder sb = new StringBuilder(word);
        int length = word.length();
        double maxValue = length * 0.3;
        double randomValue = Math.random() * maxValue;
        int randomValueInt = (int)(randomValue + 1); 
        
        ArrayList<Integer> maskIndex = new ArrayList<>();
        
        while(maskIndex.size() < randomValueInt) {
            int randomIndex = (int)(Math.random() * length);
            if (maskIndex.contains(randomIndex)) continue;
            maskIndex.add(randomIndex);
            sb.replace(randomIndex, randomIndex + 1, "-");
        }
        return sb.toString();
    }
    
    // GUI에 글자 업데이트
    public void show() {
        int index = choose_word_idx.get(problemIndex);
        currentSolution = words.get(index);
        frame.currentWordField.setText(problems.get(problemIndex));
        frame.gameStatusField.setText(String.format("%d/5", tryCount));
        frame.correctField.setText(String.format("%d/%d", correctCount, wrongCount));
        frame.inputField.setText("");
    }
    
    // 입력 받은 단어와 정답 비교
    public void update() {
        String userSolution = frame.inputField.getText();
        if(currentSolution.equals(userSolution)) {
            correctCount ++;
            tryCount = 0;
            problemIndex ++;
        }
        else {
            tryCount ++;
            if(tryCount == 5) {
                wrongCount ++;
                tryCount = 0;
                problemIndex ++;
            }
        }
        
        if(wrongCount == 3 || correctCount == 10) {
            close();
        }
        show();
    }
    
    public void close() {
        System.exit(0);
    }
    
    public static void main(String[] args) {
        Hangman game = new Hangman();
        game.load("./src/words.txt");
    }
}

class Frame extends JFrame {
    Hangman hangman;
    
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
        
        JLabel correctLabel = new JLabel("정답/틀림");
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
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    hangman.update();
                }
            }
        });
        
        contentFrame.add(inputField);
        setVisible(true);
    }
    
    public void Initialization(Hangman game) {
        hangman = game;
    }
}
