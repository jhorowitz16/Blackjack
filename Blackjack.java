import java.io.*;
import java.util.*;



class Blackjack {
     
    /** Initialize game objects for both players */
    public static ArrayList hand_1 = new ArrayList();
    public static ArrayList hand_2 = new ArrayList();
    public static ArrayList<Card> deck = new ArrayList<Card>();

    /** Game constants */
    public static final int SUITS = 4;
    public static final int RANKS = 13; 
    public static final int GOAL = 21; 

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
                    possScores.add(oldScore + 11);
                }
                // double check here - size should be origSize * 2 (debugging)
                if (origSize * 2 != possScores.size()) {
                    System.out.println(possScores);
                    System.out.println(origSize);
                    System.out.println(possScores.size());
                    return -1;
                }
            }
        }
        // Get the best score that doesn't go over 21
        // assume the hand is nonNull
        int optimal = -1;
        int minScore = Integer.MAX_VALUE;
        for (int score : possScores) {
            if (score <= 21 && score > optimal) {
                optimal = score;
            }
            minScore = Math.min(minScore, score);
        }
        // first priority - pick the "optimal" under 21
        // second priority - just pick the smallest one... tho busting regardless
        if (optimal > 0)
            return optimal;
        return minScore;
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
            System.out.println(">>> <" + rank + " of " + suit + ">");
        }
        System.out.println(">>>    Score: " + calcPoints(hand)); 
    }
    
    /** Print both hands... */
    public static void displayBothHands() {
        displayHand(1);
        displayHand(2);
    }

    /** Give both players 2 card hands and display hands */
    public static void startGame() {
        buildDeck();
        hand_1.add(drawCard());
        hand_1.add(drawCard());
        hand_2.add(drawCard());
        hand_2.add(drawCard());
    }

    /** Take a single turn and return the score (-1 for bust) */
    public static int takeTurn(int hand_num) {
        System.out.println("");
        ArrayList<Card> hand;
        String player_name;
        if (hand_num == 1) {
            hand = hand_1;
            player_name = "Player One";
        } else {
            hand = hand_2;
            player_name = "Player Two";
        }
        int currScore = calcPoints(hand);
        displayHand(hand_num);
        // if the score means the game is over, then exit and report that player busts
        if (currScore == GOAL) {
            System.out.println(player_name + ", you got 21!");
            return currScore;

        } else if (currScore > GOAL) {
            System.out.println(player_name + ", you busted!");
            return -1;
        }
        // Give user options
        System.out.println("\n" + player_name + ", make your move");
        System.out.println("*** press H for Hit, S for Stand ***");
        while (true) {
            Scanner sc = new Scanner(System.in);
            String input_text = sc.nextLine().toLowerCase();
            // check for blank lines (do nothing)
            if (input_text.length() <= 0)
                continue; 
            char first = input_text.charAt(0);
            if (first == 'h') {
                hand.add(drawCard());
                return takeTurn(hand_num);
            } else if (first == 's') {
                int points = calcPoints(hand);
                System.out.println("\n" + player_name + ", you finished with " + 
                        points + " points!");
                return points;
            } else {
                System.out.println("invalid input, try again");
            }
        }
    }


    public static void main(String[] args) {
        System.out.println("\n === Welcome to the casino! === ");
        startGame();
        int player_1_score = takeTurn(1);
        int player_2_score = takeTurn(2);
        // note: busting counts as -1, so we can just take the max
        if (player_1_score > player_2_score)
            System.out.println("\n --- Player One Wins! ---\n");
        else if (player_1_score < player_2_score)
            System.out.println("\n --- Player Two Wins! ---\n");
        else
            System.out.println("\n --- Tie Game! ---\n");
    }
}
