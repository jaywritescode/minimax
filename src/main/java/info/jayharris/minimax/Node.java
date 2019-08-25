package info.jayharris.minimax;

import com.google.common.base.Suppliers;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;

/**
 * A node encapsulates a state and action in the decision tree.
 *
 * @param <S> a State, a wrapper around a game state
 * @param <A> an Action, a wrapper around a game move
 */
public class Node<S extends State<S, A>, A extends Action<S, A>> {

    private final S state;
    private final A action;
    private final int depth;
    private final NodeType nodeType;

    private final Supplier<Set<Node<S, A>>> successorsSupplier;
    private final Supplier<Double> valueSupplier;

    Node(S state,
         A action,
         int depth,
         NodeType nodeType,
         Function<Node<S, A>, Set<Node<S, A>>> successorsFunction,
         ToDoubleFunction<Node<S, A>> valueFunction) {
        this.state = state;
        this.action = action;
        this.depth = depth;
        this.nodeType = nodeType;

        successorsSupplier = Suppliers.memoize(() -> successorsFunction.apply(this));
        valueSupplier = Suppliers.memoize(() -> valueFunction.applyAsDouble(this));
    }

    /**
     * Gets the set of successor nodes that can be derived from applying each
     * legal move to the current state.
     *
     * @return a set of successor nodes
     */
    public Set<Node<S, A>> getSuccessors() {
        return successorsSupplier.get();
    }

    /**
     * Gets the utility or estimated utility of this state.
     *
     * @return a utility value
     */
    public double getValue() {
        return valueSupplier.get();
    }

    /**
     * Gets the state wrapped by this node.
     *
     * @return a state
     */
    public S getState() {
        return state;
    }

    /**
     * Gets the action wrapped by this node.
     *
     * @return an action
     */
    public A getAction() {
        return action;
    }

    public int getDepth() {
        return depth;
    }

    public NodeType getNodeType() {
        return nodeType;
    }
}
