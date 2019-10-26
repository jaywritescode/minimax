package info.jayharris.minimax;

public class FalseCutoffTest extends CutoffTest {

    static FalseCutoffTest instance = null;

    private FalseCutoffTest() { }

    @Override
    public boolean test(Object o) {
        return false;
    }

    public static FalseCutoffTest getInstance() {
        if (instance == null) {
            instance = new FalseCutoffTest();
        }
        return instance;
    }
}
