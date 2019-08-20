package info.jayharris.minimax.search;

import info.jayharris.minimax.*;

import java.util.function.Predicate;

/**
 * A minimax-search algorithm.
 *
 * Consumers are expected to implement the #utility method in a sub-class. The utility of
 * a game state is a function of the game and not the minimax search, so that's why the
 * implementation is the game's responsibility.
 *
 * @param <S> the game state
 * @param <A> an action, representing a (legal) state transition in the game
 */
public abstract class MinimaxDecision<S extends State<S, A>, A extends Action<S, A>> implements Search<S, A> {

    private final NodeFactory<S, A> nodeFactory;

    /**
     * Constructor.
     *
     * @param cutoffTest a predicate that returns true if the search algorithm
     *                   should stop expanding nodes in the search tree and use
     *                   a calculated heuristic value
     * @param heuristic the heuristic function that estimates the quality of a game
     *                  state with respect to the player using this search
     */
    protected MinimaxDecision(Predicate<AbstractNode<S, A>> cutoffTest, EvaluationFunction<S> heuristic) {
        this.nodeFactory = new NodeFactory<>(this::utility, cutoffTest, heuristic);
    }

    /**
     * Performs the search.
     *
     * @param initialState the current game state
     * @return an action
     */
    public A perform(S initialState) {
        return perform(nodeFactory.createMaxNode(initialState, null, 0));
    }

    private A perform(AbstractNode<S, A> root) {
        double v = root.value();

        return root.successors().stream()
                .filter(node -> node.value() == v)
                .findFirst()
                .map(AbstractNode::getAction)
                .orElseThrow(RuntimeException::new);
    }
}
