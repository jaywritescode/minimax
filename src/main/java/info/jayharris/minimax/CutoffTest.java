package info.jayharris.minimax;

public interface CutoffTest<S extends State<S, A>, A extends Action<S, A>> {

    boolean apply(Node2<S, A> node);
}
