# Briscola

## How was it made
We have written a minimalistic api that enables everyone to build their own simple card game.
All the classes are located in the package [`us.teamronda.briscola.api`](../src/main/java/us/teamronda/briscola/api) and are split into:
- interfaces: they hold the necessary methods and their respective javadocs
- abstract classes: they implement their respective interface and hold some code that is shared between different card games. For example the class `AbstractDeck` holds a list of cards and implements some methods to take out cards from the deck itself, which likely does not need any custom implementation from other developers.

Now let's quickly get into the interfaces defined:

### [ICard](../src/main/java/us/teamronda/briscola/api/cards/ICard.java)
This interface represents, as you might have guessed, a single playing card.
It holds two methods returning an Enum:
- ICard#getType: returns a [CardType](../src/main/java/us/teamronda/briscola/api/cards/CardType.java) enum, which represents the number or figure on the card.
- ICard#getSeed: returns a (pretty self-explanatory) [Seed](../src/main/java/us/teamronda/briscola/api/cards/Seed.java) enum
- ICard#getPoints: returns an integer and uses the Seed and CardType to calculate the worth of this card object

### [IDeck](../src/main/java/us/teamronda/briscola/api/deck/IDeck.java)
This interface represents a Deck, aka a glorified collection of cards with all sorts of methods to interact with said collection.

Instead of directly implementing this interface, we advise to extend the abstract class [AbstractDeck](../src/main/java/us/teamronda/briscola/api/deck/AbstractDeck.java) and to start working from there.
Want an example? Check out our [Deck](../src/main/java/us/teamronda/briscola/objects/Deck.java) class.

### [IPlayer](../src/main/java/us/teamronda/briscola/api/player/IPlayer.java)
This interface represents any Player participating in the game, being either a human or a bot. The object holds the integer representing the points of the player and a collection of cards representing the player's hand.
Remember that all players are supposed to have **unique** usernames.

Instead of directly implementing this interface, we advise to extend the abstract class [AbstractPlayer](../src/main/java/us/teamronda/briscola/api/player/AbstractPlayer.java) and to start working from there.
Want an example? Check out our [Player](../src/main/java/us/teamronda/briscola/objects/Player.java) class.

### [GameLoop](../src/main/java/us/teamronda/briscola/api/game/GameLoop.java)
This interface defines a few methods which define some functions that need to be called in various moments of the game. This is the heart of the project since most of the game logic will be implemented here.

As always we provide an abstract class [AbstractGameLoop](../src/main/java/us/teamronda/briscola/api/game/AbstractGameLoop.java) with some methods already implemented, let's briefly see how does it work:
- All the players are stored in a list (why not a set? see the code for an explanation) and said players can be accessed calling the `getWhoIsPlaying()` method.
- The `getWhoIsPlaying()` method accesses a player in a certain index, defined as `playerIndex` in the code, which needs to be **manually** incremented to get to the next player (and **manually** reset to zero at the end of the game).
- The cards played in a turn by the players are stored in a `Map<String, ICard>`, which associates the player's (unique) username to the card they played.
- To execute a player's turn the method `tick()` needs to be called with the `IPlayer` and `ICard` (the card they played) as parameters. This method will differ from implementation to implementation.

## The Game Flow
This paragraph will explain what happens in the code of LogicGame.java during a standard turn.
Keep in mind that we tried to write the logic of the game for N players, even if our gui supports only two players.

Our idea is to make all the bots play until the first human player is found: that's exactly what the `tickBots()` method does (that just calls `tick()` for every bot).
This way we are 100% sure that the next player is human, and we have to wait for their input to continue. When we receive their input we call the `tick()` method, and then we call the `tickBots()` method again, since we do not know how many bots there are.

When everyone has played the program calculates who won and adds the points to the winner, and we wait for the human player to advance to the next turn. In our project this is done by clicking the "Next turn" button, but this could be automated with a task calling the `nextTurn()` method.

The flowchart of the method calls would roughly look something like this (JavaFX event calls not included):
```mermaid
---
title: LogicGame.java
---

flowchart LR
    start["start()"] --> bots["tickBots()"]
    bots --> isOver{Has everyone played?}

    isOver --> |yes|calc["Calculate winner"] --> next["Next Turn"] --> bots
    isOver --> |no|player["tick()"]
    player --> bots
```

## Issues
Obviously our api is not perfect and was made only for simple card games, not to be perfect in every aspect. Here's how could it be improved:
- We assign to each card a value, so games where cards are evaluated in groups (like poker for example) will not be implementable.
- We support only one type of cards, if someone wants to use a different one they will have to rewrite the CardType and Seed enums

## GUI(s) :eyes:
Learning about what's under the hood is sure interesting, but also what the user sees is important. So in this paragraph we will dive into how our ~~beautiful~~ ui was made and what (not) to do to achieve this result.

### CardComponent ♠️
This is the custom JavaFX object we made to represent a playing card: it uses two [Rectangle](https://openjfx.io/javadoc/21/javafx.graphics/javafx/scene/shape/Rectangle.html) objects to show the front and the back of the card, which are "held together" by a [Stackpane](https://openjfx.io/javadoc/21/javafx.graphics/javafx/scene/layout/StackPane.html).
To show only one side, the other Rectangle's width is scaled down to zero (using the [Node#setScaleX](https://openjfx.io/javadoc/21/javafx.graphics/javafx/scene/Node.html#setScaleX(double)) method); we also provide a method to flip a card at runtime.
The CardComponent class itself extends StackPane, this means that we can use this object as a JavaFX node and add it to the gui without any hassle.

### Scene switching 🔄️
At one point we were in a bit of a pickle with this one: not because switching scenes was difficult, but because we had a lot of repeated code in all the Controllers (and we don't do that here).
After some trial and error (caused by how objects are initialized in JavaFX), we ended up writing the [SceneSwitcher](../src/main/java/us/teamronda/briscola/gui/SceneSwitcher.java) class. 
The class has a single [Node](https://openjfx.io/javadoc/21/javafx.graphics/javafx/scene/Node.html) variable called the `sceneHolder` and a single `switchTo` method which takes a [Guis](../src/main/java/us/teamronda/briscola/gui/Guis.java) enum as a parameter and (yes, you guessed it) switches the Scene.
Let's unpack the class slowly:
- The `sceneHolder` is needed to get a Stage instance through the Node#getScene#getWindow methods, although this returns a [Window](https://openjfx.io/javadoc/21/javafx.graphics/javafx/stage/Window.html) object which is a super class of Stage: so we need to cast it to Stage to get everything working. <br>Are unchecked casts bad? Yes... <br>But does it work? Also yes... so we are going to keep it that way.
- Why use a protected setter instead of passing the Node object directly via constructor? The JavaFX variables are not initialized when the constructor is called, so when the switchTo method will be called a NullPointerException will be thrown.

Every controller extends SceneSwitcher and in their `initialize()` methods the `sceneHolder` is set using the setter, and we're set!

## Other questions:
❓ How does the bot play?<p>
💬 It just plays a random card. Implementing an algorithm for the bot is outside the scope of this project. If anyone wants to work on it, feel free to implement it yourself!

---

❓ Where did you get the cards' assets from?<p>
💬 They are from [another repo](https://github.com/profumato4/Briscola), they implemented Briscola as well using Swing as the gui framework. Go check them out. (The repo is under MIT License).

---

❓ Why is your base package `us.teamronda.briscola`?<p>
💬 "Ronda" is a moroccan card game, so since we are a team of two people we thought of this package name. It just sounded cool.
