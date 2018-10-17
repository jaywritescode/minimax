package info.jayharris.minimax;

import java.util.Comparator;
import java.util.Set;
import java.util.function.DoubleSupplier;
import java.util.stream.Collectors;

class Node<S extends State<S, A>, A extends Action<S, A>> {

    private final S state;

    private final A action;

    private final int depth;

    private DoubleSupplier valueSupplier;

    static final Comparator<Node> comparator = Comparator.comparingDouble(Node::getHeuristicValue);

    Node(S state, A action, int depth) {
        this.state = state;
        this.action = action;
        this.depth = depth;
        this.valueSupplier = () -> state.eval();
    }

    Set<Node<S, A>> successors() {
        return state.actions().stream().map(this::apply).collect(Collectors.toSet());
    }

    private Node<S, A> apply(A successorAction) {
        return new Node<>(successorAction.apply(state), successorAction, depth + 1);
    }

    S getState() {
        return state;
    }

    A getAction() {
        return action;
    }

    void calculateHeuristicValue() {
        setHeuristicValueToConstant(state.eval());
    }

    double getHeuristicValue() {
        return valueSupplier.getAsDouble();
    }

    void setHeuristicValueToConstant(double value) {
        this.valueSupplier = () -> value;
    }

    boolean terminalTest() {
        return state.terminalTest();
    }
}
