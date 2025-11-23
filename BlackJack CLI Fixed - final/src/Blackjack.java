import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Blackjack {

    class Card {
        String cardName;
        ImageIcon cardImageIcon;
        
        Card(String cardName, ImageIcon cardImageIcon) {
            this.cardName = cardName;
            this.cardImageIcon = cardImageIcon;
        }

        public String toString() {
            return cardName;
        }
    }

    int cardHeight = 60;
    int cardWidth = 30;

    String[] cardList = {
        "clubs_A", "clubs_2", "clubs_3", "clubs_4", "clubs_5", "clubs_6", "clubs_7",
        "clubs_8", "clubs_9", "clubs_10", "clubs_J", "clubs_Q", "clubs_K",
        "diamonds_A", "diamonds_2", "diamonds_3", "diamonds_4", "diamonds_5", "diamonds_6",
        "diamonds_7", "diamonds_8", "diamonds_9", "diamonds_10", "diamonds_J", "diamonds_Q", "diamonds_K",
        "spades_A", "spades_2", "spades_3", "spades_4", "spades_5", "spades_6", "spades_7",
        "spades_8", "spades_9", "spades_10", "spades_J", "spades_Q", "spades_K",
        "hearts_A", "hearts_2", "hearts_3", "hearts_4", "hearts_5", "hearts_6", "hearts_7",
        "hearts_8", "hearts_9", "hearts_10", "hearts_J", "hearts_Q", "hearts_K"
    };

    Scanner scan = new Scanner(System.in);

    ArrayList<Card> cardSet;
    ArrayList<Integer> cardValues;
    
    ArrayList<Card> playerDrawn = new ArrayList<>();
    ArrayList<Integer> playerDrawnValues = new ArrayList<>();

    ArrayList<Card> dealerDrawn = new ArrayList<>();
    ArrayList<Integer> dealerDrawnValues = new ArrayList<>();
    
    ArrayList<Card> usedCards = new ArrayList<>();
    ArrayList<Integer> usedCardValues = new ArrayList<>();

    ImageIcon cardBackImageIcon;

    double playerMoney = 1000.0;
    boolean keepGoing = true;
    double bet = 0;

    public static void main(String[] args) {
        new Blackjack();
    }

    Blackjack() {
        setupCards();
        shuffleCards();
        System.out.println("=================================");
        System.out.println("   WELCOME TO BLACKJACK!");
        System.out.println("=================================");
        playAgain();
        scan.close();
    }

    public void setupCards() {
        cardValues = new ArrayList<>();

        for (String cardName : cardList) {
            String rank = cardName.substring(cardName.lastIndexOf('_') + 1);
            
            if (rank.equals("A")) {
                cardValues.add(11);
            } else if (rank.equals("J") || rank.equals("Q") || rank.equals("K")) {
                cardValues.add(10);
            } else {
                cardValues.add(Integer.parseInt(rank));
            }
        }

        cardSet = new ArrayList<Card>();
        for (String cardName : cardList) {
            try {
                Image cardImg = new ImageIcon(getClass().getResource("./img/" + cardName + ".png")).getImage();
                ImageIcon cardImageIcon = new ImageIcon(cardImg.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));
                Card card = new Card(cardName, cardImageIcon);
                cardSet.add(card);
            } catch (Exception e) {
                Card card = new Card(cardName, null);
                cardSet.add(card);
            }
        }

        try {
            Image cardBackImg = new ImageIcon(getClass().getResource("./img/back_light.png")).getImage();
            cardBackImageIcon = new ImageIcon(cardBackImg.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));
        } catch (Exception e) {
            cardBackImageIcon = null;
        }
    }

    public void shuffleCards() {
        for (int i = 0; i < cardSet.size(); i++) {
            int j = (int)(Math.random() * cardSet.size());

            int temp1 = cardValues.get(i);
            cardValues.set(i, cardValues.get(j));
            cardValues.set(j, temp1);
            
            Card temp = cardSet.get(i);
            cardSet.set(i, cardSet.get(j));
            cardSet.set(j, temp);
        }
    }

    public void playAgain() {
        while (keepGoing) {
            ensureDeckSize();
            askUserHowMuchtoBet();
            dealCardstoPlayer();
            showTwoCards();
            
            if (checkForBlackJack()) {
                clearTheBoard();
                printStats();
                askToPlayAgain();
                continue;
            }
            
            askUsertoHitStayOrDoubleDown();
            clearTheBoard();
            printStats();
            askToPlayAgain();
        }
    }

    public void ensureDeckSize() {
        if (cardSet.size() < 15) {
            cardSet.addAll(usedCards);
            cardValues.addAll(usedCardValues);
            usedCards.clear();
            usedCardValues.clear();
            shuffleCards();
            System.out.println("\n*** Deck reshuffled ***");
        }
    }

    public void askUserHowMuchtoBet() {
        boolean betValid = false;

        System.out.println("\nYou have $" + playerMoney);

        while (!betValid) {
            System.out.println("Enter the amount of money you want to bet. Minimum bet = $15.");
            System.out.println("Your bet must be in 5s, 10s, 25s, 100s, and 500s.");
            
            if (!scan.hasNextDouble()) {
                System.out.println("Invalid input. Please enter a number.");
                scan.nextLine();
                continue;
            }
            
            bet = scan.nextDouble();
            scan.nextLine(); // Consume newline
            
            if (bet > playerMoney) {
                System.out.println("You don't have enough money! You have $" + playerMoney);
                continue;
            }
            
            if (bet >= 15 && (bet % 5 == 0)) {
                System.out.println("Confirm your bet of $" + bet + " (y/n)");
                String confirmation = scan.nextLine().trim();
                
                if (confirmation.equalsIgnoreCase("y")) {
                    System.out.println("Confirmed");
                    betValid = true;
                    playerMoney -= bet;
                } else if (confirmation.equalsIgnoreCase("n")) {
                    System.out.println("Please enter a new bet.");
                } else {
                    System.out.println("Invalid argument, please enter a new bet.");
                }
            } else {
                System.out.println("Invalid bet amount. Try again.");
            }
        }
    }

    public void dealCardstoPlayer() {
        Card card1 = cardSet.remove(0);
        Card card2 = cardSet.remove(0);

        int card1value = cardValues.remove(0);
        int card2value = cardValues.remove(0);

        System.out.println("\nPlayer: " + card1 + "\t" + card2);

        usedCards.add(card1);
        usedCards.add(card2);
        usedCardValues.add(card1value);
        usedCardValues.add(card2value);

        playerDrawn.add(card1);
        playerDrawn.add(card2);
        playerDrawnValues.add(card1value);
        playerDrawnValues.add(card2value);
    }

    public void showTwoCards() {
        Card card1 = cardSet.remove(0);
        Card card2 = cardSet.remove(0);

        int card1value = cardValues.remove(0);
        int card2value = cardValues.remove(0);

        System.out.println("Dealer: " + card1 + "\t[HIDDEN]");

        usedCards.add(card1);
        usedCards.add(card2);
        usedCardValues.add(card1value);
        usedCardValues.add(card2value);

        dealerDrawn.add(card1);
        dealerDrawn.add(card2);
        
        dealerDrawnValues.add(card1value);
        dealerDrawnValues.add(card2value);
    }

    public int calculateHandValue(ArrayList<Integer> hand) {
        int sum = 0;
        int aces = 0;

        for (int value : hand) {
            sum += value;
            if (value == 11) {
                aces++;
            }
        }

        while (sum > 21 && aces > 0) {
            sum -= 10;
            aces--;
        }

        return sum;
    }

    public boolean checkForBlackJack() {
        int playerValue = calculateHandValue(playerDrawnValues);
        int dealerValue = calculateHandValue(dealerDrawnValues);

        boolean playerBJ = (playerValue == 21 && playerDrawnValues.size() == 2);
        boolean dealerBJ = (dealerValue == 21 && dealerDrawnValues.size() == 2);

        if (playerBJ || dealerBJ) {
            System.out.println("\n=== BLACKJACK CHECK ===");
            System.out.println("Dealer reveals: " + dealerDrawn);
            
            if (playerBJ && dealerBJ) {
                System.out.println("Both have Blackjack! Push!");
                playerMoney += bet; // Return bet
            } else if (playerBJ) {
                System.out.println("BLACKJACK! You win 3:2!");
                playerMoney += bet * 2.5; // 3:2 payout
            } else {
                System.out.println("Dealer has Blackjack! You lose!");
            }
            return true;
        }
        
        System.out.println("Your hand value: " + playerValue);
        return false;
    }

    public int dealerDrawsUntil17() {
        int sumDealerCards = calculateHandValue(dealerDrawnValues);

        System.out.println("\n=== DEALER'S TURN ===");
        System.out.println("Dealer reveals: " + dealerDrawn);
        System.out.println("Dealer has: " + sumDealerCards);

        while (sumDealerCards < 17) {
            System.out.println("Dealer hits...");
            
            ensureDeckSize();
            
            Card cardRemoved = cardSet.remove(0);
            int cardValueRemoved = cardValues.remove(0);
                    
            dealerDrawn.add(cardRemoved);
            dealerDrawnValues.add(cardValueRemoved);

            usedCards.add(cardRemoved);
            usedCardValues.add(cardValueRemoved);
            
            sumDealerCards = calculateHandValue(dealerDrawnValues);
            System.out.println("Dealer draws " + cardRemoved + " -> Total: " + sumDealerCards);
        }

        if (sumDealerCards >= 17 && sumDealerCards <= 21) {
            System.out.println("Dealer stands with " + sumDealerCards);
        } else if (sumDealerCards > 21) {
            System.out.println("Dealer busts with " + sumDealerCards);
        }

        return sumDealerCards;
    }

    public void askUsertoHitStayOrDoubleDown() {
        String decision = "";
        boolean playerTurn = true;

        while (playerTurn) {
            System.out.println("\nYour deck: " + playerDrawn);
            System.out.println("Your hand value: " + calculateHandValue(playerDrawnValues));
            System.out.println("Dealer showing: " + dealerDrawn.get(0));
            
            if (playerDrawn.size() == 2) {
                System.out.println("Do you want to Hit (H), Stay (ST), or Double Down (DD)?");
            } else {
                System.out.println("Do you want to Hit (H) or Stay (ST)?");
            }
            
            decision = scan.nextLine().trim().toUpperCase();

            if (decision.equals("H")) {
                ensureDeckSize();
                
                Card cardRemoved = cardSet.remove(0);
                int cardValueRemoved = cardValues.remove(0);
                        
                playerDrawn.add(cardRemoved);
                playerDrawnValues.add(cardValueRemoved);

                usedCards.add(cardRemoved);
                usedCardValues.add(cardValueRemoved);

                int playerValue = calculateHandValue(playerDrawnValues);
                System.out.println("You drew: " + cardRemoved);
                System.out.println("Your hand value: " + playerValue);

                if (playerValue > 21) {
                    System.out.println("\nPlayer Busts! You lose!");
                    playerTurn = false;
                }
            } 
            else if (decision.equals("ST")) {
                playerTurn = false;
                
                int playerValue = calculateHandValue(playerDrawnValues);
                int dealerValue = dealerDrawsUntil17();

                determineWinner(playerValue, dealerValue);
            } 
            else if (decision.equals("DD") && playerDrawn.size() == 2) {
                if (playerMoney < bet) {
                    System.out.println("Not enough money to double down!");
                    continue;
                }

                System.out.println("Double Down! Your bet is now: $" + (bet * 2));
                playerMoney -= bet;
                bet *= 2;
                
                ensureDeckSize();
                
                Card cardRemoved = cardSet.remove(0);
                int cardValueRemoved = cardValues.remove(0);
                        
                playerDrawn.add(cardRemoved);
                playerDrawnValues.add(cardValueRemoved);

                usedCards.add(cardRemoved);
                usedCardValues.add(cardValueRemoved);

                int playerValue = calculateHandValue(playerDrawnValues);
                System.out.println("You drew: " + cardRemoved);
                System.out.println("Your hand value: " + playerValue);

                if (playerValue > 21) {
                    System.out.println("\nPlayer Busts! You lose!");
                    playerTurn = false;
                } else {
                    playerTurn = false;
                    int dealerValue = dealerDrawsUntil17();
                    determineWinner(playerValue, dealerValue);
                }
            } 
            else {
                System.out.println("Invalid option. Try again.");
            }
        }
    }

    public void determineWinner(int playerValue, int dealerValue) {
        System.out.println("\n=== FINAL RESULTS ===");
        System.out.println("Player: " + playerValue);
        System.out.println("Dealer: " + dealerValue);

        if (playerValue > 21) {
            System.out.println("Player Bust! Dealer wins!");
        } else if (dealerValue > 21) {
            System.out.println("Dealer Bust! You win!");
            playerMoney += bet * 2;
        } else if (playerValue > dealerValue) {
            System.out.println("You win!");
            playerMoney += bet * 2;
        } else if (playerValue < dealerValue) {
            System.out.println("Dealer wins!");
        } else {
            System.out.println("Push! Tie game.");
            playerMoney += bet; // Return bet
        }
    }

    public void clearTheBoard() {
        playerDrawn.clear();
        playerDrawnValues.clear();
        dealerDrawn.clear();
        dealerDrawnValues.clear();
        bet = 0;
    }

    public void printStats() {
        System.out.println("\n======================");
        System.out.println("Player Money: $" + playerMoney);
        System.out.println("======================");
    }

    public void askToPlayAgain() {
        if (playerMoney < 15) {
            System.out.println("\nYou don't have enough money to continue!");
            System.out.println("Final amount: $" + playerMoney);
            keepGoing = false;
            return;
        }

        boolean invalid = true;
        while (invalid) {
            System.out.println("\nDo you want to play again? (y/n)");
            String playAgain = scan.nextLine().trim();
            
            if (playAgain.equalsIgnoreCase("y")) {
                invalid = false;
            } else if (playAgain.equalsIgnoreCase("n")) {
                invalid = false;
                keepGoing = false;
                System.out.println("Thanks for playing! Final amount: $" + playerMoney);
            } else {
                System.out.println("Invalid input. Try again.");
            }
        }
    }
}
