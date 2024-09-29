
Test the `Graphics` function for creating correct JSON output for the `json2D_JSXGraph` project:
- [https://github.com/jsxgraph/json2D_JSXGraph](https://github.com/jsxgraph/json2D_JSXGraph)

```mma
Plot[Sin[x], {x, -Pi, Pi}]
```

```mma
Plot[Tan[x], {x, -Pi, Pi}, PlotRange->{-10,10}]
```

```mma
Plot[Sin[E^x],{x,-2,6},PlotRange->{-3,3}]
```

```mma
LogPlot[{x^x, Exp[x], x!}, {x, 1, 5}]
```

```mma
NumberLinePlot[{Prime[Range[20]],Prime[Range[40]],Prime[Range[80]]}]
```

```mma
LogLogPlot[{Log[x]^x, x^x}, {x, 0.1, 10}]
```

```mma
LogLinearPlot[{Erf[x], Erfc[x]}, {x, 0.01, 10}]
```

```mma
ListPolarPlot[Table[{n, Log[n]}, {n, 500}]]
```

```mma
ListPolarPlot[{Range[100]/4, Sqrt[Range[100]], Log[Range[100]]}]
```

```mma
ListPlot[Prime[Range[25]]]
```

```mma
ListPlot[
 Table[{k, 
   PDF[BinomialDistribution[50, p], k]}, {p, {0.3, 0.5, 0.8}}, {k, 0, 
   50}], Filling -> Axis]
```

```mma
ListLogLogPlot[{Range[20], Sqrt[Range[20]], Log[Range[20]]}, Joined -> True]
```

```mma
ListLogLogPlot[Range[20]^3, Filling -> Bottom]
```

```mma
ListLogLogPlot[Range[20]^3, Filling -> Axis]
```
 
```mma
ListLogLinearPlot[ Table[{n, n^k}, {k, {-1, -0.5, 0.5, 1}}, {n, 1, 10}], Joined -> True ]
```

```mma
Graphics[Point[Table[{t, Cos[t]}, {t,-Pi, Pi, 0.2}]]]
```

```mma
Graphics[ Table[{Hue[RandomReal[]], Arrow[RandomReal[1, {2, 2}]]}, {75}]]
```

```mma
Graphics[Table[{Hue[h, s], Disk[{12h, 8s}]}, {h, 0, 1, 1/6}, {s, 0, 1, 1/4}]]
```

```mma
Graphics[Table[{EdgeForm[{GrayLevel[0, 0.5]}], Hue[(-11+q+10*r)/72, 1, 1, 0.6], Disk[(8-r)*{Cos[2*Pi*q/12], Sin[2*Pi*q/12]}, (8-r)/3]}, {r,6}, {q, 12}]]
```

```mma
Graphics[Table[{AbsoluteThickness[t], Line[{{20 t, 10}, {20 t, 80}}], Text[ToString[t]<>"pt", {20 t, 0}]}, {t, 0, 10}]]
```

```mma
Graphics[GraphicsComplex[{{0, 0}, {2, 0}, {2, 2}, {0, 2}}, Table[Circle[i], {i, 4}]]]
```

```mma
data = Table[15 {Cos[t], Sin[t]}, {t, 0, 4*Pi, 4*Pi/5}];

Graphics[GraphicsComplex[data, {Green, Line[{1, 2, 3, 4, 5, 6}], Red, Point[{1, 2, 3, 4, 5}]}]]
```

```mma
Graphics[Line[{{-1, -1}, {3,3}, {1, 1}, {4, 5}}],Axes->True, PlotRange->{0.0, 2.0}]
```
