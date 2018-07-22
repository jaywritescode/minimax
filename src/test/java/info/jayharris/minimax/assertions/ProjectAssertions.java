package info.jayharris.minimax.assertions;

import info.jayharris.minimax.transposition.BaseTranspositionTable;
import org.assertj.core.api.Assertions;

public class ProjectAssertions extends Assertions {

    public static BaseTranspositionTableAssert assertThat(BaseTranspositionTable actual) {
        return new BaseTranspositionTableAssert(actual);
    }
}
