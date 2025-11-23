import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

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

    int cardHeight = 200;
    int cardWidth = 150;

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
    double bet = 0;
    double currentBet = 0;

    JFrame frame;
    JPanel bettingPanel;
    JPanel gamePanel;
    JLabel moneyLabel;
    JLabel currentBetLabel;
    JLabel minBetLabel;
    JButton confirmBetButton;
    JButton chip1Button, chip5Button, chip25Button, chip100Button;
    JButton hitButton, stayButton, doubleDownButton;
    JPanel dealerCardPanel, playerCardPanel;
    JLabel gameMoneyLabel;

    public static void main(String[] args) {
        new Blackjack();
    }

    Blackjack() {
        setupCards();
        shuffleCards();
        setupGUI();
    }

    public void setupGUI() {
        frame = new JFrame("Blackjack");
        frame.setSize(1000, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        
        showBettingScreen();
        
        frame.setVisible(true);
    }

    public void showBettingScreen() {
        if (bettingPanel != null) {
            frame.remove(bettingPanel);
        }
        
        bettingPanel = new JPanel();
        bettingPanel.setLayout(null);
        bettingPanel.setBounds(0, 0, 1000, 700);
        bettingPanel.setBackground(new Color(53, 101, 77));
        
        // Player Money Label (top right)
        moneyLabel = new JLabel("Player Money: $" + (int)playerMoney);
        moneyLabel.setBounds(700, 20, 250, 80);
        moneyLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        moneyLabel.setOpaque(true);
        moneyLabel.setBackground(new Color(173, 216, 230));
        moneyLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        moneyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bettingPanel.add(moneyLabel);
        
        // Current Bet Display (center)
        JPanel betDisplayPanel = new JPanel();
        betDisplayPanel.setLayout(new BorderLayout());
        betDisplayPanel.setBounds(325, 250, 350, 120);
        betDisplayPanel.setBackground(new Color(173, 216, 230));
        betDisplayPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        
        currentBetLabel = new JLabel("Current Bet: $" + (int)currentBet);
        currentBetLabel.setFont(new Font("Arial", Font.BOLD, 24));
        currentBetLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        minBetLabel = new JLabel("Minimum bet: $15");
        minBetLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        minBetLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        betDisplayPanel.add(currentBetLabel, BorderLayout.CENTER);
        betDisplayPanel.add(minBetLabel, BorderLayout.SOUTH);
        bettingPanel.add(betDisplayPanel);
        
        // Confirm Bet Button
        confirmBetButton = new JButton("Confirm Bet");
        confirmBetButton.setBounds(375, 400, 250, 60);
        confirmBetButton.setFont(new Font("Arial", Font.BOLD, 18));
        confirmBetButton.setBackground(new Color(173, 216, 230));
        confirmBetButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        confirmBetButton.addActionListener(e -> confirmBet());
        bettingPanel.add(confirmBetButton);
        
        // Chip Buttons (bottom)
        JPanel chipPanel = new JPanel();
        chipPanel.setLayout(new GridLayout(1, 4, 30, 0));
        chipPanel.setBounds(150, 550, 700, 80);
        chipPanel.setBackground(new Color(173, 216, 230));
        chipPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        
        chip1Button = createChipButton("$1", 1);
        chip5Button = createChipButton("$5", 5);
        chip25Button = createChipButton("$25", 25);
        chip100Button = createChipButton("$100", 100);
        
        chipPanel.add(chip1Button);
        chipPanel.add(chip5Button);
        chipPanel.add(chip25Button);
        chipPanel.add(chip100Button);
        
        bettingPanel.add(chipPanel);
        
        frame.add(bettingPanel);
        frame.revalidate();
        frame.repaint();
    }

    public JButton createChipButton(String text, double value) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setBackground(new Color(173, 216, 230));
        button.addActionListener(e -> addToBet(value));
        return button;
    }

    public void addToBet(double amount) {
        if (currentBet + amount <= playerMoney) {
            currentBet += amount;
            currentBetLabel.setText("Current Bet: $" + (int)currentBet);
        }
    }

    public void confirmBet() {
        if (currentBet < 15) {
            JOptionPane.showMessageDialog(frame, "Minimum bet is $15!", "Invalid Bet", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (currentBet > playerMoney) {
            JOptionPane.showMessageDialog(frame, "You don't have enough money!", "Invalid Bet", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        bet = currentBet;
        playerMoney -= bet;
        currentBet = 0;
        
        // Remove betting screen and start game
        frame.remove(bettingPanel);
        startGame();
    }

    public void startGame() {
        ensureDeckSize();
        dealCardstoPlayer();
        showTwoCards();
        showGameScreen();
    }

    public void showGameScreen() {
        if (gamePanel != null) {
            frame.remove(gamePanel);
        }
        
        gamePanel = new JPanel();
        gamePanel.setLayout(null);
        gamePanel.setBounds(0, 0, 1000, 700);
        gamePanel.setBackground(new Color(53, 101, 77));
        
        // Player Money Label (top right)
        gameMoneyLabel = new JLabel("$" + (int)playerMoney);
        gameMoneyLabel.setBounds(850, 20, 120, 50);
        gameMoneyLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gameMoneyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bettingPanel.add(gameMoneyLabel);
        
        // Dealer Card Panel (top)
        dealerCardPanel = new JPanel();
        dealerCardPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        dealerCardPanel.setBounds(0, 50, 1000, 220);
        dealerCardPanel.setBackground(new Color(53, 101, 77));
        
        // Add dealer cards
        for (int i = 0; i < dealerDrawn.size(); i++) {
            JPanel cardSlot = createCardPanel(dealerDrawn.get(i), i == 1);
            dealerCardPanel.add(cardSlot);
        }
        
        gamePanel.add(dealerCardPanel);
        
        // Player Card Panel (middle)
        playerCardPanel = new JPanel();
        playerCardPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        playerCardPanel.setBounds(0, 320, 1000, 220);
        playerCardPanel.setBackground(new Color(53, 101, 77));
        
        // Add player cards
        for (int i = 0; i < playerDrawn.size(); i++) {
            JPanel cardSlot = createCardPanel(playerDrawn.get(i), false);
            playerCardPanel.add(cardSlot);
        }
        
        gamePanel.add(playerCardPanel);
        
        // Action Buttons Panel (bottom)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3, 40, 0));
        buttonPanel.setBounds(150, 580, 700, 70);
        buttonPanel.setBackground(new Color(173, 216, 230));
        buttonPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        
        hitButton = new JButton("Hit");
        hitButton.setFont(new Font("Arial", Font.BOLD, 20));
        hitButton.setBackground(new Color(173, 216, 230));
        hitButton.addActionListener(e -> playerHit());
        
        stayButton = new JButton("Stay");
        stayButton.setFont(new Font("Arial", Font.BOLD, 20));
        stayButton.setBackground(new Color(173, 216, 230));
        stayButton.addActionListener(e -> playerStay());
        
        doubleDownButton = new JButton("Double Down");
        doubleDownButton.setFont(new Font("Arial", Font.BOLD, 20));
        doubleDownButton.setBackground(new Color(173, 216, 230));
        doubleDownButton.addActionListener(e -> playerDoubleDown());
        
        buttonPanel.add(hitButton);
        buttonPanel.add(stayButton);
        buttonPanel.add(doubleDownButton);
        
        gamePanel.add(buttonPanel);
        
        frame.add(gamePanel);
        frame.revalidate();
        frame.repaint();
    }

    public JPanel createCardPanel(Card card, boolean hidden) {
        JPanel cardPanel = new JPanel();
        cardPanel.setPreferredSize(new Dimension(cardWidth, cardHeight));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        cardPanel.setLayout(new BorderLayout());
        
        if (hidden) {
            // Card back design
            cardPanel.setBackground(new Color(200, 100, 100));
            JLabel backLabel = new JLabel("CARD", SwingConstants.CENTER);
            backLabel.setFont(new Font("Arial", Font.BOLD, 20));
            backLabel.setForeground(Color.WHITE);
            cardPanel.add(backLabel, BorderLayout.CENTER);
        } else {
            // Parse card name
            String cardName = card.cardName;
            String suit = cardName.substring(0, cardName.indexOf('_'));
            String rank = cardName.substring(cardName.indexOf('_') + 1);
            
            // Determine color based on suit
            Color suitColor = (suit.equals("hearts") || suit.equals("diamonds")) ? Color.RED : Color.BLACK;
            
            // Get suit symbol
            String suitSymbol = getSuitSymbol(suit);
            
            // Top left corner - rank and suit
            JLabel topLabel = new JLabel(rank + suitSymbol);
            topLabel.setFont(new Font("Arial", Font.BOLD, 24));
            topLabel.setForeground(suitColor);
            topLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 0));
            
            // Center - large suit symbol
            JLabel centerLabel = new JLabel(suitSymbol, SwingConstants.CENTER);
            centerLabel.setFont(new Font("Arial", Font.BOLD, 80));
            centerLabel.setForeground(suitColor);
            
            // Bottom right corner - rank and suit (upside down)
            JLabel bottomLabel = new JLabel(rank + suitSymbol, SwingConstants.RIGHT);
            bottomLabel.setFont(new Font("Arial", Font.BOLD, 24));
            bottomLabel.setForeground(suitColor);
            bottomLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 10));
            
            cardPanel.add(topLabel, BorderLayout.NORTH);
            cardPanel.add(centerLabel, BorderLayout.CENTER);
            cardPanel.add(bottomLabel, BorderLayout.SOUTH);
        }
        
        return cardPanel;
    }
    
    public String getSuitSymbol(String suit) {
        switch (suit) {
            case "hearts": return "♥";
            case "diamonds": return "♦";
            case "clubs": return "♣";
            case "spades": return "♠";
            default: return "";
        }
    }

    public void playerHit() {
        ensureDeckSize();
        
        Card cardRemoved = cardSet.remove(0);
        int cardValueRemoved = cardValues.remove(0);
                
        playerDrawn.add(cardRemoved);
        playerDrawnValues.add(cardValueRemoved);

        usedCards.add(cardRemoved);
        usedCardValues.add(cardValueRemoved);

        // Refresh card display
        updatePlayerCards();
        
        // Disable double down after first hit
        doubleDownButton.setEnabled(false);

        int playerValue = calculateHandValue(playerDrawnValues);

        if (playerValue > 21) {
            JOptionPane.showMessageDialog(frame, "Player Busts! You lose!");
            endRound();
        }
    }

    public void playerStay() {
        int playerValue = calculateHandValue(playerDrawnValues);
        int dealerValue = dealerDrawsUntil17();
        
        // Reveal dealer's hidden card
        updateDealerCards(true);

        determineWinner(playerValue, dealerValue);
        endRound();
    }

    public void playerDoubleDown() {
        if (playerMoney < bet) {
            JOptionPane.showMessageDialog(frame, "Not enough money to double down!");
            return;
        }

        playerMoney -= bet;
        bet *= 2;
        gameMoneyLabel.setText("$" + (int)playerMoney);
        
        ensureDeckSize();
        
        Card cardRemoved = cardSet.remove(0);
        int cardValueRemoved = cardValues.remove(0);
                
        playerDrawn.add(cardRemoved);
        playerDrawnValues.add(cardValueRemoved);

        usedCards.add(cardRemoved);
        usedCardValues.add(cardValueRemoved);

        updatePlayerCards();

        int playerValue = calculateHandValue(playerDrawnValues);

        if (playerValue > 21) {
            JOptionPane.showMessageDialog(frame, "Player Busts! You lose!");
            endRound();
        } else {
            int dealerValue = dealerDrawsUntil17();
            updateDealerCards(true);
            determineWinner(playerValue, dealerValue);
            endRound();
        }
    }

    public void updatePlayerCards() {
        playerCardPanel.removeAll();
        
        for (int i = 0; i < playerDrawn.size(); i++) {
            JPanel cardSlot = createCardPanel(playerDrawn.get(i), false);
            playerCardPanel.add(cardSlot);
        }
        
        playerCardPanel.revalidate();
        playerCardPanel.repaint();
    }

    public void updateDealerCards(boolean revealHidden) {
        dealerCardPanel.removeAll();
        
        for (int i = 0; i < dealerDrawn.size(); i++) {
            boolean hideThisCard = (i == 1 && !revealHidden);
            JPanel cardSlot = createCardPanel(dealerDrawn.get(i), hideThisCard);
            dealerCardPanel.add(cardSlot);
        }
        
        dealerCardPanel.revalidate();
        dealerCardPanel.repaint();
    }

    public void endRound() {
        int option = JOptionPane.showConfirmDialog(frame, 
            "Your money: $" + (int)playerMoney + "\nPlay again?", 
            "Round Over", 
            JOptionPane.YES_NO_OPTION);
        
        if (option == JOptionPane.YES_OPTION && playerMoney >= 15) {
            clearTheBoard();
            frame.remove(gamePanel);
            showBettingScreen();
        } else {
            JOptionPane.showMessageDialog(frame, "Thanks for playing! Final amount: $" + (int)playerMoney);
            System.exit(0);
        }
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
            Card card = new Card(cardName, null);
            cardSet.add(card);
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

    public void ensureDeckSize() {
        if (cardSet.size() < 15) {
            cardSet.addAll(usedCards);
            cardValues.addAll(usedCardValues);
            usedCards.clear();
            usedCardValues.clear();
            shuffleCards();
        }
    }

    public void dealCardstoPlayer() {
        Card card1 = cardSet.remove(0);
        Card card2 = cardSet.remove(0);

        int card1value = cardValues.remove(0);
        int card2value = cardValues.remove(0);

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

    public int dealerDrawsUntil17() {
        int sumDealerCards = calculateHandValue(dealerDrawnValues);

        while (sumDealerCards < 17) {
            ensureDeckSize();
            
            Card cardRemoved = cardSet.remove(0);
            int cardValueRemoved = cardValues.remove(0);
                    
            dealerDrawn.add(cardRemoved);
            dealerDrawnValues.add(cardValueRemoved);

            usedCards.add(cardRemoved);
            usedCardValues.add(cardValueRemoved);
            
            sumDealerCards = calculateHandValue(dealerDrawnValues);
        }

        return sumDealerCards;
    }

    public void determineWinner(int playerValue, int dealerValue) {
        String message = "Player: " + playerValue + "\nDealer: " + dealerValue + "\n\n";

        if (playerValue > 21) {
            message += "Player Bust! Dealer wins!";
        } else if (dealerValue > 21) {
            message += "Dealer Bust! You win!";
            playerMoney += bet * 2;
        } else if (playerValue > dealerValue) {
            message += "You win!";
            playerMoney += bet * 2;
        } else if (playerValue < dealerValue) {
            message += "Dealer wins!";
        } else {
            message += "Push! Tie game.";
            playerMoney += bet;
        }

        gameMoneyLabel.setText("$" + (int)playerMoney);
        JOptionPane.showMessageDialog(frame, message, "Results", JOptionPane.INFORMATION_MESSAGE);
    }

    public void clearTheBoard() {
        playerDrawn.clear();
        playerDrawnValues.clear();
        dealerDrawn.clear();
        dealerDrawnValues.clear();
        bet = 0;
    }
}
