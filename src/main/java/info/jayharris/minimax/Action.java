package info.jayharris.minimax;

public interface Action<S extends State<S, A>, A extends Action<S, A>> {

    public S apply(S initialState);
}
