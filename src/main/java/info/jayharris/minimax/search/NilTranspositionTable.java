package info.jayharris.minimax.search;

import info.jayharris.minimax.State;

import java.util.OptionalDouble;

public class NilTranspositionTable implements TranspositionTable {

    static NilTranspositionTable instance;

    @Override
    public void setUtilityValue(State state, double value) {
    }

    @Override
    public OptionalDouble getUtilityValue(State state) {
        return OptionalDouble.empty();
    }

    public static NilTranspositionTable getInstance() {
        if (instance == null) {
            instance = new NilTranspositionTable();
        }
        return instance;
    }
}
