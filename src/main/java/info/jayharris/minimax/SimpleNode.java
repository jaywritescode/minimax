package info.jayharris.minimax;

import com.google.common.base.Suppliers;

import java.util.Set;
import java.util.function.Supplier;

public abstract class SimpleNode<S extends State<S, A>, A extends Action<S, A>> {

    final S state;
    private final A action;
    final int depth;
    final EvaluationFunction<S> utility;

    final Supplier<Set<? extends SimpleNode<S, A>>> successors;
    private final Supplier<Double> value;

    SimpleNode(S state, A action, int depth, EvaluationFunction<S> utility) {
        this.state = state;
        this.action = action;
        this.depth = depth;
        this.utility = utility;

        successors = Suppliers.memoize(this::getSuccessors);
        value = Suppliers.memoize(this::getValue);
    }

    public Set<? extends SimpleNode<S, A>> successors() {
        return successors.get();
    }

    public double value() {
        return value.get();
    }

    abstract Set<? extends SimpleNode<S, A>> getSuccessors();

    abstract double getValue();

    public S getState() {
        return state;
    }

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
