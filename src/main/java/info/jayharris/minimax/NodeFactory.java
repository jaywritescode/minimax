package info.jayharris.minimax;

import java.util.function.Predicate;

public class NodeFactory<S extends State<S, A>, A extends Action<S, A>> {

    private final EvaluationFunction<S> utility;
    private final Predicate<AbstractNode<S, A>> cutoffTest;
    private final EvaluationFunction<S> heuristic;

    public NodeFactory(EvaluationFunction<S> utility,
                       Predicate<AbstractNode<S, A>> cutoffTest,
                       EvaluationFunction<S> heuristic) {
        this.utility = utility;
        this.cutoffTest = cutoffTest;
        this.heuristic = heuristic;
    }

    public MaxNode<S, A> createMaxNode(S state, A action, int depth) {
        return new MaxNode<>(state, action, depth, this);
    }

    public MinNode<S, A> createMinNode(S state, A action, int depth) {
        return new MinNode<>(state, action, depth, this);
    }

    public EvaluationFunction<S> getUtility() {
        return utility;
    }
}
