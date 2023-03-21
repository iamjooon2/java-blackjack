package dto;

import java.util.Map;

public class ResultDto {
    private final Map<String, Integer> playerResult;
    private final int dealerResult;

    private ResultDto(final Map<String, Integer> playerResult, final int dealerResult) {
        this.playerResult = playerResult;
        this.dealerResult = dealerResult;
    }

    public static ResultDto of(final Map<String, Integer> playerResult, final int dealerResult) {
        return new ResultDto(playerResult, dealerResult);
    }

    public Map<String, Integer> getPlayerResult() {
        return playerResult;
    }

    public int getDealerResult() {
        return dealerResult;
    }
}
