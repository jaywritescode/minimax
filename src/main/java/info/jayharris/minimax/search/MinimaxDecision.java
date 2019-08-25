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

    Predicate<Node<S, A>> cutoffTest;
    EvaluationFunction<S> heuristic;

    NodeFactory<S, A> nodeFactory;

    /**
     * Constructor.
     *
     * @param cutoffTest a predicate that returns true if the search algorithm
     *                   should stop expanding nodes in the search tree and use
     *                   a calculated heuristic value
     * @param heuristic the heuristic function that estimates the quality of a game
     *                  state with respect to the player using this search
     */
    protected MinimaxDecision(Predicate<Node<S, A>> cutoffTest, EvaluationFunction<S> heuristic) {
        this.cutoffTest = cutoffTest;
        this.heuristic = heuristic;

        this.nodeFactory = NodeFactory.<S, A>builder()
                .setMaxValueFunction(this::maxValue)
                .setMinValueFunction(this::minValue)
                .build();
    }

    /**
     * Performs the search.
     *
     * @param initialState the current game state
     * @return an action
     */
    public A perform(S initialState) {
        return perform(nodeFactory.createRootNode(initialState));
    }

    private A perform(Node<S, A> initialNode) {
        double v = initialNode.getValue();

        return initialNode.getSuccessors().stream()
                .filter(node -> node.getValue() == v)
                .findFirst()
                .map(Node::getAction)
                .orElseThrow(RuntimeException::new);
    }

    private double maxValue(Node<S, A> node) {
        S state = node.getState();

        if (state.terminalTest()) {
            return utility(state);
        }

        double v = Double.NEGATIVE_INFINITY;
        for (Node<S, A> succ : node.getSuccessors()) {
            v = Double.max(v, succ.getValue());
        }
        return v;
    }

    private double minValue(Node<S, A> node) {
        S state = node.getState();

        if (state.terminalTest()) {
            return utility(state);
        }

        double v = Double.POSITIVE_INFINITY;
        for (Node<S, A> succ : node.getSuccessors()) {
            v = Double.min(v, succ.getValue());
        }
        return v;
    }
}
