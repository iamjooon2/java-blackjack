package blackjack.domain.player;

import blackjack.domain.card.Card;

import java.util.List;

public class Dealer extends Player {
    private static final int DEALER_CRITICAL_SCORE = 16;
    private static final int DEALER_INITIAL_CARDS_SIZE = 1;
    private static final String KOREAN_NAME = "딜러";

    public Dealer() {
        this.name = KOREAN_NAME;
    }

    public boolean isUnderCriticalScore() {
        return calculateScore() <= DEALER_CRITICAL_SCORE;
    }

    @Override
    public List<Card> getInitialCards() {
        return this.cards.getCards().subList(START_INDEX, DEALER_INITIAL_CARDS_SIZE);
    }
}