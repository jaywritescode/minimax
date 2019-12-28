package info.jayharris.minimax.search;

import info.jayharris.minimax.Action;
import info.jayharris.minimax.Node;
import info.jayharris.minimax.State;

import java.util.function.Predicate;

/**
 * A predicate that is applied to a node in a search tree, and that returns
 * true iff the search should be terminated at that node.
 *
 * @param <S> a game state
 * @param <A> an action taken in the game
 */
public abstract class CutoffTest<S extends State<S, A>, A extends Action<S, A>> implements Predicate<Node<S, A>> {

    public boolean cutoffSearch(Node<S, A> node) {
        return test(node);
    }
}
