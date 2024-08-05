import java.util.*;
import javax.imageio.ImageIO;
import java.util.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;

// Main game panel class
class Game extends JPanel {
    private Timer timer; // Timer for the game loop
    private Snake snake; // Snake object
    private Point cherry; // Point object representing the cherry
    private int points = 0; // Current score
    private int best = 0; // Best score
    private BufferedImage image; // Image for the cherry
    private GameStatus status; // Current game status
    private boolean didLoadCherryImage = true; // Flag to check if cherry image loaded successfully

    // Fonts for different text displays
    private static Font FONT_M = new Font("MV Boli", Font.PLAIN, 24);
    private static Font FONT_M_ITALIC = new Font("MV Boli", Font.ITALIC, 24);
    private static Font FONT_L = new Font("MV Boli", Font.PLAIN, 84);
    private static Font FONT_XL = new Font("MV Boli", Font.PLAIN, 150);
    private static int WIDTH = 760; // Game panel width
    private static int HEIGHT = 520; // Game panel height
    private static int DELAY = 50; // Delay for the game loop

    // Constructor
    public Game() {
        try {
            image = ImageIO.read(new File("cherry.png")); // Load cherry image
        } catch (IOException e) {
            didLoadCherryImage = false; // Set flag if image fails to load
        }

        addKeyListener(new KeyListener()); // Add key listener for controlling the snake
        setFocusable(true); // Set focusable to true for key events
        setBackground(new Color(130, 205, 71)); // Set background color
        setDoubleBuffered(true); // Enable double buffering for smoother rendering

        snake = new Snake(WIDTH / 2, HEIGHT / 2); // Initialize snake in the center
        status = GameStatus.NOT_STARTED; // Set initial game status
        repaint(); // Repaint the panel
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        render(g); // Render the game
        Toolkit.getDefaultToolkit().sync(); // Sync the graphics
    }

    // Method to update the game state
    private void update() {
        snake.move(); // Move the snake

        if (cherry != null && snake.getHead().intersects(cherry, 20)) {
            snake.addTail(); // Add a segment to the snake
            cherry = null; // Remove the cherry
            points++; // Increase score
        }

        if (cherry == null) {
            spawnCherry(); // Spawn a new cherry
        }

        checkForGameOver(); // Check if the game is over
    }

    // Method to reset the game
    private void reset() {
        points = 0; // Reset points
        cherry = null; // Reset cherry
        snake = new Snake(WIDTH / 2, HEIGHT / 2); // Reset snake
        setStatus(GameStatus.RUNNING); // Set status to running
    }

    // Method to set the game status
    private void setStatus(GameStatus newStatus) {
        switch (newStatus) {
            case RUNNING:
                timer = new Timer(); // Initialize timer
                timer.schedule(new GameLoop(), 0, DELAY); // Schedule game loop
                break;
            case PAUSED:
                timer.cancel(); // Cancel timer
                break;
            case GAME_OVER:
                timer.cancel(); // Cancel timer
                best = points > best ? points : best; // Update best score
                break;
        }
        status = newStatus; // Update status
    }

    // Method to toggle pause
    private void togglePause() {
        setStatus(status == GameStatus.PAUSED ? GameStatus.RUNNING : GameStatus.PAUSED);
    }

    // Check if the snake has hit the wall or itself
    private void checkForGameOver() {
        Point head = snake.getHead();
        boolean hitBoundary = head.getX() <= 20
                || head.getX() >= WIDTH + 10
                || head.getY() <= 40
                || head.getY() >= HEIGHT + 30;

        boolean ateItself = false;

        for (Point t : snake.getTail()) {
            ateItself = ateItself || head.equals(t);
        }

        if (hitBoundary || ateItself) {
            setStatus(GameStatus.GAME_OVER); // Set status to game over
        }
    }

    // Method to draw centered strings
    public void drawCenteredString(Graphics g, String text, Font font, int y) {
        FontMetrics metrics = g.getFontMetrics(font);
        int x = (WIDTH - metrics.stringWidth(text)) / 2;

        g.setFont(font);
        g.drawString(text, x, y);
    }

