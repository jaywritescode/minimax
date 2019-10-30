package info.jayharris.minimax.search;

import info.jayharris.minimax.Action;
import info.jayharris.minimax.Node;
import info.jayharris.minimax.State;

import java.util.function.Predicate;

public abstract class CutoffTest<S extends State<S, A>, A extends Action<S, A>> implements Predicate<Node<S, A>> {

    public boolean cutoffSearch(Node<S, A> node) {
        return test(node);
    }
}
