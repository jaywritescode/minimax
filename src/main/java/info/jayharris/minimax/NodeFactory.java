package info.jayharris.minimax;

import java.util.Set;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

/**
 * The NodeFactory is responsible for creating nodes in the search tree.
 * <p>
 * It injects the functions that node needs to memoize its utility or heuristic
 * value and its successors without exposing those actual calculations to the node.
 *
 * @param <S> the game state
 * @param <A> an action, representing a (legal) state transition in the game
 */
public class NodeFactory<S extends State<S, A>, A extends Action<S, A>> {

    ToDoubleFunction<Node<S, A>> maxValueFunction;
    ToDoubleFunction<Node<S, A>> minValueFunction;

    public NodeFactory(Builder<S, A> builder) {
        this.maxValueFunction = builder.maxValueFunction;
        this.minValueFunction = builder.minValueFunction;
    }

    public Node<S, A> createRootNode(S initialState) {
        return createMaxNode(initialState, null, 0);
    }

    public Set<Node<S, A>> successors(Node<S, A> node) {
        return node.getState().actions().stream()
                .map(action -> applyActionForSuccessor(node, action))
                .collect(Collectors.toSet());
    }

    private Node<S, A> applyActionForSuccessor(Node<S, A> initial, A action) {
        if (initial.getNodeType() == NodeType.MAX) {
            return createMinNode(action.apply(initial.getState()), action, initial.getDepth() + 1);
        }
        return createMaxNode(action.apply(initial.getState()), action, initial.getDepth() + 1);
    }

    private Node<S, A> createMaxNode(S state, A action, int depth) {
        return create(state, action, depth, NodeType.MAX);
    }

    private Node<S, A> createMinNode(S state, A action, int depth) {
        return create(state, action, depth, NodeType.MIN);
    }

    private Node<S, A> create(S state, A action, int depth, NodeType nodeType) {
        return new Node<>(state, action, depth, nodeType, this::successors,
                          nodeType == NodeType.MAX ? maxValueFunction : minValueFunction);
    }

    public static <S extends State<S, A>, A extends Action<S, A>> Builder<S, A> builder() {
        return new Builder<>();
    }

    public static class Builder<S extends State<S, A>, A extends Action<S, A>> {

        ToDoubleFunction<Node<S, A>> maxValueFunction;
        ToDoubleFunction<Node<S, A>> minValueFunction;

        public Builder<S, A> setMaxValueFunction(ToDoubleFunction<Node<S, A>> maxValueFunction) {
            this.maxValueFunction = maxValueFunction;
            return this;
        }

        public Builder<S, A> setMinValueFunction(ToDoubleFunction<Node<S, A>> minValueFunction) {
            this.minValueFunction = minValueFunction;
            return this;
        }

        public NodeFactory<S, A> build() {
            return new NodeFactory<>(this);
        }
    }
}
