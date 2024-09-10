package org.matheclipse.core.patternmatching;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.matheclipse.core.eval.Errors;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.exception.ValidateException;
import org.matheclipse.core.eval.interfaces.IFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.expression.S;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.parser.ExprParser;

/**
 * <p>
 * Create a pattern-matching rule which invokes the method name in the given instance, if
 * leftHandSide is matching.
 * 
 * <p>
 * Deprecated: don't use Java reflection for method calling.
 * 
 */
@Deprecated
public final class PatternMatcherAndInvoker extends PatternMatcher {
  private static final long serialVersionUID = -2448717771259975643L;

  private transient Method fMethod;
  private transient Type[] fTypes;
  private transient IFunctionEvaluator fInstance;

  @Override
  public Object clone() throws CloneNotSupportedException {
    PatternMatcherAndInvoker v = (PatternMatcherAndInvoker) super.clone();
    v.fLHSPriority = fLHSPriority;
    v.fThrowIfTrue = fThrowIfTrue;
    v.fLhsPatternExpr = fLhsPatternExpr;
    IPatternMap patternMap = createPatternMap();
    v.fPatternMap = patternMap.copy();
    v.fLhsExprToMatch = fLhsExprToMatch;
    v.fSetFlags = fSetFlags;
    v.fMethod = fMethod;
    v.fTypes = fTypes;
    v.fInstance = fInstance;
    return v;
  }

  @Override
  public IPatternMatcher copy() {
    PatternMatcherAndInvoker v = new PatternMatcherAndInvoker();
    v.fLhsPatternExpr = fLhsPatternExpr;
    if (fPatternMap != null) {
      v.fPatternMap = fPatternMap.copy();
    }
    v.fLhsExprToMatch = fLhsExprToMatch;
    v.fSetFlags = fSetFlags;
    v.fMethod = fMethod;
    v.fTypes = fTypes;
    v.fInstance = fInstance;
    return v;
  }

  /** Void constructor for Externalizable. */
  @SuppressWarnings("unused")
  public PatternMatcherAndInvoker() {
    super(null);
  }

  /**
   * <p>
   * Create a pattern-matching rule which invokes the method name in the given instance, if
   * leftHandSide is matching.
   * 
   * <p>
   * Deprecated: don't use Java reflection for method calling.
   *
   * @param leftHandSide could contain pattern expressions for "pattern-matching"
   * @param instance instance of an IFunctionEvaluator interface
   * @param methodName method to call
   */
  @Deprecated
  public PatternMatcherAndInvoker(final IExpr leftHandSide, IFunctionEvaluator instance,
      final String methodName) {
    super(leftHandSide);
    this.fInstance = instance;
    initInvoker(instance, methodName);
  }

  /**
   * <p>
   * Create a pattern-matching rule which invokes the method name in the given instance, if
   * leftHandSide is matching.
   * 
   * <p>
   * Deprecated: don't use Java reflection for method calling.
   * 
   * @param leftHandSide
   * @param instance
   * @param methodName
   */
  @Deprecated
  public PatternMatcherAndInvoker(final String leftHandSide, IFunctionEvaluator instance,
      final String methodName) {
    this.fInstance = instance;

    final ExprParser parser = new ExprParser(EvalEngine.get());
    IExpr lhs = parser.parse(leftHandSide);

    fLhsPatternExpr = lhs;
    int[] priority = new int[] {IPatternMap.DEFAULT_RULE_PRIORITY};
    determinePatterns(priority);
    this.fLHSPriority = priority[0];
    initInvoker(instance, methodName);
  }

  private void initInvoker(IFunctionEvaluator instance, final String methodName) {
    Class<? extends IFunctionEvaluator> c = instance.getClass();
    Method[] declaredMethods = c.getDeclaredMethods();
    List<Method> namedMethods = new ArrayList<Method>();
    for (Method method : declaredMethods) {
      if (method.getName().equals(methodName))
        namedMethods.add(method);
    }
    if (namedMethods.size() == 1) {
      this.fMethod = namedMethods.get(0);
      this.fTypes = fMethod.getGenericParameterTypes();
    } else {
      // throw an exception ?
    }
  }

  /** {@inheritDoc} */
  @Override
  public IExpr eval(final IExpr leftHandSide, EvalEngine engine) {

    if (isRuleWithoutPatterns() && fLhsPatternExpr.equals(leftHandSide)) {
      if (fTypes.length != 0) {
        return F.NIL;
      }
      IExpr result = F.NIL;
      try {
        result = (IExpr) fMethod.invoke(fInstance);
      } catch (ValidateException | IllegalArgumentException | ReflectiveOperationException e) {
        Errors.printMessage(S.General, e, engine);
      }
      return result != null ? result : F.NIL;
    }
    IPatternMap patternMap = createPatternMap();
    if (fTypes.length != patternMap.size()) {
      return F.NIL;
    }
    patternMap.initPattern();
    if (matchExpr(fLhsPatternExpr, leftHandSide, engine)) {

      List<IExpr> args = patternMap.getValuesAsList();
      try {
        if (args != null) {
          IExpr result = (IExpr) fMethod.invoke(fInstance, args.toArray());
          return result != null ? result : F.NIL;
        }
      } catch (IllegalArgumentException | ReflectiveOperationException e) {
        Errors.printMessage(S.General, e, engine);
      }
    }
    return F.NIL;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj instanceof PatternMatcherAndInvoker) {
      // don't compare fInstance, fMethod, fTypes here
      return super.equals(obj);
    }

    return false;
  }

  @Override
  public int hashCode() {
    return super.hashCode() * 47;
  }
}
