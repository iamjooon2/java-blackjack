package blackjack.domain.game;

import static blackjack.fixture.CardBundleGenerator.getCardBundleOfBlackjack;
import static blackjack.fixture.CardBundleGenerator.getCardBundleOfBust;
import static blackjack.fixture.CardBundleGenerator.getCardBundleOfFifteen;
import static blackjack.fixture.CardBundleGenerator.getCardBundleOfNonBlackjackTwentyOne;
import static blackjack.fixture.CardBundleGenerator.getCardBundleOfTen;
import static blackjack.fixture.CardBundleGenerator.getCardBundleOfTwenty;
import static org.assertj.core.api.Assertions.assertThat;

import blackjack.domain.participant.Dealer;
import blackjack.domain.participant.Player;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class ResultRefereeTest {

    private static final Dealer dealer20 = Dealer.of(getCardBundleOfTwenty());
    private static final Dealer dealer21 = Dealer.of(getCardBundleOfNonBlackjackTwentyOne());
    private static final Dealer dealerBlackjack = Dealer.of(getCardBundleOfBlackjack());
    private static final Dealer dealerBust = Dealer.of(getCardBundleOfBust());

    private static final Player player10 = Player.of("ten", getCardBundleOfTen());
    private static final Player player15 = Player.of("fifteen", getCardBundleOfFifteen());
    private static final Player player20 = Player.of("twenty", getCardBundleOfTwenty());
    private static final Player player21 = Player.of("nonBlackjack21", getCardBundleOfNonBlackjackTwentyOne());
    private static final Player playerBlackjack = Player.of("blackjack", getCardBundleOfBlackjack());
    private static final Player playerBust = Player.of("bust", getCardBundleOfBust());

    private static final List<Integer> LOSE_ONCE = List.of(0, 1, 0);
    private static final List<Integer> WIN_ONCE = List.of(1, 0, 0);
    private static final List<Integer> DRAW_ONCE = List.of(0, 0, 1);

    @DisplayName("딜러가 블랙잭인 경우, 블랙잭인 플레이어는 무승부, 그 외에는 전부 패배한다.")
    @Test
    void dealerBlackjack() {
        List<ResultStatistics> results = new ResultReferee(
                dealerBlackjack, List.of(playerBlackjack, player20, player21, playerBust))
                .getResults(); // 실제로는 해당 시점에 플레이어가 버스트 혹은 3장의 카드를 지닐 수 없다.

        ResultStatistics dealerResult = results.get(0);
        assertThat(getResultCounts(dealerResult)).isEqualTo(List.of(3, 0, 1));
        assertThat(getResultCounts(results.get(1))).isEqualTo(DRAW_ONCE);
        assertThat(getResultCounts(results.get(2))).isEqualTo(LOSE_ONCE);
        assertThat(getResultCounts(results.get(3))).isEqualTo(LOSE_ONCE);
        assertThat(getResultCounts(results.get(4))).isEqualTo(LOSE_ONCE);
    }

    @DisplayName("플레이어가 버스트인 경우 무조건 딜러가 승리한다.")
    @Nested
    class PlayerBustTest {

        @DisplayName("플레이어와 딜러 모두 버스트인 경우, 딜러가 나중에 버스트가 되었으므로 승리한다.")
        @Test
        void playerLoseOnDealerBust() {
            List<ResultStatistics> results = new ResultReferee(dealerBust, List.of(playerBust))
                    .getResults();

            ResultStatistics dealerResult = results.get(0);
            assertThat(getResultCounts(dealerResult)).isEqualTo(WIN_ONCE);
            assertThat(getResultCounts(results.get(1))).isEqualTo(LOSE_ONCE);
        }

        @DisplayName("플레이어가 버스트인 경우, 딜러가 최종적으로 생존해있으므로 승리한다.")
        @Test
        void playerLoseOnDealerNotBust() {
            List<ResultStatistics> results = new ResultReferee(dealer20, List.of(playerBust))
                    .getResults();

            ResultStatistics dealerResult = results.get(0);
            assertThat(getResultCounts(dealerResult)).isEqualTo(WIN_ONCE);
            assertThat(getResultCounts(results.get(1))).isEqualTo(LOSE_ONCE);
        }
    }

    @DisplayName("딜러가 버스트인 경우, 버스트가 아닌 플레이어는 전부 승리한다.")
    @Test
    void dealerBust() {
        List<ResultStatistics> results = new ResultReferee(dealerBust,
                List.of(player10, player15, player20, playerBust))
                .getResults();

        ResultStatistics dealerResult = results.get(0);
        assertThat(getResultCounts(dealerResult)).isEqualTo(List.of(1, 3, 0));
        for (int i : List.of(1, 2, 3)) {
            assertThat(getResultCounts(results.get(i))).isEqualTo(WIN_ONCE);
        }
        assertThat(getResultCounts(results.get(4))).isEqualTo(LOSE_ONCE);
    }

    @DisplayName("플레이어가 블랙잭인 경우, 딜러도 블랙잭이면 무승부, 그 외에는 전부 승리한다.")
    @Nested
    class PlayerBlackjackTest {

        @DisplayName("딜러도 블랙잭이면 서로 무승부가 된다.")
        @Test
        void playerDrawOnDealerBlackjack() {
            List<ResultStatistics> results = new ResultReferee(
                    dealerBlackjack, List.of(playerBlackjack))
                    .getResults();

            ResultStatistics dealerResult = results.get(0);
            assertThat(getResultCounts(dealerResult)).isEqualTo(DRAW_ONCE);
            assertThat(getResultCounts(results.get(1))).isEqualTo(DRAW_ONCE);
        }

        @DisplayName("딜러의 패가 3장 이상으로 구성된 21이어도 블랙잭이 아니므로 플레이어가 승리한다.")
        @Test
        void playerDrawOnDealerNonBlackjack21() {
            List<ResultStatistics> results = new ResultReferee(dealer21, List.of(playerBlackjack))
                    .getResults();

            ResultStatistics dealerResult = results.get(0);
            assertThat(getResultCounts(dealerResult)).isEqualTo(LOSE_ONCE);
            assertThat(getResultCounts(results.get(1))).isEqualTo(WIN_ONCE);
        }
    }

    @DisplayName("딜러가 블랙잭 및 버스트가 아닌 경우 버스트인 플레이어는 패배, 그 외에는 전부 대소비교를 통해 판정한다.")
    @Nested
    class DealerNonBlackjackTest {

        @DisplayName("딜러가 20 이하인 경우")
        @Test
        void simpleComparison() {
            List<ResultStatistics> results = new ResultReferee(dealer20,
                    List.of(player21, player20, player10, player15, playerBust))
                    .getResults();

            ResultStatistics dealerResult = results.get(0);
            assertThat(getResultCounts(dealerResult)).isEqualTo(List.of(3, 1, 1));
            assertThat(getResultCounts(results.get(1))).isEqualTo(WIN_ONCE);
            assertThat(getResultCounts(results.get(2))).isEqualTo(DRAW_ONCE);
            for (int i : List.of(3, 4, 5)) {
                assertThat(getResultCounts(results.get(i))).isEqualTo(LOSE_ONCE);
            }
        }

        @DisplayName("딜러의 패가 블랙잭이 아닌 21인 경우")
        @Test
        void simpleComparisonDealerNonBlackjack21() {
            List<ResultStatistics> results = new ResultReferee(dealer21,
                    List.of(playerBlackjack, player21, player20, player10, player15, playerBust))
                    .getResults();

            ResultStatistics dealerResult = results.get(0);
            assertThat(getResultCounts(dealerResult)).isEqualTo(List.of(4, 1, 1));
            assertThat(getResultCounts(results.get(1))).isEqualTo(WIN_ONCE);
            assertThat(getResultCounts(results.get(2))).isEqualTo(DRAW_ONCE);
            for (int i : List.of(3, 4, 5, 6)) {
                assertThat(getResultCounts(results.get(i))).isEqualTo(LOSE_ONCE);
            }
        }
    }

    private List<Integer> getResultCounts(ResultStatistics dealerResult) {
        return Arrays.stream(ResultType.values())
                .map(type -> dealerResult.getStats().get(type).toInt())
                .collect(Collectors.toList());
    }
}