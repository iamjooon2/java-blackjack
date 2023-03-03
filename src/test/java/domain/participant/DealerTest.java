package domain.participant;

import domain.card.Card;
import domain.card.Denomination;
import domain.card.Suit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("딜러는 ")
class DealerTest {

    @Test
    void 카드를_가질_수_있다() {
        //given
        Dealer dealer = new Dealer();
        int beforeCount = dealer.getCards().size();

        //when
        dealer.addCard(new Card(Denomination.ACE, Suit.CLUB));

        //then
        int afterCount = dealer.getCards().size();

        assertThat(beforeCount + 1).isEqualTo(afterCount);
    }

    @Test
    void 카드들의_합을_계산_할_수_있다() {
        //given
        Dealer dealer = new Dealer();
        List<Card> cards = List.of(new Card(Denomination.ACE, Suit.CLUB), new Card(Denomination.EIGHT, Suit.HEART));
        cards.forEach(dealer::addCard);

        //when
        int totalScore = dealer.calculateScore();

        //then
        assertThat(totalScore).isEqualTo(19);
    }

    @Test
    void 카드의_합이_16_이하면_stand가_아니다() {
        //given
        Dealer dealer = new Dealer();
        List<Card> cards = List.of(new Card(Denomination.ACE, Suit.CLUB), new Card(Denomination.FIVE, Suit.HEART));
        cards.forEach(dealer::addCard);

        //when

        //then
        assertThat(dealer.isStand()).isFalse();
    }

    @Test
    void 카드의_합이_16_초과면_stand() {
        //given
        Dealer dealer = new Dealer();
        List<Card> cards = List.of(new Card(Denomination.ACE, Suit.CLUB), new Card(Denomination.SIX, Suit.HEART));
        cards.forEach(dealer::addCard);

        //when

        //then
        assertThat(dealer.isStand()).isTrue();
    }

    @Test
    void 카드들의_합이_21_이하라면_bust_가_아니다() {
        //given
        Dealer dealer = new Dealer();

        //when

        //then
        assertThat(dealer.isBust()).isFalse();
    }

    @Test
    void 카드들의_합이_21_초과라면_bust() {
        //given
        Dealer dealer = new Dealer();
        List<Card> cards = List.of(new Card(Denomination.FIVE, Suit.CLUB),
                new Card(Denomination.TEN, Suit.HEART),
                new Card(Denomination.TEN, Suit.SPADE));

        //when
        cards.forEach(dealer::addCard);

        //then
        assertThat(dealer.isBust()).isTrue();
    }
}