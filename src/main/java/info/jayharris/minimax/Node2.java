package info.jayharris.minimax;

import java.util.Comparator;
import java.util.Set;
import java.util.function.DoubleSupplier;
import java.util.stream.Collectors;

public class Node2<S extends State<S, A>, A extends Action<S, A>> {

    private final S state;

    private final A action;

    private final int depth;

    private DoubleSupplier heuristic;

    public static Comparator<Node2> comparator = Comparator.comparingDouble(Node2::getHeuristicValue);

    public Node2(S state, A action, int depth) {
        this.state = state;
        this.action = action;
        this.depth = depth;
        this.heuristic = state::eval;
    }

    Set<Node2<S, A>> successors() {
        return state.actions().stream().map(this::apply).collect(Collectors.toSet());
    }

    Node2<S, A> apply(A successorAction) {
        return new Node2<S, A>(successorAction.apply(state), successorAction, depth + 1);
    }

    S getState() {
        return state;
    }

    A getAction() {
        return action;
    }

    void memoizeHeuristicValue(double value) {
        this.heuristic = () -> value;
    }

    double getHeuristicValue() {
        return heuristic.getAsDouble();
    }

    boolean terminalTest() {
        return state.terminalTest();
    }
}
