package info.jayharris.minimax.search;

import info.jayharris.minimax.*;

import java.util.Comparator;
import java.util.function.ToDoubleFunction;

/**
 * Minimax search algorithm implementation.
 *
 * @param <S> the type of State
 * @param <A> the type of Action
 */
public abstract class MinimaxDecision<S extends State<S, A>, A extends Action<S, A>> implements Search<S, A> {

    CutoffTest<S, A> cutoffTest;
    ToDoubleFunction<S> heuristicFn;

    public MinimaxDecision(ToDoubleFunction<S> heuristicFn) {
        this(FalseCutoffTest.getInstance(), heuristicFn);
    }

    public MinimaxDecision(CutoffTest<S, A> cutoffTest, ToDoubleFunction<S> heuristicFn) {
        this.cutoffTest = cutoffTest;
        this.heuristicFn = heuristicFn;
    }

    @Override
    public A perform(S initialState) {
        return perform(Node.createRootNode(initialState, this::maxValue));
    }

    private A perform(Node<S, A> root) {
        double v = root.getValue();

        return root.getKnownSuccessors().stream()
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

        if (cutoffTest.cutoffSearch(node)) {
            return heuristicFn.applyAsDouble(state);
        }

        return state.actions().stream()
                .map(action -> Node.createSuccessor(node, action, this::minValue))
                .max(Comparator.comparingDouble(Node::getValue))
                .map(Node::getValue)
                .get();
    }

    private double minValue(Node<S, A> node) {
        S state = node.getState();

        if (state.terminalTest()) {
            return utility(state);
        }

        if (cutoffTest.cutoffSearch(node)) {
            return heuristicFn.applyAsDouble(state);
        }

        return state.actions().stream()
                .map(action -> Node.createSuccessor(node, action, this::maxValue))
                .min(Comparator.comparingDouble(Node::getValue))
                .map(Node::getValue)
                .get();
    }
}
