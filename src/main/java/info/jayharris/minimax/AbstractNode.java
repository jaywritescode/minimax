package info.jayharris.minimax;

import com.google.common.base.Suppliers;

import java.util.Set;
import java.util.function.Supplier;

/**
 * A node encapsulates a state and action in the decision tree.
 *
 * @param <S> a State, a wrapper around a game state
 * @param <A> an Action, a wrapper around a game move
 */
public abstract class AbstractNode<S extends State<S, A>, A extends Action<S, A>> {

    final S state;
    private final A action;
    final int depth;

    final NodeFactory<S, A> nodeFactory;

    final EvaluationFunction<S> utility;
    final Supplier<Set<? extends AbstractNode<S, A>>> successors;
    private final Supplier<Double> value;

    AbstractNode(S state, A action, int depth, NodeFactory<S, A> nodeFactory) {
        this.state = state;
        this.action = action;
        this.depth = depth;
        this.nodeFactory = nodeFactory;

        utility = nodeFactory.getUtility();
        successors = Suppliers.memoize(this::getSuccessors);
        value = Suppliers.memoize(this::getValue);
    }

    /**
     * Gets the set of successor nodes that can be derived from applying each
     * legal move to the current state.
     *
     * @return a set of successor nodes
     */
    public Set<? extends AbstractNode<S, A>> successors() {
        return successors.get();
    }

    /**
     * Gets the utility value of this node's state.
     *
     * @return a utility value
     */
    public double value() {
        return value.get();
    }

    abstract Set<? extends AbstractNode<S, A>> getSuccessors();

    abstract double getValue();

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

    double utility() {
        return utility.apply(state);
    }
}
