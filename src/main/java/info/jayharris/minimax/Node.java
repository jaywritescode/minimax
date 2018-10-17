package info.jayharris.minimax;

import java.util.Comparator;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.stream.Collectors;

class Node<S extends State<S, A>, A extends Action<S, A>> {

    private final S state;

    private final A action;

    private final int depth;

    private OptionalDouble value = OptionalDouble.empty();

    static Comparator<Node> comparator = Comparator.comparingDouble(Node::getValue);

    Node(S state, A action, int depth) {
        this.state = state;
        this.action = action;
        this.depth = depth;
    }

    Set<Node<S, A>> successors() {
        return state.actions().stream().map(this::apply).collect(Collectors.toSet());
    }

    Node<S, A> apply(A successorAction) {
        return new Node<>(successorAction.apply(state), successorAction, depth + 1);
    }

    S getState() {
        return state;
    }

    A getAction() {
        return action;
    }

    void calculateHeuristicValue() {
        setValue(state.eval());
    }

    double getValue() {
        return value.getAsDouble();
    }

    void setValue(double value) {
        this.value = OptionalDouble.of(value);
    }

    boolean terminalTest() {
        return state.terminalTest();
    }
}
