## FindMinimum

```
FindMinimum(f, {x, xstart})
```

> searches for a local numerical minimum of `f` for the variable `x` and the start value `xstart`. 

```
FindMinimum(f, {x, xstart}, Method->methodName)
```

> searches for a local numerical minimum of `f` for the variable `x` and the start value `xstart`, with one of the following method names:

```
FindMinimum(f, {{x, xstart},{y, ystart},...})
```

> searches for a local numerical minimum of the multivariate function `f` for the variables `x, y,...` and the corresponding start values `xstart, ystart,...`. 

See
* [Wikipedia - Mathematical optimization](https://en.wikipedia.org/wiki/Mathematical_optimization)

#### Powell

Implements the Powell optimizer. 

This is the default method, if no `methodName` is given.

#### ConjugateGradient

Implements the ConjugateGradient optimizer.  
This is a derivative based method and the functions must be symbolically differentiable.

#### SequentialQuadratic

Implements the sequentiel quadratic optimizer.  
This is a derivative, multivariate based method and the functions must be symbolically differentiable.

### Examples

```
>> FindMinimum(Sin(x), {x, 0.5}) 
{-1.0,{x->-1.5708}}

>> FindMinimum(Sin(x)*Sin(2*y), {{x, 2}, {y, 2}}, Method -> "ConjugateGradient") 
{-1.0,{x->1.5708,y->2.35619}}        
```

### Related terms 
[FindMaximum](FindMaximum.md), [FindRoot](FindRoot.md), [NRoots](NRoots.md), [Solve](Solve.md)
 

### Implementation status

* &#x2611; - partially implemented

### Github

* [Implementation of FindMinimum](https://github.com/axkr/symja_android_library/blob/master/symja_android_library/matheclipse-core/src/main/java/org/matheclipse/core/reflection/system/FindMinimum.java#L110) 
