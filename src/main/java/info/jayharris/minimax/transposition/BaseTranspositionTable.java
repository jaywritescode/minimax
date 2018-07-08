package info.jayharris.minimax.transposition;

import info.jayharris.minimax.Action;
import info.jayharris.minimax.State;

import java.util.OptionalLong;

public abstract class BaseTranspositionTable<S extends State<S, A>, A extends Action<S, A>> {

    public abstract boolean contains(S state);

    public abstract OptionalLong get(S state);

    public abstract void put(S state, long utility);

    public abstract long size();
}
