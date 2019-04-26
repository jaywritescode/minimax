package info.jayharris.minimax;

import info.jayharris.minimax.transposition.NilTranspositions;
import info.jayharris.minimax.transposition.Transpositions;

public class DecisionTreeFactory<S extends State<S, A>, A extends Action<S, A>> {

    private Transpositions<S> transpositions;

    private HeuristicEvaluationFunction<S> heuristic;

    private CutoffTest<S> cutoffTest;

    public DecisionTreeFactory(HeuristicEvaluationFunction<S> heuristic, CutoffTest<S> cutoffTest) {
        this(new NilTranspositions<>(), heuristic, cutoffTest);
    }

    public DecisionTreeFactory(Transpositions<S> transpositions, HeuristicEvaluationFunction<S> heuristic, CutoffTest<S> cutoffTest) {
        this.transpositions = transpositions;
        this.heuristic = heuristic;
        this.cutoffTest = cutoffTest;
    }

    public DecisionTree<S, A> build(S root) {
        return new DecisionTree<>(root, transpositions, heuristic, cutoffTest);
    }
}
