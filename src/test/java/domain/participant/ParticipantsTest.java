package domain.participant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import domain.card.Deck;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Participants는 ")
class ParticipantsTest {

    @ParameterizedTest(name = "player가 총 1~7명 일때만 생성된다 name = {0}")
    @ValueSource(strings = {"가비", "연어,가비,애쉬,우가,럿고,비버,아코"})
    void player_가_총_1에서_7명이다(String names) {
        //given

        //when

        //then
        assertDoesNotThrow(() -> Participants.of(List.of(names.split(","))));
    }

    @ParameterizedTest(name = "player가 총 1~7명이 아니면 예외가 발생한다 name = {0}")
    @ValueSource(strings = {",", "연어,가비,애쉬,우가,럿고,비버,아코,네오"})
    void player_가_총_1에서_7명이_아니면_예외가_발생한다(String names) {
        //given

        //when

        //then
        assertThatThrownBy(() -> Participants.of(List.of(names.split(","))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 플레이어의 수는 1 ~ 7 이내여야 합니다");
    }

    @Test
    void 중복된_이름을_입력하면_예외가_발생한다() {
        //given
        List<String> names = List.of("도비", "   도비");

        //when

        //then
        assertThatThrownBy(() -> Participants.of(names))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 플레이어의 이름은 중복될 수 없습니다");
    }

    @Test
    void 카드_두_장을_받고_시작한다() {
        //given
        List<String> players = List.of("연어", "가비");
        Participants participants = Participants.of(players);

        //when
        participants.initHand(Deck.create());

        //then
        assertThat(participants.getDealer().getCards()).hasSize(2);
        assertThat(participants.getPlayers())
                .map(Participant::getCards)
                .allMatch(cards -> cards.size() == 2);
    }
}
