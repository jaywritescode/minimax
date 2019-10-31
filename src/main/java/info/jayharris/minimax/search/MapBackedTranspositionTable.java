package info.jayharris.minimax.search;

import info.jayharris.minimax.Action;
import info.jayharris.minimax.State;

import java.util.HashMap;
import java.util.Map;
import java.util.OptionalDouble;

public class MapBackedTranspositionTable<S extends State<S, A>, A extends Action<S, A>> implements TranspositionTable<S, A> {

    Map<S, OptionalDouble> map;

    private MapBackedTranspositionTable() {
        map = new HashMap<>();
    }

    @Override
    public void setUtilityValue(S state, double value) {
        map.put(state, OptionalDouble.of(value));
    }

    @Override
    public OptionalDouble getUtilityValue(S state) {
        return map.getOrDefault(state, OptionalDouble.empty());
    }

    public static <S extends State<S, A>, A extends Action<S, A>> MapBackedTranspositionTable<S, A> create() {
        return new MapBackedTranspositionTable<>();
    }
}
