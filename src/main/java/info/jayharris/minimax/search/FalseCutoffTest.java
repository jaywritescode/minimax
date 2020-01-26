package info.jayharris.minimax.search;

import info.jayharris.minimax.Node;

public class FalseCutoffTest extends CutoffTest {

    static FalseCutoffTest instance = null;

    private FalseCutoffTest() { }

    @Override
    public boolean cutoffSearch(Node node) {
        return false;
    }

    public static FalseCutoffTest getInstance() {
        if (instance == null) {
            instance = new FalseCutoffTest();
        }
        return instance;
    }
}
