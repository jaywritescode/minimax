package info.jayharris.minimax;

import java.util.Collection;
import java.util.OptionalLong;
import java.util.function.Function;
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

    private final NodeFactory<S, A> nodeFactory;

    private OptionalLong utility;

    Node(S state, A action) {
        this.state = state;
        this.action = action;
        this.nodeFactory = new NodeFactory<>(state);
        this.utility = OptionalLong.empty();

    }

    Collection<Node> successors() {
        return state.actions().stream()
                .map((Function<A, Node>) nodeFactory::withAction)
                .collect(Collectors.toList());
    }

    boolean terminalTest() {
        return state.terminalTest();
    }

    A getAction() {
        return action;
    }

    long getUtility() {
        return utility.getAsLong();
    }

    void setUtility() {
        utility = state.utility();
    }
}
