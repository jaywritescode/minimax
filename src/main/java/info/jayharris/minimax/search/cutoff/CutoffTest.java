package info.jayharris.minimax.search.cutoff;

import info.jayharris.minimax.Action;
import info.jayharris.minimax.search.Node;
import info.jayharris.minimax.State;

/**
 * A predicate that is applied to a node in a search tree, and that returns
 * true iff the search should be terminated at that node.
 *
 * @param <S> a game state
 * @param <A> an action taken in the game
 */
public abstract class CutoffTest<S extends State<S, A>, A extends Action<S, A>> {

    public abstract boolean cutoffSearch(Node<S, A> node);
}
