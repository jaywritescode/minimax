package info.jayharris.minimax.transposition;

import info.jayharris.minimax.Action;
import info.jayharris.minimax.State;

import java.util.OptionalDouble;

public interface Transpositions<S extends State<S, A>, A extends Action<S, A>> {

    OptionalDouble get(S state);

    void put(S state, double utility);
}
