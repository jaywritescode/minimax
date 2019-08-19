package info.jayharris.minimax;

import java.util.Set;
import java.util.stream.Collectors;

public class MinNode<S extends State<S, A>, A extends Action<S, A>> extends AbstractNode<S, A> {

    MinNode(S state, A action, int depth, EvaluationFunction<S> utility) {
        super(state, action, depth, utility);
    }

    double getValue() {
        if (state.terminalTest()) {
            return utility();
        }

        double v = Double.POSITIVE_INFINITY;
        for (AbstractNode<S, A> succ : successors.get()) {
            v = Double.min(v, succ.getValue());
        }
        return v;
    }

    public Set<MaxNode<S, A>> getSuccessors() {
        return state.actions().stream().map(this::successorNode).collect(Collectors.toSet());
    }

    private MaxNode<S, A> successorNode(A action) {
        return new MaxNode<>(action.apply(state), action, depth + 1, utility);
    }
}
