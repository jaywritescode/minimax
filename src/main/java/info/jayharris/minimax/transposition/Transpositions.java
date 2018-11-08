package info.jayharris.minimax.transposition;

import info.jayharris.minimax.State;

import java.util.OptionalDouble;

public interface Transpositions<S extends State<S, ?>> {

    OptionalDouble get(S state);

    void put(S state, double utility);
}
