# Blackjack in Java

## Features

- **Full GUI Interface**: Modern graphical interface with card animations and interactive buttons
- **Realistic Card Display**: Playing cards with proper suit symbols (♥, ♦, ♣, ♠) and color-coded suits
- **Casino Rules**: Standard Blackjack rules including:
  - 3:2 payout for Blackjack
  - Dealer hits until 17
  - Double Down option on first two cards
  - Push (tie) returns bet
- **Betting System**: 
  - Starting bankroll of $1,000
  - Minimum bet of $15
  - Quick-bet chips: $1, $5, $25, $100
- **Automatic Deck Management**: Deck automatically reshuffles when running low
- **Ace Handling**: Aces count as 11 or 1, automatically adjusted to prevent busting

## Game Rules

1. **Card Values**:
   - Number cards (2-10): Face value
   - Face cards (J, Q, K): 10 points
   - Aces: 11 or 1 (automatically adjusted)

2. **Dealer Rules**:
   - Dealer must hit on 16 or less
   - Dealer must stay on 17 or more

3. **Betting**:
   - Bets must be in increments of $5
   - Minimum bet is $15
   - Cannot bet more than your available money
   - Game ends when you have less than $15

## Running the Game

### Requirements
- Java Development Kit (JDK) 8 or higher

### Compilation
```bash
javac Blackjack.java
```

### Execution
```bash
java Blackjack
```

## Code Structure

### Main Components

- **Card Class**: Represents individual playing cards with name and image icon
- **GUI Components**:
  - Betting screen with chip buttons
  - Game screen with card displays
  - Action buttons (Hit, Stay, Double Down)
- **Game Logic**:
  - Card dealing and shuffling
  - Hand value calculation with Ace handling
  - Win/loss determination
  - Money management

### Key Methods

- `setupCards()`: Initializes the 52-card deck with values
- `shuffleCards()`: Randomizes the deck order
- `calculateHandValue()`: Computes hand total with proper Ace handling
- `dealerDrawsUntil17()`: Implements dealer drawing logic
- `determineWinner()`: Evaluates final hands and awards winnings
- `createCardPanel()`: Renders individual playing cards with suit symbols

## Game Flow

1. **Betting Phase**:
   - Player selects bet amount using chip buttons
   - Confirms bet to start round

2. **Initial Deal**:
   - Player receives 2 cards (both visible)
   - Dealer receives 2 cards (1 hidden)
   - Check for Blackjack (21 with first 2 cards)

3. **Player's Turn**:
   - Choose to Hit, Stay, or Double Down
   - Continue until Stay, Bust, or Double Down

4. **Dealer's Turn**:
   - Dealer reveals hidden card
   - Dealer draws until reaching 17 or higher

5. **Resolution**:
   - Compare final hands
   - Award winnings or collect losses
   - Option to play another round


## Technical Notes

- **Deck Management**: Automatically reshuffles when fewer than 15 cards remain
- **Display**: 1000x700 pixel window with green felt background
- **Card Size**: 150x200 pixels per card
- **Color Scheme**: Red suits (♥♦) and black suits (♣♠) for realism
