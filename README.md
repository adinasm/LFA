# Deterministic finite automaton

The dfa is read from the file and it is saved in a DFA object that contains
the transitions(and the reversed ones), the final states, the start state, as
well as the accesible, productive and useful states. For each argument type, a
different method from the DFA class is called.

### -e:
For every final state that is read, it is checked whether it is equal to the
start state. If such state exists, then the dfa accepts e.

### -a:
A DFS traversal of the graph is done from the start state and all the
encountered states are accesible states.

### -u:
DFS traversals on the transposed graph are performed from the final states and
all the encountered states that are also accesible are useful.

### -v:
If there are no useful states, no accesible final state or the start state is
not productive, then the dfa accepts an empty language.

### -f:
A DFS traversal from the start node is done and if a cycle that contains a
useful state is detected, then the dfa accepts an infinite language.