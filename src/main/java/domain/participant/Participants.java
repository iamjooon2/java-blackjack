package domain.participant;

import domain.PlayerGameResult;
import domain.card.Deck;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Participants {

    private static final int MIN_PLAYER_COUNT = 1;
    private static final int MAX_PLAYER_COUNT = 7;
    private static final String ERROR_PLAYER_COUNT = "[ERROR] 플레이어의 수는 1 ~ 7 이내여야 합니다";
    private static final String ERROR_DUPLICATED_NAME = "[ERROR] 플레이어의 이름은 중복될 수 없습니다";

    private final Participant dealer;
    private final List<Participant> players;

    private Participants(List<Participant> players) {
        this.dealer = new Dealer();
        this.players = players;
    }

    public static Participants of(List<String> playersName) {
        validate(playersName);

        List<Participant> players = playersName.stream()
                .map(Player::from)
                .collect(Collectors.toList());

        return new Participants(players);
    }

    private static void validate(List<String> names) {
        validatePlayersCount(names);
        validateDuplication(names);
    }

    private static void validatePlayersCount(List<String> names) {
        if (names.size() < MIN_PLAYER_COUNT || names.size() > MAX_PLAYER_COUNT) {
            throw new IllegalArgumentException(ERROR_PLAYER_COUNT);
        }
    }

    private static void validateDuplication(List<String> names) {
        long removedDistinctCount = names.stream()
                .map(String::trim)
                .distinct()
                .count();

        if (removedDistinctCount != names.size()) {
            throw new IllegalArgumentException(ERROR_DUPLICATED_NAME);
        }
    }

    public void initHand(Deck deck) {
        dealer.initHand(deck.pollTwoCards());
        players.forEach(player -> player.initHand(deck.pollTwoCards()));
    }

    public void playDealerTurn(Deck deck) {
        dealer.addCard(deck.pollAvailableCard());
    }

    public boolean canDealerHit() {
        return dealer.canHit();
    }

    public boolean isDealerBust() {
        return dealer.isBust();
    }

    public int getDealerScore() {
        return dealer.calculateScore();
    }

    public boolean isDealerBlackjack() {
        return dealer.isBlackjack();
    }

    public Participant getDealer() {
        return dealer;
    }

    public List<Participant> getPlayers() {
        return Collections.unmodifiableList(players);
    }
}
