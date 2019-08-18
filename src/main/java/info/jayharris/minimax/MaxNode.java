package info.jayharris.minimax;

import java.util.Set;
import java.util.stream.Collectors;

public class MaxNode<S extends State<S, A>, A extends Action<S, A>> extends SimpleNode<S, A> {

    public MaxNode(S state, A action, int depth, EvaluationFunction<S> utility) {
        super(state, action, depth, utility);
    }

    double getValue() {
        if (state.terminalTest()) {
            return utility();
        }

        double v = Double.NEGATIVE_INFINITY;
        for (SimpleNode<S, A> succ : successors.get()) {
            v = Double.max(v, succ.getValue());
        }
        return v;
    }

    public Set<MinNode<S, A>> getSuccessors() {
        return state.actions().stream().map(this::successorNode).collect(Collectors.toSet());
    }

    private MinNode<S, A> successorNode(A action) {
        return new MinNode<>(action.apply(state), action, depth + 1, utility);
    }
}
