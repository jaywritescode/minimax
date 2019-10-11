package info.jayharris.minimax.search;

import info.jayharris.minimax.*;

import java.util.function.ToDoubleFunction;

/**
 * Minimax search implementation with alpha-beta pruning.
 *
 * @param <S> the type of State
 * @param <A> the type of Action
 */
public abstract class AlphaBetaPruningSearch<S extends State<S, A>, A extends Action<S, A>> implements Search<S, A> {

    CutoffTest<S, A> cutoffTest;
    ToDoubleFunction<S> heuristic;

    AlphaBetaPruningSearch(ToDoubleFunction<S> heuristic) {
        this(FalseCutoffTest.getInstance(), heuristic);
    }

    AlphaBetaPruningSearch(CutoffTest<S, A> cutoffTest, ToDoubleFunction<S> heuristic) {
        this.cutoffTest = cutoffTest;
        this.heuristic = heuristic;
    }

    @Override
    public A perform(S initialState) {
        return perform(Node.createRootNode(initialState, new MaxValueFunction()));
    }

    private A perform(Node<S, A> root) {
        double v = root.getValue();

        return root.getKnownSuccessors().stream()
                .filter(node -> node.getValue() == v)
                .findFirst()
                .map(Node::getAction)
                .orElseThrow(RuntimeException::new);
    }

    private class MaxValueFunction implements ToDoubleFunction<Node<S, A>> {
        double alpha;
        double beta;

        MaxValueFunction() {
            this(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        }

        MaxValueFunction(double alpha, double beta) {
            this.alpha = alpha;
            this.beta = beta;
        }

        @Override
        public double applyAsDouble(Node<S, A> node) {
            S state = node.getState();

            if (state.terminalTest()) {
                return utility(state);
            }

            if (cutoffTest.cutoffSearch(node)) {
                return heuristic.applyAsDouble(state);
            }

            double v = Double.NEGATIVE_INFINITY;
            for (A action : state.actions()) {
                Node<S, A> successor = Node.createSuccessor(node, action, new MinValueFunction(alpha, beta));
                v = Math.max(v, successor.getValue());

                if (v >= beta) {
                    return v;
                }
                alpha = Math.max(alpha, v);
            }
            return v;
        }
    }

    private class MinValueFunction implements ToDoubleFunction<Node<S, A>> {
        double alpha;
        double beta;

        MinValueFunction(double alpha, double beta) {
            this.alpha = alpha;
            this.beta = beta;
        }

        public double applyAsDouble(Node<S, A> node) {
            S state = node.getState();

            if (state.terminalTest()) {
                return utility(state);
            }

            if (cutoffTest.cutoffSearch(node)) {
                return heuristic.applyAsDouble(state);
            }

            double v = Double.POSITIVE_INFINITY;
            for (A action : state.actions()) {
                Node<S, A> successor = Node.createSuccessor(node, action, new MaxValueFunction(alpha, beta));
                v = Math.min(v, successor.getValue());

                if (v <= alpha) {
                    return v;
                }
                beta = Math.min(beta, v);
            }
            return v;
        }
    }
}
