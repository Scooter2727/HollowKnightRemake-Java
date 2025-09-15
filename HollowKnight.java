//      IMPORTS     //
import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.event.*;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class HollowKnight extends JPanel implements KeyListener, Runnable
{
                        //      SETTINGS & GAME STATES      //

    // Game States
    // G.S 0 - Home State
    // G.S 1 - Controls State
    // G.S 2 - Level 1 (Tutorial)
    // G.S 3 - Level 2
    // G.S 4 - Level 3 (Final Boss)
    // G.S 5 - Win State
    // G.S 6 - Game Over State
    // G.S 7 - High Score Calculator
    // G.S 8 - Input Name Screen
    // G.S 9 - High Score Screen

    // Things to do
    // Fix the attacking at the beginning of the game?
    // Add images to the controls screen
    // Add images to the high score screen


    public static boolean resetEnemy = false;

    public static int gameState = 0; // The current game state
    public static int scoreTimer = 0; // The timer to track the score
    public static int bossAnimationTimer = 0; // The timer to set all of the boss animations
    public static int playerAttackTimer = 10;

    public static boolean enemyRight = false; // Checks if the enemy is facing right

    public static String name = ""; // The variable to store the name
    public static int score = 1000; // Placeholder score

    public static int cursorY = 200; // The y-position of the dash in the controls screen
    public static int cursorX = 50; // The x-position of the dash in the controls screen

    // Background Images
    public static BufferedImage titleScreen;
    public static BufferedImage gameOverScreen;
    public static BufferedImage winScreen;
    public static BufferedImage controlsScreen;
    public static BufferedImage inputNameScreen;
    public static BufferedImage highScoreScreen;
    public static BufferedImage background1;
    public static BufferedImage background2;
    public static BufferedImage background3;

    // Extra Image Settings
    public static BufferedImage health1;
    public static BufferedImage health2;
    public static BufferedImage health3;
    public static BufferedImage health4;
    public static BufferedImage health5;
    public static BufferedImage fullSoul;
    public static BufferedImage emptySoul;

    public static String name1;
    public static String name2;
    public static String name3;
    public static String name4;
    public static String name5;

    public static int highScore1;
    public static int highScore2;
    public static int highScore3;
    public static int highScore4;
    public static int highScore5;

    public static int leftKey = 65;
    public static char leftKeyChar = 'a';

    public static int rightKey = 68;
    public static char rightKeyChar = 'd';

    public static int dashKey = 76;
    public static char dashKeyChar = 'l';

    public static int attackKey = 74;
    public static char attackKeyChar = 'j';

    public static int healKey = 75;
    public static char healKeyChar = 'k';

    public static int jumpKey = 32;
    public static char jumpKeyChar = ' ';

                        //      PLAYER GAME STATS       //

    // Position/Movement Stats
    public static int posX = 25; // Start x-pos of the player
    public static int posY = 525; // Start y-pos of the player
    public static boolean leftPressed = false; // Checks if the 'a' key is pressed
    public static boolean rightPressed = false; // Checks if the 'd' key is pressed
    public static boolean playerRight = true; // Checks what direction the player is facing

    // Jump Stats
    public static int jumpVelocityPlayer = -36; // The jump velocity for the player
    public static int gravityPlayer = 3; // The amount of gravity for the player
    public static boolean jumpedPlayer = false; // Checks if the player is jumping

    // Core Stats
    public static int playerSpeed = 8; // The speed of the player
    public static int dashLength = 175; // The length of the dash
    public static int playerHealth = 5; // The player's health
    public static int soulMeter = 0; // Tracks the amount of "soul" the player has
    public static int attackDelay = 0; // Sets a delay for attacking
    public static int dashDelay = 0; // Sets a delay for dashing
    public static int playerHitFrame; // Allows animations for when the player is hit
    public static boolean isSlash = false; // Checks if the player is attacking

    public static boolean newGame = false;

    // Animation Stats
    public static int curPlayerFrame = 0; // Checks what frame of the player should be outputted
    public static BufferedImage[] playerImg = new BufferedImage[6];

    // Enemy Stats (first level)
    public static int posxEnemy = 250; // The starting x-pos of the enemy
    public static int posyEnemy = 475; // The starting y-pos of the enemy
    public static BufferedImage[] enemy = new BufferedImage[4];
    public static int enemyHealth = 5; // The enemy health
    public static int enemyAttackDelay = 0; // The enemy attack delay
    public static int curEnemyFrame = 0; // The current outputted frame for the enemy
    public static int enemyHitFrame = 0; // Allows the enemy hit animations (flicker)

    // Boss 1 Stats
    public static BufferedImage[] boss1Img = new BufferedImage[4];
    public static int boss1Speed = 11; // The speed of the first boss
    public static int posxBoss1 = 0; // The starting x-pos for the first boss
    public static int posyBoss1 = 0; // The starting y-pos for the first boss
    public static int boss1Frame = 1; // The frame counter for the first boss
    public static boolean boss1Up = false; // Checks if the first boss is going up
    public static boolean boss1Left = false; // Checks if the first boss is going left
    public static int boss1Health = 10; // Starting health for the first boss

    // Boss 2 Stats
    public static BufferedImage[] boss2Img = new BufferedImage[2];
    public static BufferedImage boss2Beam; // The image for the final boss's attack
    public static int posxBoss2 = 640; // The starting x-pos for the final boss
    public static int posyBoss2 = 125; // The starting y-pos for the final boss
    public static int boss2Frame = 0; // The frame counter for the final boss
    public static int boss2Health = 15; // The starting health for the final boss
    public boolean boss2Right = true; // Checks if the final boss is going right
    public static int beamAttackTimer = 0; // The delay in between attacks
    public static boolean boss2Attacking = false; // Checks if the final boss is attacking
    public static boolean boss2Animation = false;

                        //      DEFAULT SETTINGS AND IMAGE IMPORTATION      //

    public HollowKnight()
    {
        setPreferredSize(new Dimension(1280, 720)); // Dimensions of screen

        // Add keyListener
        this.setFocusable(true);
        addKeyListener(this);

        // Add Threading
        Thread thread = new Thread(this);
        thread.start();

                        //      IMAGE IMPORTATION       //

        try
        {
            // LVL 1 Enemy Images
            enemy[0] = ImageIO.read(new File("assets/bugLeft.png"));
            enemy[1] = ImageIO.read(new File("assets/bugRight.png"));
            enemy[2] = ImageIO.read(new File("assets/bugLeft2.png"));
            enemy[3] = ImageIO.read(new File("assets/bugRight2.png"));

            // LVL 2 Boss Images
            boss1Img[0] = ImageIO.read(new File("assets/gruzLeft.png"));
            boss1Img[1] = ImageIO.read(new File("assets/gruzRight.png"));
            boss1Img[2] = ImageIO.read(new File("assets/gruzLeft2.png"));
            boss1Img[3] = ImageIO.read(new File("assets/gruzRight2.png"));

            // LVL 3 Boss Images
            boss2Img[0] = ImageIO.read(new File("assets/radianceIdle.png"));
            boss2Img[1] = ImageIO.read(new File("assets/radianceAttack.png"));
            boss2Beam = ImageIO.read(new File("assets/beamAttackAnimation.png"));

            // Background Images
            // Fix incorrect path: use the existing background image for level 1
            background1 = ImageIO.read(new File("assets/background.png"));
            background2 = ImageIO.read(new File("assets/background.png"));
            background3 = ImageIO.read(new File("assets/boss2Background.png"));
            titleScreen = ImageIO.read(new File("assets/titleScreen.png"));
            gameOverScreen = ImageIO.read(new File("assets/gameOver.png"));
            winScreen = ImageIO.read(new File("assets/winScreen.png"));
            controlsScreen = ImageIO.read(new File("assets/controlsScreen.png"));
            highScoreScreen = ImageIO.read(new File("assets/highScores.png"));
            inputNameScreen = ImageIO.read(new File("assets/inputNameScreen.png"));

            // All player animations
            playerImg[0] = ImageIO.read(new File("assets/walk1R.png")); // Add 2 later
            playerImg[1] = ImageIO.read(new File("assets/walk1L.png")); // Add 2 later
            playerImg[2] = ImageIO.read(new File("assets/attackRight.png"));
            playerImg[3] = ImageIO.read(new File("assets/attackLeft.png"));
            playerImg[4] = ImageIO.read(new File("assets/jumpingRight.png"));
            playerImg[5] = ImageIO.read(new File("assets/jumpingLeft.png"));

            // Health Images
            health1 = ImageIO.read(new File("assets/life1.png"));
            health2 = ImageIO.read(new File("assets/life2.png"));
            health3 = ImageIO.read(new File("assets/life3.png"));
            health4 = ImageIO.read(new File("assets/life4.png"));
            health5 = ImageIO.read(new File("assets/life5.png"));

            // Soul Images
            fullSoul = ImageIO.read(new File("assets/fullSoul.png"));
            emptySoul = ImageIO.read(new File("assets/emptySoul.png"));
        }
        catch (Exception e) {
            // Surface asset loading issues instead of failing silently
            e.printStackTrace();
        }

        // Fallbacks to avoid blank screen if some assets are missing
        if (fullSoul == null) fullSoul = emptySoul;
    }

                        //      PAINTCOMPONENT      //

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g); // clears the screen (redraw wants to clear out what was there before)

        if(gameState == 0) g.drawImage(titleScreen, 0, 0, null); // Draws the title screen
        else if(gameState == 1)
        {
            g.drawImage(controlsScreen, 0, 0, null); // Draws the controls screen
            g.setColor(new Color(255, 255, 255));
            Font font = new Font("Times New Roman", Font.PLAIN, 50);
            g.setFont(font);

            if (leftKeyChar == 32) g.drawString("Left: Space", 100, 190);
            else g.drawString("Left: " + leftKeyChar, 100, 190);

            if (rightKeyChar == 32) g.drawString("Right: Space", 100, 390);
            else g.drawString("Right: " + rightKeyChar, 100, 390);

            if (jumpKeyChar == 32) g.drawString("Jump: Space", 100, 590);
            else g.drawString("Jump: " + jumpKeyChar, 100, 590);

            if (dashKeyChar == 32) g.drawString("Dash: Space", 650, 190);
            else g.drawString("Dash: " + dashKeyChar, 650, 190);

            if (attackKeyChar == 32) g.drawString("Attack: Space", 650, 390);
            else g.drawString("Attack: " + attackKeyChar, 650, 390);

            if (healKeyChar == 32) g.drawString("Heal: Space", 650, 590);
            g.drawString("Heal: " + healKeyChar, 650, 590);

            g.drawString("-", cursorX, cursorY);
        }

        else if(gameState == 2)
        {
                        //      LVL 1 LOGIC & ANIMATION       //

            if (resetEnemy)
            {
                resetVariables();
                resetEnemy = false;
                bossAnimationTimer = 0;
                curEnemyFrame = 0;
            }
            basicMovement();

            // Homing for the first enemy
            if (posX > posxEnemy)
            {
                enemyRight = true;
                posxEnemy += 2;
            }
            if (posX < posxEnemy)
            {
                enemyRight = false;
                posxEnemy -= 2;
            }

            // Animations
            if (bossAnimationTimer == 10)
            {
                if (enemyRight)
                {
                        if (curEnemyFrame == 1) curEnemyFrame = 3;
                        else curEnemyFrame = 1;
                }
                else
                {
                        if (curEnemyFrame == 0) curEnemyFrame = 2;
                        else curEnemyFrame = 0;
                }
                bossAnimationTimer = 0;
            }


            // Collision Detection + Lose Condition
            enemyAttackDelay += 1;
            if (enemyHealth > 0 && enemyAttackDelay > 50)
            {
                if(!(posX + 40 < posxEnemy || posX > posxEnemy + 115 || posY + 100 < posyEnemy || posY > posyEnemy + 150))
                {
                    playerHealth -= 1; // What happens when you lose?
                    playerHitFrame = 5;
                    enemyAttackDelay = 0;
                }
            }

            // Drawing the player, enemy, and background
            g.drawImage(background1,0,0, null); // The background

            drawPlayer(g); // Draw the player

            if (enemyHealth > 0)
            {
                if (enemyHitFrame % 2 == 0) g.drawImage(enemy[curEnemyFrame], posxEnemy, posyEnemy, null); // The enemy
            }
            else
            {
                resetVariables(); // Resets the variables when the enemy dies
                gameState = 3;
            }

            playerHealthSetup(g);  // Player Health

            // Soul Meter
            if (soulMeter > 4) g.drawImage(fullSoul, 80, 65, null);
            else g.drawImage(emptySoul, 80, 65, null);
        }
        else if(gameState == 3)
        {
            basicMovement(); // Basic logic

                        //      BOSS1 MOVEMENT      //

            if (boss1Up)
            {
                posyBoss1 -= boss1Speed;
                if (posyBoss1 < 0) boss1Up = false;
            }
            else
            {
                posyBoss1 += boss1Speed;
                if (posyBoss1 > 475) boss1Up = true;
            }
            if (boss1Left)
            {
                posxBoss1 -= boss1Speed;
                if (posxBoss1 < -10)
                {
                    boss1Left = false;
                    boss1Frame = 1;
                }
            }
            else
            {
                posxBoss1 += boss1Speed;
                if (posxBoss1 > 1100)
                {
                    boss1Left = true;
                    boss1Frame = 0;
                }
            }

            // Collision Detection + Lose Condition
            enemyAttackDelay += 1;
            if (enemyHealth > 0 && enemyAttackDelay > 50)
            {
                if(!(posX + 40 < posxBoss1 || posX > posxBoss1 + 190 || posY + 100 < posyBoss1 - 10 || posY > posyBoss1 + 190))
                {
                    playerHealth -= 1; // What happens when you lose?
                    playerHitFrame = 5;
                    enemyAttackDelay = 0;
                }
            }

            // Drawing the player, enemy, and background
            g.drawImage(background2,0,0, null); // The background

            drawPlayer(g); // Draw the player

            // Animations

            if (boss1Frame == 1 || boss1Frame == 3)
            {
                if (bossAnimationTimer % 2 == 0) boss1Frame = 1;
                else boss1Frame = 3;
            }

            if (boss1Frame == 0 || boss1Frame == 2)
            {
                if (bossAnimationTimer % 2 == 0) boss1Frame = 0;
                else boss1Frame = 2;
            }

            // Boss1 Drawing
            if (boss1Health > 0)
            {
                if (enemyHitFrame % 2 == 0) g.drawImage(boss1Img[boss1Frame], posxBoss1, posyBoss1, null); // The enemy
            }
            else
            {
                gameState = 4;
                resetVariables(); // Resets the variables when the enemy dies
            }

            playerHealthSetup(g); // Player Health

            // Soul Meter
            if (soulMeter > 4) g.drawImage(fullSoul, 80, 65, null);
            else g.drawImage(emptySoul, 80, 65, null);

        }
        else if(gameState == 4)
        {
                        //      LVL 3 LOGIC & ANIMATION       //

            basicMovement();

                        //      BOSS2 MOVEMENT      //

            if (boss2Right && posxBoss2 + 150 < 1280)
            {
                posxBoss2 += 4;
            }
            else if (posxBoss2 > 0)
            {
                boss2Right = false;
                posxBoss2 -= 4;
            }
            else
            {
                boss2Right = true;
            }

                        //      BOSS 2 BEAM ATTACK      //

            if (beamAttackTimer >= 50)
            {
                boss2Frame = 1;
                boss2Animation = true;
                if (beamAttackTimer == 85)
                {
                    // collision
                    boss2Frame = 0;
                    beamAttackTimer = 0;
                    boss2Attacking = true;
                    boss2Animation = false;
                }
            }

            // Collision Detection + Lose Condition
            enemyAttackDelay += 1;
            if (enemyHealth > 0 && enemyAttackDelay > 50)
            {
                if(!(posX + 40 < posxBoss2 || posX > posxBoss2 + 140 || posY + 100 < posyBoss2 - 10 || posY > posyBoss2 + 290))
                {
                    playerHealth -= 1; // What happens when you lose?
                    playerHitFrame = 5;
                    enemyAttackDelay = 0;
                }
            }

            // Drawing the player, enemy, and background
            g.drawImage(background3,0,0, null); // The background

            drawPlayer(g); // Draw the player

            // Boss2 Drawing
            if (boss2Health > 0)
            {
                if (enemyHitFrame % 2 == 0)
                {
                    if (boss2Animation)g.drawImage(boss2Img[boss2Frame], posxBoss2 - 75, posyBoss2, null); // The enemy
                    else g.drawImage(boss2Img[boss2Frame], posxBoss2, posyBoss2, null); // The enemy
                }
            }
            else
            {
                newGame = true;
                gameState = 5;
            }

            if (beamAttackTimer >= 75)
            {
                g.drawImage(boss2Beam, 0, 425, null);
                if (posY > 425 && enemyAttackDelay > 50)
                {
                    playerHealth --;
                    enemyAttackDelay = 0;
                }
                boss2Attacking = false;
            }

            playerHealthSetup(g);  // Player Health

            // Soul Meter
            if (soulMeter > 4) g.drawImage(fullSoul, 80, 65, null);
            else g.drawImage(emptySoul, 80, 65, null);
        }
        else if (gameState == 5)
        {
            g.drawImage(winScreen, 0, 0, null);
            resetEnemy = true;
        }
        else if (gameState == 6)
        {
            g.drawImage(gameOverScreen, 0, 0, null);
            resetEnemy = true;
        }
        else if (gameState == 7)
        {
            score = scoreTimer / 50;

            if (score < highScore1)
            {
                name5 = name4;
                name4 = name3;
                name3 = name2;
                name2 = name1;
                name1 = name;

                highScore5 = highScore4;
                highScore4 = highScore3;
                highScore3 = highScore2;
                highScore2 = highScore1;
                highScore1 = score;
            }
            else if (score < highScore2)
            {
                name5 = name4;
                name4 = name3;
                name3 = name2;
                name2 = name;

                highScore5 = highScore4;
                highScore4 = highScore3;
                highScore3 = highScore2;
                highScore2 = score;
            }
            else if (score < highScore3)
            {
                name5 = name4;
                name4 = name3;
                name3 = name;

                highScore5 = highScore4;
                highScore4 = highScore3;
                highScore3 = score;
            }
            else if (score < highScore4)
            {
                name5 = name4;
                name4 = name;

                highScore5 = highScore4;
                highScore4 = score;
            }
            else if (score < highScore5)
            {
                name5 = name;
                highScore5 = score;
            }

            try
            {
                outputTextFile();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
            gameState = 9;
        }
        else if (gameState == 8)
        {
            g.drawImage(inputNameScreen, 0, 0, null);
            g.setColor(new Color(255, 255, 255));
            Font font = new Font("Times New Roman", Font.PLAIN, 220);
            g.setFont(font);
            g.drawString(name, 435, 515);
        }
        else if (gameState == 9)
        {
            g.drawImage(highScoreScreen, 0, 0, null);

            g.setColor(new Color(255, 255, 255));
            Font font = new Font("Times New Roman", Font.PLAIN, 40);
            g.setFont(font);

            g.drawString(name1, 240, 250); // #1 Name
            g.drawString(name2, 240, 325); // #2 Name
            g.drawString(name3, 240, 400); // #3 Name
            g.drawString(name4, 240, 475); // #4 Name
            g.drawString(name5, 240, 550); // #5 Name

            g.drawString("" + highScore1, 985, 250); // #1 Score
            g.drawString("" + highScore2, 985, 325); // #1 Score
            g.drawString("" + highScore3, 985, 400); // #1 Score
            g.drawString("" + highScore4, 985, 475); // #1 Score
            g.drawString("" + highScore5, 985, 550); // #1 Score
        }

        //      TIMER       //

        if (gameState == 2 || gameState == 3 || gameState == 4)
        {
            g.setColor(new Color(255, 255, 255));
            Font font = new Font("Times New Roman", Font.PLAIN, 40);
            g.setFont(font);
            g.drawString("" + (scoreTimer / 50), 1225, 50);
        }
    }

    public static void main (String[] args) throws IOException
    {
        JFrame frame = new JFrame("Hollow Knight"); // Rename later?
        HollowKnight panel = new HollowKnight();
        frame.add(panel);
        frame.setVisible(true);
        frame.pack();

        //      TEXT FILE STREAMING     //5

        Scanner inputFile = new Scanner(new File("assets/gameScores.txt"));
        

        String player1 = inputFile.nextLine();
        String player2 = inputFile.nextLine();
        String player3 = inputFile.nextLine();
        String player4 = inputFile.nextLine();
        String player5 = inputFile.nextLine();

        inputFile.close();

        name1 = player1.split(" ")[0];
        highScore1 = parseInt(player1.split(" ")[1]);
        name2 = player2.split(" ")[0];
        highScore2 = parseInt(player2.split(" ")[1]);
        name3 = player3.split(" ")[0];
        highScore3 = parseInt(player3.split(" ")[1]);
        name4 = player4.split(" ")[0];
        highScore4 = parseInt(player4.split(" ")[1]);
        name5 = player5.split(" ")[0];
        highScore5 = parseInt(player5.split(" ")[1]);
    }

    public void keyPressed(KeyEvent e)
    {
        char keyInput = e.getKeyChar();
        int key = e.getKeyCode();

        if(gameState == 0)
        {
            if (key == 83) gameState = 2; // 's'
            if (key == 67) gameState = 1; // 'c'
            if (key == 72) gameState = 9; // 'h'
        }
        else if (gameState == 1)
        {
            if (key == 27) gameState = 0; // esc
            if (key == 40 && cursorY < 600) cursorY += 200; // Down
            if (key == 38 && cursorY > 200) cursorY -= 200; // Up
            if (key == 37 && cursorX > 50) cursorX -= 550; // left
            if (key == 39 && cursorX < 600) cursorX += 550; // Right
            if (cursorY == 200 && cursorX == 50 && key != 27 && key != 40 && key != 38 && key != 37 && key != 39 && key != rightKey && key != jumpKey && key != dashKey && key != attackKey && key != healKey) // Left
            {
                leftKey = key;
                leftKeyChar = keyInput;
            }
            if (cursorY == 400 && cursorX == 50 && key != 27 && key != 40 && key != 38 && key != 37 && key != 39 && key != leftKey && key != jumpKey && key != dashKey && key != attackKey && key != healKey) // Right
            {
                rightKey = key;
                rightKeyChar = keyInput;
            }
            if (cursorY == 600 && cursorX == 50 && key != 27 && key != 40 && key != 38 && key != 37 && key != 39 && key != leftKey && key != rightKey && key != dashKey && key != attackKey && key != healKey) // Jump
            {
                jumpKey = key;
                jumpKeyChar = keyInput;
            }
            if (cursorY == 200 && cursorX == 600 && key != 27 && key != 40 && key != 38 && key != 37 && key != 39 && key != leftKey && key != rightKey && key != jumpKey && key != attackKey && key != healKey) // Dash
            {
                dashKey = key;
                dashKeyChar = keyInput;
            }
            if (cursorY == 400 && cursorX == 600 && key != 27 && key != 40 && key != 38 && key != 37 && key != 39 && key != leftKey && key != rightKey && key != jumpKey && key != dashKey && key != healKey) // Attack
            {
                attackKey = key;
                attackKeyChar = keyInput;
            }
            if (cursorY == 600 && cursorX == 600 && key != 27 && key != 40 && key != 38 && key != 37 && key != 39 && key != leftKey && key != rightKey && key != jumpKey && key != dashKey && key != attackKey) // Heal
            {
                healKey = key;
                healKeyChar = keyInput;
            }
        }
        else if(gameState == 2 || gameState == 3 || gameState == 4) // if its level 1, 2, or 3
        {
            if(key == leftKey) leftPressed = true;
            if(key == rightKey) rightPressed = true;

            if(key == 86) // 'v'
            {
                enemyHealth = 0;
                boss1Health = 0;
                boss2Health = 0;
            }

            if(key == jumpKey) jumpedPlayer = true;

            if(key == dashKey && dashDelay > 25 && ((posX > 155 || playerRight) && (posX < 1025 || !playerRight))) // Dash (Make it work near walls?)
            {
                if(playerRight) posX += dashLength;
                else posX -= dashLength;
                dashDelay = 0;
            }
            if(key == attackKey) isSlash = true;
            if (key == healKey && soulMeter == 5 && playerHealth != 5) // soul thing
            {
                playerHealth += 1;
                soulMeter = 0;
            }
        }
        else if (gameState == 5)
        {
            if (scoreTimer / 50 < highScore5) gameState = 8;
            else gameState = 9;
        }
        else if (gameState == 6) gameState = 0;
        else if (gameState == 7) gameState = 0;
        else if (gameState == 8)
        {
            if (key != 8 && key != 10 && name.length() <= 3) // Backspace or Enter
            {
                name += keyInput;
            }
            if (key == 8 && name.length() > 0) // Backspace
            {
                name = name.substring(0, name.length() - 1);
            }
            if (key == 10 && name.length() > 0) // Enter
            {
                gameState = 7;
            }
        }
        else if (gameState == 9) gameState = 0;
        repaint();
    }

    public void keyReleased(KeyEvent e)
    {
        int key = e.getKeyCode();
        if (gameState == 2 || gameState == 3 || gameState == 4)
        {
            if (key == leftKey) leftPressed = false;
            if (key == rightKey) rightPressed = false;
        }
        else
        {
            leftPressed = false;
            rightPressed = false;
        }
    }

    public void run() // Threading
    {
        while(true)
        {
            try
            {
                Thread.sleep(20); // 50 fps
                repaint();
            }
            catch(Exception e){}
        }
    }

    public static void resetVariables () // Resets all the important variables in between levels
    {
        if (newGame) // Checks if it is a new game
        {
            playerHealth = 5;
            soulMeter = 0;
            scoreTimer = 0;
            beamAttackTimer = 0;
            newGame = false;
            name = "";
        }

        // Animation settings
        curPlayerFrame = 0;

        // Position settings
        posX = 25;
        posY = 525;
        if (gameState == 4) posY = 480;
        posxEnemy = 250;
        posxBoss1 = 0;
        posyBoss1 = 125;
        posxBoss2 = 640;

        // Health settings
        enemyHealth = 5;
        boss1Health = 10;
        boss2Health = 15;

        // Movement settings
        leftPressed = false;
        rightPressed = false;
        jumpedPlayer = false;
        jumpVelocityPlayer = -36;
    }

    public static void jumpLogic ()
    {
        if(jumpedPlayer)
        {
            if (playerRight) curPlayerFrame = 4;
            else curPlayerFrame = 5;
            posY += jumpVelocityPlayer;
            jumpVelocityPlayer += gravityPlayer;

            if(jumpVelocityPlayer == 39)
            {
                jumpedPlayer = false;
                jumpVelocityPlayer = -36;
            }
        }
    }

    public static void walkLogic ()
    {
        if (leftPressed && posX > -20)
        {
            posX -= playerSpeed;
            playerRight = false;
            if(jumpedPlayer) curPlayerFrame = 5;
            else curPlayerFrame = 1;
        }
        if (rightPressed && posX < 1200)
        {
            posX += playerSpeed;
            playerRight = true;
            if(jumpedPlayer) curPlayerFrame = 4;
            else curPlayerFrame = 0;
        }
    }
    public static void attackLogicEnemy ()
    {
        if(isSlash && attackDelay > 25)
        {
            if (playerRight)
            {
                curPlayerFrame = 2;
                if(!(posX + 200 < posxEnemy || posX + 50 > posxEnemy + 125 || posY + 100 < posyEnemy || posY > posyEnemy + 150))
                {
                    enemyHealth -= 1;
                    enemyHitFrame = 5;
                    if (soulMeter < 5) soulMeter += 1;
                }
            }
            else
            {
                curPlayerFrame = 3;
                if(!(posX < posxEnemy + 125 || posX - 150 > posxEnemy + 125 || posY + 100 < posyEnemy || posY > posyEnemy + 150))
                {
                    enemyHealth -= 1;
                    enemyHitFrame = 5;
                    if (soulMeter < 5) soulMeter += 1;
                }
            }
            isSlash = false;
            attackDelay = 0;
            playerAttackTimer = 5;
        }
    }
    public static void attackLogicBoss1 ()
    {
        if(isSlash && attackDelay > 25)
        {
            if (playerRight)
            {
                curPlayerFrame = 2;
                if(!(posX + 200 < posxBoss1 || posX + 50 > posxBoss1 + 200 || posY + 100 < posyBoss1 || posY > posyBoss1 + 200))
                {
                    boss1Health -= 1;
                    enemyHitFrame = 5;
                    if (soulMeter < 5) soulMeter += 1;
                }
            }
            else
            {
                curPlayerFrame = 3;
                if(!(posX < posxBoss1 + 200 || posX - 150 > posxBoss1 + 200 || posY + 100 < posyBoss1 || posY > posyBoss1 + 200))
                {
                    boss1Health -= 1;
                    enemyHitFrame = 5;
                    if (soulMeter < 5) soulMeter += 1;
                }
            }
            isSlash = false;
            attackDelay = 0;
            playerAttackTimer = 5;
        }
    }

    public static void attackLogicBoss2 ()
    {
        if(isSlash && attackDelay > 25)
        {
            if (playerRight)
            {
                curPlayerFrame = 2;
                if(!(posX + 200 < posxBoss2 || posX + 50 > posxBoss2 + 200 || posY + 100 < posyBoss2 || posY > posyBoss2 + 200))
                {
                    boss2Health -= 1;
                    enemyHitFrame = 5;
                    if (soulMeter < 5) soulMeter += 1;
                }
            }
            else
            {
                curPlayerFrame = 3;
                if(!(posX < posxBoss2 + 200 || posX - 150 > posxBoss2 + 200 || posY + 100 < posyBoss2 || posY > posyBoss2 + 200))
                {
                    boss2Health -= 1;
                    enemyHitFrame = 5;
                    if (soulMeter < 5) soulMeter += 1;
                }
            }
            isSlash = false;
            attackDelay = 0;
            playerAttackTimer = 5;
        }
    }

    public static void basicMovement ()
    {
        // Timers
        scoreTimer ++;
        attackDelay ++; // Delay for attack
        dashDelay ++; // Delay for dash
        bossAnimationTimer ++;
        if (playerAttackTimer >= 0) playerAttackTimer --;
        if (gameState == 4) beamAttackTimer += 1; // Timer for the boss2 attack
        if (enemyHitFrame > 0) enemyHitFrame --; // Animations for when enemy gets hit
        if (playerHitFrame > 0) playerHitFrame --; // Animations for when player gets hit

        jumpLogic(); // Jumping logic
        walkLogic(); // Walking animation for player

        // Attack logic
        if (gameState == 2) attackLogicEnemy();
        else if (gameState == 3) attackLogicBoss1();
        else if (gameState == 4) attackLogicBoss2();
    }

    public static void outputTextFile() throws IOException // The method to run when the high scores need to be updated
    {
        PrintWriter outputFile = new PrintWriter(new FileWriter("assets/gameScores.txt"));

        outputFile.printf("%s %d%n", name1, highScore1);
        outputFile.printf("%s %d%n", name2, highScore2);
        outputFile.printf("%s %d%n", name3, highScore3);
        outputFile.printf("%s %d%n", name4, highScore4);
        outputFile.printf("%s %d", name5, highScore5);
        outputFile.close();
    }

    public static void playerDeath()
    {
        newGame = true;
        gameState = 6;
        resetVariables();
    }

    public static void playerHealthSetup(Graphics g)
    {
        if (playerHealth > 4)
        {
            g.drawImage(health5, 425, 64, null);
            g.drawImage(health4, 370, 64, null);
            g.drawImage(health3, 315, 64, null);
            g.drawImage(health2, 260, 64, null);
            g.drawImage(health1, 205, 64, null);
        }
        else if (playerHealth > 3)
        {
            g.drawImage(health4, 370, 64, null);
            g.drawImage(health3, 315, 64, null);
            g.drawImage(health2, 260, 64, null);
            g.drawImage(health1, 205, 64,  null);
        }
        else if (playerHealth > 2)
        {
            g.drawImage(health3, 315, 64, null);
            g.drawImage(health2, 260, 64, null);
            g.drawImage(health1, 205, 64,  null);
        }
        else if (playerHealth > 1)
        {
            g.drawImage(health2, 260, 64, null);
            g.drawImage(health1, 205, 64,  null);
        }
        else if (playerHealth > 0) g.drawImage(health1, 205, 64,  null);
        else if (playerHealth == 0) playerDeath();
    }

    public static void drawPlayer(Graphics g)
    {
        if (playerHitFrame % 2 == 0)
        {
                if (playerAttackTimer > 0 && playerRight) curPlayerFrame = 2;
                else if (playerAttackTimer > 0) curPlayerFrame = 3;
                if (playerAttackTimer == 0)
                {
                    if (playerRight) curPlayerFrame = 0;
                    else curPlayerFrame = 1;
                }
                if (curPlayerFrame == 3) g.drawImage(playerImg[curPlayerFrame], posX - 150, posY, null); // The player
                else g.drawImage(playerImg[curPlayerFrame], posX, posY, null); // The player
        }
    }
                    //      USELESS METHODS     //
    public void keyTyped(KeyEvent e) {}
}