package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.Errors;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.exception.ValidateException;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.expression.ImplementationStatus;
import org.matheclipse.core.expression.S;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IASTAppendable;
import org.matheclipse.core.interfaces.IExpr;

public class ZTransform extends AbstractFunctionEvaluator {

  public ZTransform() {}

  @Override
  public IExpr evaluate(final IAST ast, EvalEngine engine) {
    // see http://www.reduce-algebra.com/docs/ztrans.pdf
    try {
      final IExpr fx = ast.arg1();
      if (fx.isIndeterminate()) {
        return S.Indeterminate;
      }
      IExpr n = ast.arg2();
      if (!(n.isVariable() || n.isList())) {
        // `1` is not a valid variable.
        return Errors.printMessage(ast.topHead(), "ivar", F.list(n), engine);
      }
      IExpr z = ast.arg3();
      if (!(z.isVariable() || z.isList())) {
        // `1` is not a valid variable.
        return Errors.printMessage(ast.topHead(), "ivar", F.list(z), engine);
      }
      if (!n.isList() && !z.isList()) {
        if (fx.isFree(n, true) && fx.isFree(z, true)) {
          // (fx*z)/(-1 + z)
          return F.Times(fx, z, F.Power(F.Plus(F.CN1, z), F.CN1));
        }
        if (fx.equals(n)) {
          // z/(-1 + z)^2
          return F.Times(z, F.Power(F.Plus(F.CN1, z), F.CN2));
        }
        if (fx.isAST()) {
          final IAST function = (IAST) fx;
          if (function.isPlus()) {
            // ZTransform(a_+b_+c_,n_,z_) -> ZTransform(a,n,z)+ZTransform(b,n,z)+ZTransform(c,n,z)
            return function.mapThread(F.ZTransform(F.Slot1, n, z), 1).eval(engine);
          } else if (function.isTimes()) {
            int indx = function.indexOf(x -> x.isFree(n, true));
            if (indx > 0) {
              IExpr arg = function.get(indx);
              IExpr rest = function.removeAtCopy(indx);
              return F.Times(arg, F.ZTransform(rest, n, z));
            }
            if (!function.isExpanded()) {
              return ast.setAtCopy(1, F.ExpandAll(function));
            }
          } else if (function.isPower()) {
            if (!function.isExpanded()) {
              return ast.setAtCopy(1, F.ExpandAll(function));
            }
          }
          if (function.isAST1() && function.arg1().isPlus2()) {
            IAST plus2 = (IAST) function.arg1();
            int k = plus2.first().toIntDefault();
            if (k > 0 && plus2.second().equals(n)) {
              // shift equation case
              IASTAppendable sum = F.PlusAlloc(k + 1);
              for (int i = 0; i < k; i++) {
                sum.append(F.Times(F.CN1, F.Power(z, F.ZZ(k - i)), function.setAtCopy(1, F.ZZ(i))));
              }
              sum.append(
                  F.Times(F.Power(z, F.ZZ(k)), F.ZTransform(function.setAtCopy(1, n), n, z)));
              return sum;
            }
          }
        }
      }
    } catch (final ValidateException ve) {
      // int number validation
      return Errors.printMessage(S.ZTransform, ve, engine);
    }
    return F.NIL;
  }

  @Override
  public int[] expectedArgSize(IAST ast) {
    return ARGS_3_3;
  }

  @Override
  public int status() {
    return ImplementationStatus.EXPERIMENTAL;
  }
}
