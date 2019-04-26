package info.jayharris.minimax.transposition;

import info.jayharris.minimax.State;

import java.util.OptionalDouble;

/**
 * This is a Transpositions implementation that always assumes there is no
 * transposition for a given state.
 *
 * @param <S> the type of state
 */
public class NilTranspositions<S extends State<S, ?>> implements Transpositions<S> {

    @Override
    public OptionalDouble get(S state) {
        return OptionalDouble.empty();
    }

    @Override
    public void put(S state, double utility) { }
}
