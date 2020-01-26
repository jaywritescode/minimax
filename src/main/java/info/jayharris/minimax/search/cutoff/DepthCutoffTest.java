package info.jayharris.minimax.search.cutoff;

import info.jayharris.minimax.Action;
import info.jayharris.minimax.search.Node;
import info.jayharris.minimax.State;

public class DepthCutoffTest<S extends State<S, A>, A extends Action<S, A>> extends CutoffTest<S, A> {

    private final int MAX_DEPTH;

    public DepthCutoffTest(int maxDepth) {
        MAX_DEPTH = maxDepth;
    }

    @Override
    public boolean cutoffSearch(Node<S, A> node) {
        return node.getDepth() > MAX_DEPTH;
    }
}
