package domain.participant;

import domain.card.Card;
import domain.card.Deck;
import domain.result.PlayerGameResult;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Participants {

    private static final int MIN_PLAYER_COUNT = 1;
    private static final int MAX_PLAYER_COUNT = 7;
    private static final int MULTIPLY_VALUE_FOR_DEALER_PROFIT = -1;
    private static final String ERROR_PLAYER_COUNT = "[ERROR] 플레이어의 수는 1 ~ 7 이내여야 합니다";
    private static final String ERROR_DUPLICATED_NAME = "[ERROR] 플레이어의 이름은 중복될 수 없습니다";

    private final Dealer dealer;
    private final List<Player> players;

    private Participants(final List<Player> players) {
        this.dealer = new Dealer();
        this.players = players;
    }

    public static Participants of(final List<String> playersName) {
        validate(playersName);

        List<Player> players = playersName.stream()
                .map(Player::from)
                .collect(Collectors.toList());

        return new Participants(players);
    }

    private static void validate(final List<String> names) {
        validatePlayersCount(names);
        validateDuplication(names);
    }

    private static void validatePlayersCount(final List<String> names) {
        if (names.size() < MIN_PLAYER_COUNT || names.size() > MAX_PLAYER_COUNT) {
            throw new IllegalArgumentException(ERROR_PLAYER_COUNT);
        }
    }

    private static void validateDuplication(final List<String> names) {
        Set<String> setNames = new HashSet<>(names);
        if (setNames.size() != names.size()) {
            throw new IllegalArgumentException(ERROR_DUPLICATED_NAME);
        }
    }

    public void initHand(final Deck deck) {
        dealer.initHand(deck.pollTwoCards());
        players.forEach(player -> player.initHand(deck.pollTwoCards()));
    }

    public void playDealerTurn(final Deck deck) {
        dealer.addCard(deck.pollAvailableCard());
    }

    public boolean canDealerHit() {
        return dealer.canHit();
    }

    public Map<String, Integer> getPlayerBettingResult() {
        Map<String, Integer> betResult = new LinkedHashMap<>();
        for (Player player : getPlayers()) {
            PlayerGameResult playerGameResult = getPlayerGameResult(player);
            int reward = playerGameResult.calculateBenefit(player.getBetAmount());
            betResult.put(player.getName(), reward);
        }

        return new LinkedHashMap<>(betResult);
    }

    private PlayerGameResult getPlayerGameResult(final Player player) {
        if (isPlayerBlackjack(player)) {
            return PlayerGameResult.BLACKJACK;
        }
        if (isPlayerWin(player)) {
            return PlayerGameResult.WIN;
        }
        if (isDraw(player)) {
            return PlayerGameResult.DRAW;
        }
        return PlayerGameResult.LOSE;
    }

    private boolean isPlayerBlackjack(final Player player) {
        return player.isBlackjack() && !dealer.isBlackjack();
    }

    private boolean isPlayerWin(final Player player) {
        return (player.calculateScore() > dealer.calculateScore() && !player.isBust()) || dealer.isBust();
    }

    private boolean isDraw(final Player player) {
        return player.calculateScore() == dealer.calculateScore();
    }

    public int getDealerBettingResult() {
        return getPlayerBettingResult().values().stream()
                .mapToInt(money -> money)
                .sum() * MULTIPLY_VALUE_FOR_DEALER_PROFIT;
    }

    public List<String> getPlayersName() {
        return players.stream()
                .map(player -> player.getName())
                .collect(Collectors.toList());
    }

    public String getDealerName() {
        return dealer.getName();
    }

    public Card getDealerCardWithInvisible() {
        return dealer.getCardWithInvisible();
    }

    public Dealer getDealer() {
        return dealer;
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }
}
