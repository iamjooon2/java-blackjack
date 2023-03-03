package domain;

import domain.card.Card;
import domain.card.Deck;
import domain.card.Denomination;
import domain.card.Suit;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DeckTest {

    @Test
    void 덱은_52가지_종류의_카드가_있다() {
        //given
        List<Card> expectedCards = initCards();
        Deck deck = Deck.create();

        //when

        // then
        assertThat(deck.getCards()).doesNotHaveDuplicates();
    }

    @Test
    void 카드를_뽑을_수_있다() {
        //given
        Deck deck = Deck.create();
        List<Card> cards = initCards();

        //when
        Card card = deck.pollAvailableCard();

        //then
        assertThat(card).isIn(cards);
    }

    @Test
    void 뽑은_카드는_제거된다() {
        //given
        Deck deck = Deck.create();

        //when
        for (int i = 0; i < 52; i++) {
            deck.pollAvailableCard();
        }

        //then
        assertThatThrownBy(deck::pollAvailableCard)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("[ERROR] 뽑을 수 있는 카드가 존재하지 않습니다");
    }

    private List<Card> initCards() {
        return Arrays.stream(Denomination.values())
                .flatMap(rank -> Arrays.stream(Suit.values())
                        .map(suit -> new Card(rank, suit)))
                .collect(Collectors.toUnmodifiableList());
    }
}
