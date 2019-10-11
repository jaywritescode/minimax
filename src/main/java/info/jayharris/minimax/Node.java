package info.jayharris.minimax;

import com.google.common.base.Suppliers;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;

public class Node<S extends State<S, A>, A extends Action<S, A>> {

    private final S state;
    private final A action;
    private final int depth;

    // We want to generate this collection of successors lazily because
    // we may be able to bail on a branch of the search tree without expanding
    // all the successor nodes.
    private final Collection<Node<S, A>> successors;
    private final Supplier<Double> valueSupplier;

    private Node(S state, ToDoubleFunction<Node<S, A>> childrenFn) {
        this.state = state;
        this.action = null;
        this.depth = 0;

        this.successors = new HashSet<>();
        this.valueSupplier = Suppliers.memoize(() -> childrenFn.applyAsDouble(this));
    }

    private Node(Node<S, A> parent, A action, ToDoubleFunction<Node<S, A>> childrenFn) {
        this.state = action.perform(parent.state);
        this.action = action;
        this.depth = parent.depth + 1;

        this.successors = new HashSet<>();
        this.valueSupplier = Suppliers.memoize(() -> childrenFn.applyAsDouble(this));
    }

    public S getState() {
        return state;
    }

    public A getAction() {
        return action;
    }

    public Collection<Node<S, A>> getKnownSuccessors() {
        return successors;
    }

    public double getValue() {
        return valueSupplier.get();
    }

    public int getDepth() {
        return depth;
    }

    public static <S extends State<S, A>, A extends Action<S, A>> Node<S, A> createRootNode(
            S state, ToDoubleFunction<Node<S, A>> childrenFn) {
        return new Node<>(state, childrenFn);
    }

    public static <S extends State<S, A>, A extends Action<S, A>> Node<S, A> createSuccessor(
            Node<S, A> parent, A action, ToDoubleFunction<Node<S, A>> childrenFn) {
        Node<S, A> successor = new Node<>(parent, action, childrenFn);
        parent.successors.add(successor);
        return successor;
    }
}
