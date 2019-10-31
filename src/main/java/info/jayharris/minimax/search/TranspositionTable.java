package info.jayharris.minimax.search;

import info.jayharris.minimax.Action;
import info.jayharris.minimax.State;

import java.util.OptionalDouble;

public interface TranspositionTable<S extends State<S, A>, A extends Action<S, A>> {

    void setUtilityValue(S state, double value);

    OptionalDouble getUtilityValue(S state);
}
