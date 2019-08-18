package info.jayharris.minimax;

public interface EvaluationFunction<S extends State<S, ?>> {

    double apply(S state);
}
