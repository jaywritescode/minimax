package info.jayharris.minimax;

public interface CutoffTest<S extends State<S, ?>> {

    boolean apply(Node<S, ?> node);
}
