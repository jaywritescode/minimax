package info.jayharris.minimax;

public class NodeFactory<S extends State<S, A>, A extends Action<S, A>> {

    private final EvaluationFunction<S> utility;

    public NodeFactory(EvaluationFunction<S> utility) {
        this.utility = utility;
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
