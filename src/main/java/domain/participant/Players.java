package domain.participant;

import domain.card.Deck;

import java.util.List;
import java.util.stream.Collectors;

public class Players {
    private static final int MIN_PLAYER_COUNT = 1;
    private static final int MAX_PLAYER_COUNT = 7;
    private static final String ERROR_PLAYER_COUNT = "[ERROR] 플레이어의 수는 1 ~ 7 이내여야 합니다";
    private static final String ERROR_DUPLICATED_NAME = "[ERROR] 플레이어의 이름은 중복될 수 없습니다";

    private final List<Participant> players;

    private Players(List<Participant> players) {
        this.players = players;
    }

    public static Players of(List<String> names) {
        validate(names);

        return new Players(names.stream()
                .map(Player::from)
                .collect(Collectors.toList()));
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

    public void initPlayersHand(Deck deck) {
        players.forEach(player -> player.initHand(deck.pollTwoCards()));
    }

    public List<Participant> toList() {
        return players;
    }
}
