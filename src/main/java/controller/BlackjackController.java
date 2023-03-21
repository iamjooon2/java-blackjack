package controller;

import domain.card.Deck;
import domain.participant.Participants;
import domain.participant.Player;
import dto.ParticipantDto;
import dto.ResultDto;
import java.util.List;
import java.util.stream.Collectors;
import view.InputView;
import view.OutputView;

public class BlackjackController {

    public void run() {
        try {
            Deck deck = Deck.create();
            Participants participants = Participants.of(getPlayersName());

            doGame(deck, participants);

            printResult(participants);
        } catch (IllegalArgumentException e) {
            OutputView.printError(e);
        }
    }

    private List<String> getPlayersName() {
        try {
            return InputView.readPlayersName();
        } catch (IllegalArgumentException e) {
            OutputView.printError(e);
            return getPlayersName();
        }
    }

    private void doGame(final Deck deck, final Participants participants) {
        initBetting(participants);

        initParticipantsHand(deck, participants);
        runPlayersTurn(deck, participants);
        runDealerTurn(deck, participants);
    }

    private void initBetting(final Participants participants) {
        List<Player> players = participants.getPlayers();
        for (Player player : players) {
            betEachPlayer(player);
        }
    }

    private void betEachPlayer(final Player player) {
        try {
            player.betPlayer(InputView.readBetMoney(ParticipantDto.from(player)));
        } catch (IllegalArgumentException e) {
            OutputView.printError(e);
            betEachPlayer(player);
        }
    }

    private void initParticipantsHand(final Deck deck, final Participants participants) {
        OutputView.printStartMessage(participants.getPlayers().stream()
                .map(player -> ParticipantDto.from(player))
                .collect(Collectors.toList()));

        participants.initHand(deck);

        OutputView.printDealerCard(ParticipantDto.from(participants.getDealer()));
        OutputView.printPlayersCard(participants.getPlayers().stream()
                .map(player -> ParticipantDto.from(player))
                .collect(Collectors.toList()));
    }

    public void runPlayersTurn(final Deck deck, final Participants participants) {
        List<Player> players = participants.getPlayers();
        for (Player player : players) {
            runPlayerTurn(deck, player);
        }
        InputView.closeScanner();
    }

    private void runPlayerTurn(final Deck deck, final Player player) {
        while (!player.isBust() && isCommandHit(player)) {
            player.addCard(deck.pollAvailableCard());
            ParticipantDto playerDto = ParticipantDto.from(player);
            OutputView.printPlayerCard(playerDto);
        }
    }

    private boolean isCommandHit(final Player player) {
        try {
            String targetCommand = InputView.readHit(ParticipantDto.from(player));
            return HitCommand.HIT == HitCommand.find(targetCommand);
        } catch (IllegalArgumentException e) {
            OutputView.printError(e);
            return isCommandHit(player);
        }
    }

    private void runDealerTurn(final Deck deck, final Participants participants) {
        if (participants.canDealerHit()) {
            participants.playDealerTurn(deck);
            OutputView.printDealerHit();
        }
    }

    private void printResult(final Participants participants) {
        ParticipantDto dealerDto = ParticipantDto.from(participants.getDealer());
        List<ParticipantDto> playerDtos = participants.getPlayers().stream()
                .map(player -> ParticipantDto.from(player))
                .collect(Collectors.toList());

        OutputView.printAllHands(dealerDto, playerDtos);

        ResultDto resultDto = ResultDto.of(participants.getPlayerBettingResult(),
                participants.getDealerBettingResult());
        OutputView.printBettingResult(resultDto);
    }
}
