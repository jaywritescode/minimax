package info.jayharris.minimax;

public class NodeFactory<S extends State<S, A>, A extends Action<S, A>> {

    S initialState;

    public NodeFactory(S initialState) {
        this.initialState = initialState;
    }

    public Node<S, A> withAction(A action) {
        return new Node<>(action.apply(initialState), action);
    }

    public static <S extends State<S, A>, A extends Action<S, A>> Node<S, A> rootNode(S root) {
        return new Node<>(root, null);
    }
}
