package blackjack.domain.result;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

public enum Result {
    WIN("승", 1),
    STANDOFF("무", 0),
    LOSE("패", -1);

    private final String result;
    private final int compareResult;

    Result(String result, int compareResult) {
        this.result = result;
        this.compareResult = compareResult;
    }

    public Result reverse(Result result) {
        if (result.equals(WIN)) {
            return LOSE;
        }
        if (result.equals(LOSE)) {
            return WIN;
        }
        return result;
    }

    public static Map<Result, Integer> countByResults(List<Result> results) {
        return Arrays.stream(values())
                .collect(toMap(result -> result, result -> result.count(results)));
    }

    public int count(List<Result> results) {
        return (int) results.stream()
                .filter(this::equals)
                .count();
    }

    public String getResult() {
        return this.result;
    }

    public int getCompareResult() {
        return this.compareResult;
    }
}
