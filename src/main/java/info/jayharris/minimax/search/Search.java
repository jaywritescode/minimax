package info.jayharris.minimax.search;

import info.jayharris.minimax.Action;
import info.jayharris.minimax.State;

public interface Search<S extends State<S, A>, A extends Action<S, A>> {
    A perform();
}
