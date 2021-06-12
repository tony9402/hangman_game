import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class Hangman {
    ArrayList<String> words = new ArrayList<>();
    ArrayList<String> problems = new ArrayList<>();
    ArrayList<Integer> choose_word_idx = new ArrayList<Integer>();
    String currentSolution;
    int tryCount = 0;      // 시도 횟수
    int correctCount = 0;  // 성공 횟수
    int wrongCount = 0;    // 실패 횟수
    int problemIndex = 0;  // 현재 라운드
    final int maxWordCount = 15;
    static Frame frame = new Frame(); // GUI Initialization
    
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
        gameInitialization(maxWordCount);
        
        frame.Initialization(this);
        show();
    }
    
    // words.txt에서 단어 wordCount
    public void gameInitialization(int wordCount) {
        while(problems.size() < wordCount) {
            int index = getRandomIndex();
            // 이미 뽑은거면 다시 뽑기
            while (choose_word_idx.contains(index)) {
                index = getRandomIndex();
            }

            // 뽑은 정보 저장
            choose_word_idx.add(index);

            // 단어에서 글자 숨기기
            String problem = getRandomWordMask(words.get(index)); 
            
            // 글자 숨긴 단어
            problems.add(problem);
        }
    }

    // words.txt에 있는 단어 전처리
    public String preprocessing(String word) {
        StringBuilder sb = new StringBuilder();
        for(char ch : word.toCharArray()) {
            // 알파벳이 아닌 건 제외 
            //  -> 단어의 길이가 0으로 return 하면 다음 작업에서 제외
            if( !Character.isLetter(ch) ) return "";

            // 알파벳이면 대문자로 변경
            sb.append(Character.toUpperCase(ch));
        }
        return sb.toString();
    }
    
    // 전처리 후 정제된 데이터에서 랜덤으로 선택
    public int getRandomIndex() {
        int length = words.size();
        return (int)(Math.random() * length);
    }
    
    // 랜덤으로 단어에서 몇글자 가림
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
        // 맞췄을 경우
        if(currentSolution.equals(userSolution)) {
            correctCount ++;
            tryCount = 0;
            problemIndex ++;
        }
        // 틀렸을 경우
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
