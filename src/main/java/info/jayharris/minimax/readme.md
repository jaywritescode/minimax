The root of the game space search library is the `Search` interface, which represents a search algorithm.

The search algorithms, such as `MinimaxDecision`, are abstract classes with concrete implementations of the `#perform` method. The `#utility` method is expected to be implemented in a concrete class in the game's package because the utility function is a property of game, regardless of which search algorithm is used.

The `#perform` takes a `State` and wraps it in a `Node`. This is where things get a little complicated. We need to be able to query the `Node` for its successors because the `minValue` and `maxValue` methods return the best utility value over all the paths searched but they don't return which `Action` leads to that value. For the same reason, we also need to be able to query the calculated utility value of a `Node`.

The utility value of a non-terminal node is the result of applying the node to a composition of `minValue` and `maxValue` methods. However, the node doesn't know about the `minValue` and `maxValue` methods because they're an implementation detail of the search algorithm. The `NodeFactory` class exists to provide those details to the node but also to encapsulate those details behind functions and suppliers. The node is querying something for its utility value calculation, but it doesn't know what.

