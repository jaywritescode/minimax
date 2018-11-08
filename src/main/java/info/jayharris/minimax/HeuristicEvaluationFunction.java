package info.jayharris.minimax;

public interface HeuristicEvaluationFunction<S extends State<S, ?>> {

    double apply(S state);
}
