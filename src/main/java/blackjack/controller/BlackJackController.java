package blackjack.controller;

import blackjack.domain.deck.Deck;
import blackjack.domain.gamer.Dealer;
import blackjack.domain.gamer.Player;
import blackjack.domain.gamer.Players;
import blackjack.domain.rule.PlayerAnswer;
import blackjack.view.InputView;
import blackjack.view.OutputView;

import java.util.List;

public class BlackJackController {

    public static Players createPlayers() {
        List<String> names = InputView.askPlayerNames().get();
        return Players.of(InputView.askPlayerBettingMoney(names));
    }

    public static void drawMoreCard(Dealer dealer, Players players, Deck deck) {
        hitOrStandForPlayers(players, deck);
        drawCardForDealer(dealer, deck);
    }

    private static void hitOrStandForPlayers(Players players, Deck deck) {
        for (Player player : players) {
            hitOrStand(player, deck);
        }
    }

    private static void drawCardForDealer(Dealer dealer, Deck deck) {
        while (dealer.canDrawCard()) {
            dealer.draw(deck.pick());
            OutputView.printDealerDrewCard();
        }
    }

    private static void hitOrStand(Player player, Deck deck) {
        while (player.canDrawCard() && wantMoreCard(player)) {
            player.draw(deck.pick());
            OutputView.printPlayerHand(player);
        }
    }

    private static boolean wantMoreCard(Player player) {
        PlayerAnswer answer = PlayerAnswer.of(InputView.askHitOrStand(player));
        return answer.isYes();
    }
}