package game;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class SnakeGame extends JFrame {

	public SnakeGame() {
		this.setTitle("Snake Game");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		
	   GamePanel panel=new GamePanel();
	   this.add(panel);
	   this.pack();
	   this.setLocationRelativeTo(null);
	   this.setVisible(true);
	}
	public static void main(String[]args) {
		new SnakeGame();
	}
	
}
class GamePanel extends JPanel implements ActionListener{
	
	private static final int TITLE_SIZE=20;
	private static final int WIDTH=600;
	private static final int HEIGHT=600;
	private static final int MAX_SNAKE_LENGTH=(WIDTH*HEIGHT)/(TITLE_SIZE*TITLE_SIZE);
	private int DELAY=200;
	
	private int[]x=new int[MAX_SNAKE_LENGTH];
	private int[]y=new int[MAX_SNAKE_LENGTH];
	private int snakeLength;
	private char direction='R';
	private Timer timer;
	private boolean inGame=true;
	
	private int foodx;
	private int foody;
	private Random random;
	
	private int score=0;
	private int level=1;
	private final int SCORE_PER_LEVEL=5;
	
	public GamePanel() {
		this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
		this.setBackground(Color.BLACK);
		this.setFocusable(true);
		this.addKeyListener(new KeyHandler());
		random=new Random();
		initializeSnake();
		generateFood();
		
		this.requestFocusInWindow();
		
		timer=new Timer(DELAY,this);
		timer.start();
	}
	private void initializeSnake() {
		snakeLength=3;
		for(int i=0;i<snakeLength;i++) {
			x[i]=100-i*TITLE_SIZE;
			y[i]=100;
		}
	}
	
	
	private void generateFood() {
		foodx=random.nextInt(WIDTH/TITLE_SIZE)*TITLE_SIZE;
		foody=random.nextInt(HEIGHT/TITLE_SIZE)*TITLE_SIZE;
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(inGame) {
		drawFood(g);
		drawSnake(g);
		drawScore(g);
		drawLevel(g);
		}
		else
		{
			showGameOver(g);
		}
	}
	
	private void drawSnake(Graphics g) {
		g.setColor(Color.green);
		for(int i=0;i<snakeLength;i++) {
			g.fillRect(x[i], y[i], TITLE_SIZE, TITLE_SIZE);
		}
	}
	
	private void drawFood(Graphics g)
	{
		g.setColor(Color.RED);
		g.fillRect(foodx, foody, TITLE_SIZE, TITLE_SIZE);
	}
	
	private void drawScore(Graphics g) {
		String scoreText="Score: "+score;
		g.setFont(new Font("Helvetiva",Font.PLAIN,20));
		g.drawString(scoreText,10,20);
	}
	
	private void drawLevel(Graphics g) {
		String levelText="Level: "+level;
		g.setColor(Color.WHITE);
		g.drawString(levelText, 10, 40);
	}
	
	private void showGameOver(Graphics g) {
		String message="Game Over";
		g.setColor(Color.RED);
		g.setFont(new Font("Helvetica",Font.BOLD,40));
		g.drawString(message,(WIDTH-getFontMetrics(g.getFont()).stringWidth(message))/2,HEIGHT/2);
		
		String finalScore="Final Score : "+score;
		g.setFont(new Font("Helvetica",Font.PLAIN,30));
		 g.drawString(finalScore, (WIDTH - getFontMetrics(g.getFont()).stringWidth(finalScore)) / 2, (HEIGHT / 2) + 50);
		 
		 String restartMessage="Press Enter to play Again";
		 g.setFont(new Font("Helvetica",Font.PLAIN,20));
		 g.drawString(restartMessage, (WIDTH - getFontMetrics(g.getFont()).stringWidth(restartMessage)) / 2, (HEIGHT / 2) + 70);
		 
	}
	
	
	private void moveSnake() {
		
		for(int i=snakeLength-1;i>0;i--)
		{
			x[i]=x[i-1];
			y[i]=y[i-1];
		}
		switch(direction) {
		case 'U':
			y[0]-=TITLE_SIZE;
			break;
		case 'D':
			y[0]+=TITLE_SIZE;
			break;
		case 'L':
			x[0]-=TITLE_SIZE;
			break;
		case 'R':
			x[0]+=TITLE_SIZE;
			break;
			
		}
	}
	
	
	private void checkFoodCollision() {
		if(x[0]==foodx && y[0]==foody) {
			snakeLength++;
			score++;
			generateFood();
			checkLevelUp();
		}
	}
	private void checkCollisions() {
		
	if(x[0]<0 || x[0]>=WIDTH || y[0]<0 || y[0]>=HEIGHT) {
		inGame=false;
	}
	for(int i=1;i<snakeLength;i++) {
		if(x[0]==x[i] && y[0]==y[i]) {
			
			inGame=false;
		}
	}
	if(!inGame) {
		timer.stop();
	}
	
	}
	
	private void checkLevelUp() {
		if(score%SCORE_PER_LEVEL==0)
		{
			level++;
			increaseSpeed();
		}
	}
	private void increaseSpeed() {
		DELAY-=10;
		timer.setDelay(DELAY);
	}
	
	private void restartGame() {
		score=0;
		level=1;
		DELAY=200;
		initializeSnake();
		generateFood();
		inGame=true;
		timer.setDelay(DELAY);
		timer.start();
		repaint();
	}
	
	private class KeyHandler extends KeyAdapter{
		
		
		public void keyPressed(KeyEvent e) {
			int Key=e.getKeyCode();
			
			if(inGame) {
			
			if(Key==KeyEvent.VK_UP && direction!='D') {
				direction='U';
			}
			else if(Key==KeyEvent.VK_DOWN && direction!='U') {
				direction='D';
			}
			else if(Key==KeyEvent.VK_LEFT && direction!='R') {
				direction='L';
			}
			else if(Key==KeyEvent.VK_RIGHT && direction!='L') {
				direction='R';
			}
		}
			else {
				if(Key==KeyEvent.VK_ENTER) {
					repaint();
					restartGame();
				}
			}
	}
	}
	
	public void actionPerformed(ActionEvent e) {
		if(inGame) {
		moveSnake();
		checkFoodCollision();
		checkCollisions();
		}
		repaint();
	}
}


	
	
