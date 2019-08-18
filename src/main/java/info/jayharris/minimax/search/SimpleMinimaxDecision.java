package info.jayharris.minimax.search;

import info.jayharris.minimax.Action;
import info.jayharris.minimax.MaxNode;
import info.jayharris.minimax.SimpleNode;
import info.jayharris.minimax.State;

/**
 * This is the minimax-decision algorithm described in the book. It doesn't have a
 * transposition table or cutoff test.
 */
public class SimpleMinimaxDecision<S extends State<S, A>, A extends Action<S, A>> implements Search<S, A> {

    private final MaxNode<S, A> root;

    public SimpleMinimaxDecision(MaxNode<S, A> root) {
        this.root = root;
    }

    public A perform() {
        double v = root.value();

        return root.successors().stream()
                .filter(node -> node.value() == v)
                .findFirst()
                .map(SimpleNode::getAction)
                .orElseThrow(RuntimeException::new);
    }
}
