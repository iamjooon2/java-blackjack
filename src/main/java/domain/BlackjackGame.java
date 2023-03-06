package domain;

import domain.card.Deck;
import domain.participant.Participant;
import domain.participant.Participants;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BlackjackGame {

    private final Deck deck;
    private final Participants participants;

    private BlackjackGame(Participants participants) {
        this.deck = Deck.create();
        this.participants = participants;
    }

    public static BlackjackGame of(List<String> playersName) {
        return new BlackjackGame(Participants.of(playersName));
    }

    public void start() {
        participants.initHand(deck);
    }

    public void playDealerTurn() {
        participants.playDealerTurn(deck);
    }

    public boolean canDealerHit() {
        return participants.canDealerHit();
    }

    public void giveCardToParticipant(Participant player) {
        player.addCard(deck.pollAvailableCard());
    }

    public List<String> getPlayersName() {
        return participants.getPlayers().stream()
                .map(Participant::getName)
                .collect(Collectors.toList());
    }

    public Map<String, PlayerGameResult> getResult() {
        return participants.getResult();
    }

    public Participant getDealer() {
        return participants.getDealer();
    }

    public List<Participant> getPlayers() {
        return participants.getPlayers();
    }
}