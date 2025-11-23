# Blackjack in Java

## Features

- **Full GUI Interface**: Interactive buttons and cards
- **Realistic Card Display**: Cards with suits and colors
- **Casino Rules**: 
  - 3:2 payout for Blackjack
  - Dealer hits until soft 17
  - Double Down option for the first two cards
  - Push returns bet
- **Betting System**: 
  - Starting balance is $1,000
  - Minimum bet of $15
  - Quick-bet chips: $1, $5, $25, $100

## Game Rules

1. **Card Values**:
   - Number cards: Number value
   - Face cards: 10 points
   - Aces: 1 or 11

2. **Dealer Rules**:
   - Dealer hits on 16 or less
   - Dealer stays on 17 or more

3. **Betting**:
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
