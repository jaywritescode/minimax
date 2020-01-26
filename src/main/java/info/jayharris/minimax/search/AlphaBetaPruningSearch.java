package info.jayharris.minimax.search;

import info.jayharris.minimax.*;

import java.util.OptionalDouble;
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
    TranspositionTable<S, A> transpositionTable;

    public AlphaBetaPruningSearch(ToDoubleFunction<S> heuristic) {
        this(FalseCutoffTest.getInstance(), heuristic);
    }

    public AlphaBetaPruningSearch(CutoffTest<S, A> cutoffTest, ToDoubleFunction<S> heuristic) {
        this(cutoffTest, heuristic, NilTranspositionTable.getInstance());
    }

    public AlphaBetaPruningSearch(CutoffTest<S, A> cutoffTest, ToDoubleFunction<S> heuristic,
                                  TranspositionTable<S, A> transpositionTable) {
        this.cutoffTest = cutoffTest;
        this.heuristic = heuristic;
        this.transpositionTable = transpositionTable;
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

            OptionalDouble t;
            if ((t = transpositionTable.getUtilityValue(state)).isPresent()) {
                return t.getAsDouble();
            }

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
                    transpositionTable.setUtilityValue(state, v);
                    return v;
                }
                alpha = Math.max(alpha, v);
            }
            transpositionTable.setUtilityValue(state, v);
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

            OptionalDouble t;
            if ((t = transpositionTable.getUtilityValue(state)).isPresent()) {
                return t.getAsDouble();
            }

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
                    transpositionTable.setUtilityValue(state, v);
                    return v;
                }
                beta = Math.min(beta, v);
            }
            transpositionTable.setUtilityValue(state, v);
            return v;
        }
    }
}