    // Method to render the game
    private void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.BLACK);
        g2d.setFont(FONT_M);

        if (status == GameStatus.NOT_STARTED) {
            drawCenteredString(g2d, "SNAKE", FONT_XL, 200);
            drawCenteredString(g2d, "GAME", FONT_XL, 300);
            drawCenteredString(g2d, "Press Enter key to begin", FONT_M_ITALIC, 330);
            return;
        }

        Point p = snake.getHead();

        g2d.drawString("SCORE: " + String.format("%02d", points), 20, 30);
        g2d.drawString("BEST: " + String.format("%02d", best), 630, 30);

        if (cherry != null) {
            if (didLoadCherryImage) {
                g2d.drawImage(image, cherry.getX(), cherry.getY(), 60, 60, null);
            } else {
                g2d.setColor(Color.BLACK);
                g2d.fillOval(cherry.getX(), cherry.getY(), 10, 10);
                g2d.setColor(Color.BLACK);
            }
        }

        if (status == GameStatus.GAME_OVER) {
            drawCenteredString(g2d, "Press enter to start again", FONT_M_ITALIC, 330);
            drawCenteredString(g2d, "GAME OVER", FONT_L, 300);
        }

        if (status == GameStatus.PAUSED) {
            g2d.drawString("Paused", 600, 14);
        }

        g2d.setColor(new Color(33, 70, 199));
        g2d.fillRect(p.getX(), p.getY(), 10, 10);

        for (int i = 0, size = snake.getTail().size(); i < size; i++) {
            Point t = snake.getTail().get(i);
            g2d.fillRect(t.getX(), t.getY(), 10, 10);
        }

        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(4));
        g2d.drawRect(20, 40, WIDTH, HEIGHT);
    }

    // Method to spawn a cherry at a random location
    public void spawnCherry() {
        cherry = new Point((new Random()).nextInt(WIDTH - 60) + 20,
                (new Random()).nextInt(HEIGHT - 60) + 40);
    }

    // Key listener for controlling the snake
    private class KeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (status == GameStatus.RUNNING) {
                switch (key) {
                    case KeyEvent.VK_LEFT:
                        snake.turn(Direction.LEFT);
                        break;
                    case KeyEvent.VK_RIGHT:
                        snake.turn(Direction.RIGHT);
                        break;
                    case KeyEvent.VK_UP:
                        snake.turn(Direction.UP);
                        break;
                    case KeyEvent.VK_DOWN:
                        snake.turn(Direction.DOWN);
                        break;
                }
            }

            if (status == GameStatus.NOT_STARTED) {
                setStatus(GameStatus.RUNNING);
            }

            if (status == GameStatus.GAME_OVER && key == KeyEvent.VK_ENTER) {
                reset();
            }

            if (key == KeyEvent.VK_P) {
                togglePause();
            }
        }
    }

    // Game loop class for updating and repainting the game
    private class GameLoop extends java.util.TimerTask {
        public void run() {
            update();
            repaint();
        }
    }
}

// Enumeration for game status
enum GameStatus {
    NOT_STARTED, RUNNING, PAUSED, GAME_OVER
}

// Enumeration for snake direction
enum Direction {
    UP, DOWN, LEFT, RIGHT;

    public boolean isX() {
        return this == LEFT || this == RIGHT;
    }

    public boolean isY() {
        return this == UP || this == DOWN;
    }
}

// Point class representing a point on the screen
class Point {
    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point p) {
        this.x = p.getX();
        this.y = p.getY();
    }

    public void move(Direction d, int value) {
        switch (d) {
            case UP:
                this.y -= value;
                break;
            case DOWN:
                this.y += value;
                break;
            case RIGHT:
                this.x += value;
                break;
            case LEFT:
                this.x -= value;
                break;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Point setX(int x) {
        this.x = x;
        return this;
    }

    public Point setY(int y) {
        this.y = y;
        return this;
    }

    public boolean equals(Point p) {
        return this.x == p.getX() && this.y == p.getY();
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public boolean intersects(Point p) {
        return intersects(p, 10);
    }

    public boolean intersects(Point p, int tolerance) {
        int diffX = Math.abs(x - p.getX());
        int diffY = Math.abs(y - p.getY());

        return this.equals(p) || (diffX <= tolerance && diffY <= tolerance);
    }
}

// Snake class representing the snake
class Snake {
    private Direction direction;
    private Point head;
    private ArrayList<Point> tail;

    public Snake(int x, int y) {
        this.head = new Point(x, y);
        this.direction = Direction.RIGHT;
        this.tail = new ArrayList<Point>();

        // Initialize snake with a default length of 3
        this.tail.add(new Point(0, 0));
        this.tail.add(new Point(0, 0));
        this.tail.add(new Point(0, 0));
    }

    public void move() {
        ArrayList<Point> newTail = new ArrayList<Point>();

        for (int i = 0, size = tail.size(); i < size; i++) {
            Point previous = i == 0 ? head : tail.get(i - 1);
            newTail.add(new Point(previous.getX(), previous.getY()));
        }

        this.tail = newTail;
        this.head.move(this.direction, 10);
    }

    public void addTail() {
        this.tail.add(new Point(-10, -10));
    }

    public void turn(Direction d) {
        if (d.isX() && direction.isY() || d.isY() && direction.isX()) {
            direction = d;
        }
    }

    public ArrayList<Point> getTail() {
        return this.tail;
    }

    public Point getHead() {
        return this.head;
    }
}

// Main class for the game window
public class Main extends JFrame {
    public Main() {
        initUI();
    }

    private void initUI() {
        add(new Game()); // Add the game panel

        setTitle("Snake"); // Set window title
        setSize(800, 610); // Set window size

        setLocationRelativeTo(null); // Center window
        setResizable(false); // Disable window resizing
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Set default close operation
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Main ex = new Main(); // Create main window
            ex.setVisible(true); // Make window visible
        });
    }
}
