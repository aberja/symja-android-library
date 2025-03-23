## FindGraphIsomorphism

```
FindGraphIsomorphism(graph1, graph2)
```

> returns an isomorphism between `graph1` and `graph2` if it exists. Return an empty list if no isomorphism exists.
 
See:
* [Wikipedia - Graph isomorphism](https://en.wikipedia.org/wiki/Graph_isomorphism) 
  

### Examples

```
>> FindGraphIsomorphism(Graph({1,2,3,4},{1<->2,1<->4,2<->3,3<->4}), Graph({1,2,3,4},{1<->3,1<->4,2<->3,2<->4})) 
{<|1->2,2->3,3->1,4->4|>}
```

### Related terms 
[IsomorphicGraphQ](IsomorphicGraphQ.md), [GraphCenter](GraphCenter.md), [GraphDiameter](GraphDiameter.md), [GraphPeriphery](GraphPeriphery.md), [GraphRadius](GraphRadius.md), [AdjacencyMatrix](AdjacencyMatrix.md), [EdgeList](EdgeList.md), [EdgeQ](EdgeQ.md), [EulerianGraphQ](EulerianGraphQ.md), [FindEulerianCycle](FindEulerianCycle.md), [FindHamiltonianCycle](FindHamiltonianCycle.md), [FindVertexCover](FindVertexCover.md), [FindShortestPath](FindShortestPath.md), [FindSpanningTree](FindSpanningTree.md), [Graph](Graph.md), [GraphQ](GraphQ.md), [HamiltonianGraphQ](HamiltonianGraphQ.md), 
[VertexEccentricity](VertexEccentricity.md), [VertexList](VertexList.md), [VertexQ](VertexQ.md) 






### Implementation status

* &#x2611; - partially implemented

### Github

* [Implementation of FindGraphIsomorphism](https://github.com/axkr/symja_android_library/blob/master/symja_android_library/matheclipse-core/src/main/java/org/matheclipse/core/builtin/GraphFunctions.java#L1858) 
