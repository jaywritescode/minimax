package info.jayharris.minimax.transposition;

import info.jayharris.minimax.Action;
import info.jayharris.minimax.State;

import java.util.HashMap;
import java.util.Map;
import java.util.OptionalDouble;

public class InMemoryMapTranspositions<S extends State<S, A>, A extends Action<S, A>> implements Transpositions<S, A> {

    Map<S, Double> map = new HashMap<>();

    @Override
    public OptionalDouble get(S state) {
        return map.containsKey(state) ? OptionalDouble.of(map.get(state)) : OptionalDouble.empty();
    }

    @Override
    public void put(S state, double utility) {
        map.put(state, utility);
    }
}
