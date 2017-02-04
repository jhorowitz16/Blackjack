import java.io.*;
import java.util.*;



class Blackjack {
     
    /** Initialize game objects for both players */
    public static ArrayList hand_1 = new ArrayList();
    public static ArrayList hand_2 = new ArrayList();
    public static ArrayList<Card> deck = new ArrayList<Card>();
    public static boolean player_1_done = false;
    public static boolean player_2_done = false;

    /** Game constants */
    public static final int SUITS = 4;
    public static final int RANKS = 13; 

    /** Initialize the 52 card deck for the game */
    public static void buildDeck() {
        for (int i = 1; i <= SUITS; i++) {
            for (int j = 1; j <= RANKS; j++) {
                Card new_card = new Card(j, i); 
                deck.add(new_card);
            }
        }
        Collections.shuffle(deck);
    }

    /** Pop the top card off of the stack */
    public static Card drawCard() {
        Card newCard = new Card(-1, -1);
        try {
            newCard = deck.remove(0);
        }
        catch(EmptyStackException e) {
            System.out.println("Cannot draw from empty deck.");
        }
        return newCard;
    }

    /** Report the result of the game - who busts, who wins etc */
    public static String resolveGame() {
        int score_1 = calcPoints(hand_1);
        int score_2 = calcPoints(hand_2);
        if (score_1 > 21 && score_2 > 21)
            return "Tie: Both Bust";
        else if (score_1 > 21)
            return "Player Two wins with " + score_2 + " points";
        else if (score_2 > 21)
            return "Player One wins with " + score_1 + " points";
        // deal with win condition where neither busts
        if (player_1_done && player_2_done) {
            String winner;
            if (score_1 < score_2)
                winner = "Player Two";
            else if (score_1 > score_2)
                winner = "Player One";
            else
                winner = "Nobody";
            return winner + " wins the game!";
        }
        // game not over
        return "Not Over";
    }

    /** Calculate the total points in a hand (arraylist) */
    public static int calcPoints(ArrayList<Card> hand) {

        ArrayList<Integer> possScores= new ArrayList<Integer>();
        possScores.add(0);
        // maintaining a list of possible scores b/c Aces have multiple options
        int totalPoints = 0;
        for (Card c : hand) {
            int points = c.getPoints();
            if (points != Card.ACE) {
                // nothing fancy, just take the score                
                for (int i = 0; i < possScores.size(); i += 1) {
                    int oldScore = possScores.get(i);
                    possScores.set(i, oldScore + points);
                }
            } else {
                // make two branches - for ace as 1 and ace as 11 respectively
                int origSize = possScores.size();
                for (int i = 0; i < origSize; i += 1) {
                    int oldScore = possScores.get(i);
                    possScores.set(i, oldScore + 1);
                    possScores.add(possScores.get(i) + 11);
                }
                // double check here - size should be origSize * 2
                if (origSize * 2 != possScores.size())
                    return -1;
            }
        }
        // Get the best score that doesn't go over 21
        // assume the hand is nonNull
        int optimal = -1;
        for (int score : possScores) {
            if (score <= 21 && score > optimal) {
                optimal = score;
            }
        }
        return optimal;
    }

    /** Simple display for each card in the player's hand */ 
    public static void displayHand(int hand_num) {
        ArrayList<Card> hand;
        if (hand_num == 1)
            hand = hand_1;
        else
            hand = hand_2;
        System.out.println("Player " + hand_num + ": ");
        for (Card c : hand) {
            String rank = Card.rankToString(c.getRank());
            String suit = Card.suitToString(c.getSuit());
            System.out.println(">>> < " + rank + " of " + suit + " >");
        }
    }
    
    /** Print both hands... */
    public static void displayBothHands() {
        displayHand(1);
        displayHand(2);
    }

    /** Give both players 2 card hands and display hands */
    public static void startGame() {
        buildDeck();
        displayBothHands();
        hand_1.add(drawCard());
        hand_1.add(drawCard());
        hand_2.add(drawCard());
        hand_2.add(drawCard());
        displayBothHands();
    }


    public static void main(String[] args) {
        System.out.println("Hello World");
        startGame();
    }
}
