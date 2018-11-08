package info.jayharris.minimax;

import java.util.Comparator;
import java.util.OptionalDouble;
import java.util.StringJoiner;
import java.util.stream.Stream;

class Node<S extends State<S, A>, A extends Action<S, A>> {

    private final S state;

    private final A action;

    private final int depth;

    private OptionalDouble heuristicValue;

    static final Comparator<Node> comparator = Comparator.comparingDouble(Node::getHeuristicValue);

    protected Node(S state, A action, int depth) {
        this.state = state;
        this.action = action;
        this.depth = depth;

        heuristicValue = OptionalDouble.empty();
    }

    public Stream<Node<S, A>> successors() {
        return state.actions().stream().map(this::apply);
    }

    private Node<S, A> apply(A successorAction) {
        return new Node<>(successorAction.apply(state), successorAction, depth + 1);
    }

    public S getState() {
        return state;
    }

    public A getAction() {
        return action;
    }

    public int getDepth() {
        return depth;
    }

    double getHeuristicValue() {
        // TODO: probably should not be a runtime exception, because this would indicate an actual bug
        return heuristicValue.orElseThrow(RuntimeException::new);
    }

    void setHeuristicValue(double value) {
        heuristicValue = OptionalDouble.of(value);
    }

    void calculateHeuristicValue(HeuristicEvaluationFunction<S> heuristic) {
        heuristicValue = OptionalDouble.of(heuristic.apply(state));
    }

    boolean terminalTest() {
        return state.terminalTest();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Node.class.getSimpleName() + "[", "]")
                .add("state=" + state)
                .add("action=" + action)
                .add("heuristicValue=" + heuristicValue)
                .toString();
    }

    public static <S extends State<S, A>, A extends Action<S, A>> Node<S, A> root(S state) {
        return new Node<>(state, null, 0);
    }
}
