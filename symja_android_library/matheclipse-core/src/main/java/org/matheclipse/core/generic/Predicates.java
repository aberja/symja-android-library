package org.matheclipse.core.generic;

import java.io.Serializable;
import java.util.Comparator;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.expression.S;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IASTAppendable;
import org.matheclipse.core.interfaces.IBuiltInSymbol;
import org.matheclipse.core.interfaces.IEvaluator;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IPattern;
import org.matheclipse.core.interfaces.IPatternSequence;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.core.patternmatching.IPatternMatcher;
import org.matheclipse.core.patternmatching.PatternMatcher;

public class Predicates {
  private static class InASTPredicate implements Predicate<IExpr>, Serializable {
    private static final long serialVersionUID = 0;

    private final IAST target;

    private InASTPredicate(IAST target) {
      this.target = target;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj instanceof InASTPredicate) {
        InASTPredicate that = (InASTPredicate) obj;
        return target.equals(that.target);
      }
      return false;
    }

    @Override
    public int hashCode() {
      return target.hashCode();
    }

    @Override
    public boolean test(IExpr t) {
      for (IExpr expr : target) {
        if (expr.equals(t)) {
          return true;
        }
      }
      return false;
    }

    @Override
    public String toString() {
      return "In(" + target + ")";
    }
  }

  /**
   * Implement a {@link Comparator} and check if the evaluation of a binary AST object gives
   * {@link S#False} or <code>-1</code> and return <code>1</code>, or {@link S#True} or
   * <code>1</code> and return <code>-1</code>.
   */
  public static class IsBinaryFalse
      implements BiPredicate<IExpr, IExpr>, Comparator<IExpr>, Serializable {

    private static final long serialVersionUID = -651160649796790184L;

    protected final EvalEngine engine;

    protected final IExpr head;

    /**
     * Define a binary AST with the header <code>head</code>.
     *
     * @param head the AST's head expression
     */
    public IsBinaryFalse(final IExpr head) {
      this(head, EvalEngine.get());
    }

    public IsBinaryFalse(final IExpr head, EvalEngine engine) {
      this.engine = engine;
      this.head = head;
    }

    @Override
    public int compare(final IExpr firstArg, final IExpr secondArg) {
      IAST ast = F.binaryAST2(head, firstArg, secondArg);
      IExpr temp = engine.evaluate(ast);
      if (temp.isFalse() || temp.isMinusOne()) {
        return 1;
      }
      if (temp.isTrue() || temp.isOne()) {
        return -1;
      }
      return 0;
    }

    /**
     * Check if the evaluation of a binary AST object gives <code>True</code> by settings it's first
     * argument to <code>firstArg</code> and settings it's second argument to <code>secondArg</code>
     */
    @Override
    public boolean test(final IExpr firstArg, final IExpr secondArg) {
      return F.binaryAST2(head, firstArg, secondArg)//
          .eval(engine)//
          .isFalse();
    }
  }

  /**
   * Implement a {@link Comparator} and check if the evaluation of a binary AST object gives
   * {@link S#True} or <code>1</code> and return <code>1</code>, or {@link S#False} or
   * <code>-1</code> and return <code>-1</code>.
   * 
   */
  public static class IsBinaryTrue
      implements BiPredicate<IExpr, IExpr>, Comparator<IExpr>, Serializable {

    private static final long serialVersionUID = -3680368770544249965L;

    protected final EvalEngine engine;

    protected final IExpr head;

    /**
     * Define a binary AST with the header <code>head</code>.
     *
     * @param head the AST's head expression
     */
    public IsBinaryTrue(final IExpr head) {
      this(head, EvalEngine.get());
    }

    public IsBinaryTrue(final IExpr head, EvalEngine engine) {
      this.engine = engine;
      this.head = head;
    }

    @Override
    public int compare(final IExpr firstArg, final IExpr secondArg) {
      IAST ast = F.binaryAST2(head, firstArg, secondArg);
      IExpr temp = engine.evaluate(ast);
      if (temp.isTrue() || temp.isOne()) {
        return 1;
      }
      if (temp.isFalse() || temp.isMinusOne()) {
        return -1;
      }
      return 0;
    }

    /**
     * Check if the evaluation of a binary AST object gives <code>True</code> by settings it's first
     * argument to <code>firstArg</code> and settings it's second argument to <code>secondArg</code>
     */
    @Override
    public boolean test(final IExpr firstArg, final IExpr secondArg) {
      return F.binaryAST2(head, firstArg, secondArg)//
          .eval(engine)//
          .isTrue();
    }
  }

  /**
   * Returns a predicate that evaluates to {@code true} if the object reference being tested is one
   * of the arguments of the given <code>ast</code>. It does not defensively copy the collection
   * passed in, so future changes to it will alter the behavior of the predicate.
   *
   * @param ast the AST those arguments may contain the function input
   * @return a <code>java.util.function.Predicate</code> predicate of one argument.
   */
  public static Predicate<IExpr> in(IAST ast) {
    return new InASTPredicate(ast);
  }

  /**
   * Returns a predicate that evaluates to {@code true} if the object reference being tested is one
   * of the arguments of the given <code>ast</code>. It does not defensively copy the collection
   * passed in, so future changes to it will alter the behavior of the predicate.
   *
   * @param expr the expr which may match the function input
   * @return a <code>java.util.function.Predicate</code> predicate of one argument.
   */
  public static Predicate<IExpr> in(IExpr expr) {
    return new InASTPredicate(F.list(expr));
  }

  /**
   * Returns a predicate that evaluates to {@code true} if the <code>input</code> is an AST list,
   * which equals one of the given <b>header elements</b> with it's header element at index position
   * <code>0</code>.
   *
   * @param heads
   * @return a <code>java.util.function.Predicate</code> predicate of one argument.
   */
  public static Predicate<IExpr> isAST(final ISymbol[] heads) {
    return new Predicate<IExpr>() {
      @Override
      public boolean test(IExpr input) {
        for (int i = 0; i < heads.length; i++) {
          if (input.isAST(heads[i])) {
            return true;
          }
        }
        return false;
      }
    };
  }

  /**
   * Check if the evaluation of the <code>expr</code> object gives <code>False</code>. A <code>
   * IsBinaryFalse</code> predicate will be returned.
   *
   * @param expr
   * @return a <code>java.util.function.BiPredicate</code> predicate of two arguments.
   */
  public static BiPredicate<IExpr, IExpr> isBinaryFalse(IExpr expr) {
    return new IsBinaryFalse(expr, EvalEngine.get());
  }

  /**
   * Check if the evaluation of the <code>expr</code> object gives <code>True</code>. A <code>
   * IsBinaryTrue</code> predicate will be returned.
   *
   * @param expr
   * @return a <code>java.util.function.BiPredicate</code> predicate of two arguments.
   */
  public static BiPredicate<IExpr, IExpr> isBinaryTrue(IExpr expr) {
    return new IsBinaryTrue(expr, EvalEngine.get());
  }

  /**
   * Check if the evaluation of the <code>expr</code> object gives <code>True</code>. If <code>expr
   * </code> is a symbol, which has an assigned <code>Predicate</code> evaluator object, the
   * predicate will be returned. Otherwise a <code>IsUnaryTrue</code> predicate will be returned.
   *
   * @param engine
   * @param head
   * @return
   */
  public static Predicate<IExpr> isTrue(final EvalEngine engine, final IExpr head) {
    if (head.isBuiltInSymbol()) {
      IEvaluator eval = ((IBuiltInSymbol) head).getEvaluator();
      if (eval instanceof Predicate<?>) {
        return (Predicate<IExpr>) eval;
      }
    }
    return x -> engine.evalTrue(head, x);
  }

  /** @return a <code>java.util.function.Predicate</code> predicate of one argument. */
  public static Predicate<IExpr> isUnaryVariableOrPattern() {
    return new Predicate<IExpr>() {
      @Override
      public boolean test(final IExpr firstArg) {
        if (firstArg instanceof ISymbol) {
          return true;
        }
        if (firstArg instanceof IPattern) {
          return true;
        }
        return false;
      }
    };
  }

  /**
   * Create a predicate, which represents the {@link S#SameTest} option.
   * 
   * @param head if the <code>head ==
   * {@link S#Automatic}</code> use {@link IExpr#isSame(IExpr)}, otherwise evaluate the binary
   *        function <code>head(x,y)</code> to <code>true</code> ({@link S#True})
   * @param engine
   * @return
   */
  public static BiPredicate<IExpr, IExpr> sameTest(final IExpr head, final EvalEngine engine) {
    return head.equals(S.Automatic) ? //
        (x, y) -> x.isSame(y) : //
        (x, y) -> engine.evalTrue(head, x, y);
  }

  /**
   * Convert the pattern into a pattern-matching predicate used in {@link F#FreeQ(IExpr, IExpr)}.
   * FreeQ does test for subsequences (MemberQ does not test for subsequences).
   *
   * @param pattern
   * @return
   * @see IExpr#isFree(Predicate, boolean)
   */
  public static Predicate<IExpr> toFreeQ(IExpr pattern) {
    if (pattern.isSymbol() || pattern.isNumber() || pattern.isString()) {
      return x -> x.equals(pattern);
    }
    final IPatternMatcher matcher;
    if (pattern.isOrderlessAST() && pattern.isFreeOfPatterns()) {
      // append a BlankNullSequence[] to match the parts of an Orderless expression
      IPatternSequence blankNullRest = F.$ps(null, true);
      IASTAppendable newPattern = ((IAST) pattern).appendClone(blankNullRest);
      matcher = new PatternMatcher(newPattern);
    } else {
      matcher = new PatternMatcher(pattern);
    }
    return matcher;
  }

  /**
   * Convert the pattern into a pattern-matching predicate used in {@link F#MemberQ(IExpr, IExpr)}.
   * MemberQ does not test for subsequences(FreeQ does test for subsequences).
   *
   * @param pattern
   * @return
   */
  public static Predicate<IExpr> toMemberQ(IExpr pattern) {
    if (pattern.isSymbol() || pattern.isNumber() || pattern.isString()) {
      return x -> x.equals(pattern);
    }
    return new PatternMatcher(pattern);
  }

  private Predicates() {}
}
