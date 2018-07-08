package info.jayharris.minimax.transposition;

import info.jayharris.minimax.Action;
import info.jayharris.minimax.State;

import java.util.HashMap;
import java.util.Map;
import java.util.OptionalLong;

public class HashMapTranspositionTable<S extends State<S, A>, A extends Action<S, A>> extends BaseTranspositionTable<S, A> {

    private Map<S, Long> utilities;

    public HashMapTranspositionTable() {
        utilities = new HashMap<>();
    }

    @Override
    public boolean contains(S state) {
        return utilities.containsKey(state);
    }

    @Override
    public OptionalLong get(S state) {
        return contains(state) ? OptionalLong.of(utilities.get(state)) : OptionalLong.empty();
    }

    @Override
    public void put(S state, long utility) {
        utilities.put(state, utility);
    }

    @Override
    public long size() {
        return utilities.size();
    }
}
