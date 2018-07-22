package info.jayharris.minimax.assertions;

import info.jayharris.minimax.TestState;
import info.jayharris.minimax.transposition.BaseTranspositionTable;
import org.assertj.core.api.AbstractAssert;

import static org.assertj.core.api.Assertions.assertThat;

public class BaseTranspositionTableAssert extends AbstractAssert<BaseTranspositionTableAssert, BaseTranspositionTable> {

    public BaseTranspositionTableAssert(BaseTranspositionTable actual) {
        super(actual, BaseTranspositionTableAssert.class);
    }

    public BaseTranspositionTableAssert hasValue(TestState state, long value) {
        assertThat(actual.contains(state)).isTrue();
        assertThat(actual.get(state)).hasValue(value);
        return this;
    }
}
