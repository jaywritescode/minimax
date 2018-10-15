package info.jayharris.minimax;

import java.util.Comparator;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

class Node<S extends State<S, A>, A extends Action<S, A>> {

    /**
     * A state in the game.
     */
    private final S state;

    /**
     * The action such that {@code action.apply(state.predecessor()).equals(state)}.
     */
    private final A action;

    private final int depth;

    private final NodeFactory<S, A> nodeFactory;

    private OptionalDouble utility;

    static Comparator<Node> comparator = Comparator.comparingLong(Node::getUtility);

    Node(S state, A action, int depth) {
        this.state = state;
        this.action = action;
        this.depth = depth;
        this.nodeFactory = new NodeFactory<>(state);
        this.utility = OptionalDouble.empty();
    }

    List<Node<S, A>> successors() {
        return state.actions().stream()
                .map(action -> nodeFactory.withAction(action, depth + 1))
                .collect(Collectors.toList());
    }

    boolean terminalTest() {
        return state.terminalTest();
    }

    S getState() {
        return state;
    }

    A getAction() {
        return action;
    }

    int getDepth() {
        return depth;
    }

    double getUtility() {
        return utility.getAsDouble();
    }

    void setUtility(double utility) {
        setUtility(OptionalDouble.of(utility));
    }

    void setUtility(OptionalDouble utility) {
        this.utility = utility;
    }
}
