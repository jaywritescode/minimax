package info.jayharris.minimax.search;

import info.jayharris.minimax.*;
import info.jayharris.minimax.search.cutoff.CutoffTest;
import info.jayharris.minimax.search.cutoff.FalseCutoffTest;

import java.util.Comparator;
import java.util.OptionalDouble;
import java.util.function.ToDoubleFunction;

/**
 * Minimax search algorithm implementation.
 *
 * @param <S> the type of State
 * @param <A> the type of Action
 */
public abstract class MinimaxDecision<S extends State<S, A>, A extends Action<S, A>> implements Search<S, A> {

    private final ToDoubleFunction<S> heuristicFn;
    private final CutoffTest<S, A> cutoffTest;
    private final TranspositionTable<S, A> transpositionTable;

    private int nodesExamined = 0;

    public MinimaxDecision(ToDoubleFunction<S> heuristicFn) {
        this(heuristicFn, FalseCutoffTest.getInstance());
    }

    public MinimaxDecision(ToDoubleFunction<S> heuristicFn, CutoffTest<S, A> cutoffTest) {
        this(heuristicFn, cutoffTest, NilTranspositionTable.getInstance());
    }

    public MinimaxDecision(ToDoubleFunction<S> heuristicFn, CutoffTest<S, A> cutoffTest,
                           TranspositionTable<S, A> transpositionTable) {
        this.cutoffTest = cutoffTest;
        this.heuristicFn = heuristicFn;
        this.transpositionTable = transpositionTable;
    }

    @Override
    public A perform(S initialState) {
        nodesExamined = 0;
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

        ++nodesExamined;

        OptionalDouble t;
        if ((t = transpositionTable.getUtilityValue(state)).isPresent()) {
            return t.getAsDouble();
        }

        double value;
        if (state.terminalTest()) {
            value = utility(state);
        }
        else if (cutoffTest.cutoffSearch(node)) {
            value = heuristicFn.applyAsDouble(state);
        }
        else {
            value = state.actions().stream()
                    .map(action -> Node.createSuccessor(node, action, this::minValue))
                    .max(Comparator.comparingDouble(Node::getValue))
                    .map(Node::getValue)
                    .get();
        }

        transpositionTable.setUtilityValue(state, value);
        return value;
    }

    private double minValue(Node<S, A> node) {
        S state = node.getState();

        ++nodesExamined;

        OptionalDouble t;
        if ((t = transpositionTable.getUtilityValue(state)).isPresent()) {
            return t.getAsDouble();
        }

        double value;
        if (state.terminalTest()) {
            value = utility(state);
        }
        else if (cutoffTest.cutoffSearch(node)) {
            value = heuristicFn.applyAsDouble(state);
        }
        else {
            value = state.actions().stream()
                    .map(action -> Node.createSuccessor(node, action, this::maxValue))
                    .min(Comparator.comparingDouble(Node::getValue))
                    .map(Node::getValue)
                    .get();
        }

        transpositionTable.setUtilityValue(state, value);
        return value;
    }
}
