package domain.participant;

import domain.card.Card;
import domain.card.Hand;

import java.util.Collections;
import java.util.List;

public abstract class Participant {

    private static final int BLACKJACK = 21;

    private final Hand hand;

    Participant() {
        this.hand = new Hand();
    }

    public void addCard(Card card) {
        hand.addCard(card);
    }

    public int calculateScore() {
        return hand.getScore();
    }

    public boolean isBust() {
        return calculateScore() > BLACKJACK;
    }

    public void initHand(List<Card> twoCards) {
        twoCards.forEach(hand::addCard);
    }

    public abstract boolean isStay();

    public List<Card> getCards() {
        return Collections.unmodifiableList(hand.toList());
    }
}
