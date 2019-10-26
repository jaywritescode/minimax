package info.jayharris.minimax;

import java.util.function.Predicate;

public abstract class CutoffTest<S extends State<S, A>, A extends Action<S, A>> implements Predicate<Node<S, A>> {

    public boolean cutoffSearch(Node<S, A> node) {
        return test(node);
    }
}
