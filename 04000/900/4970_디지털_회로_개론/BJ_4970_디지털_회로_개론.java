// https://www.acmicpc.net/problem/4970

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BJ_4970_디지털_회로_개론 {

   interface ExpressionNode {
      int eval(final Map<Character, Integer> vars);
   }

   static class Negate implements ExpressionNode {
      private ExpressionNode inner;

      Negate(final ExpressionNode inner) {
         this.inner = inner;
      }

      @Override
      public int eval(final Map<Character, Integer> vars) {
         final int v = inner.eval(vars);
         return (v == 1 ? 1 : (v == 2 ? 0 : 2));
      }
   }

   static class Variable implements ExpressionNode {
      private char c;

      Variable(final char c) {
         this.c = c;
      }

      @Override
      public int eval(final Map<Character, Integer> vars) {
         if ('0' <= c && c <= '9') {
            return c - '0';
         }
         return vars.get(c);
      }
   }

   static class Operator implements ExpressionNode {
      private final char op;
      private final ExpressionNode lhs;
      private final ExpressionNode rhs;

      Operator(final char op, final ExpressionNode lhs, final ExpressionNode rhs) {
         this.op = op;
         this.lhs = lhs;
         this.rhs = rhs;
      }

      @Override
      public int eval(final Map<Character, Integer> vars) {
         final int lhsEval = lhs.eval(vars);
         final int rhsEval = rhs.eval(vars);

         if (op == '*') {
            if (lhsEval == 0 || rhsEval == 0) {
               return 0;
            }
            if (lhsEval == 2 && rhsEval == 2) {
               return 2;
            }
            return 1;
         } else {
            if (lhsEval == 2 || rhsEval == 2) {
               return 2;
            }
            if (lhsEval == 1 || rhsEval == 1) {
               return 1;
            }
            return 0;
         }
      }
   }


   static class ExpressionParser {
      private Stack<ExpressionNode> operands = new Stack<>();
      private Stack<Character> operators = new Stack<>();

      private void fuseNodes() {
         while (2 <= operands.size() && operators.peek() != '(') {
            final ExpressionNode rhs = operands.pop();
            final ExpressionNode lhs = operands.pop();
            operands.add(new Operator(operators.pop(), lhs, rhs));
         }
      }

      private void addOperand(final ExpressionNode newOperand) {
         operands.add(newOperand);
      }

      private int parse(final String exp, final int offset) {
         int i = 0;

         while (offset + i < exp.length()) {
            switch (exp.charAt(offset + i)) {
               case '+', '*' -> {
                  operators.add(exp.charAt(offset + i++));
                  continue;
               }

               case '(' -> {
                  operators.add('(');
                  i += parse(exp, offset + i + 1) + 1;
                  fuseNodes();
                  continue;
               }

               case ')' -> {
                  operators.pop();
                  return i + 1;
               }
            }

            boolean negate = false;
            while (exp.charAt(offset + i) == '-') {
               negate = !negate;
               ++i;
            }

            if (exp.charAt(offset + i) == '(') {
               operators.add('(');
               i += parse(exp, offset + i + 1) + 1;
               if (negate) {
                  addOperand(new Negate(operands.pop()));
               }
               fuseNodes();
            } else {
               final ExpressionNode var = new Variable(exp.charAt(offset + i++));
               addOperand(negate ? new Negate(var) : var);
               fuseNodes();
            }
         }

         return i;
      }

      ExpressionNode parse(final String exp) {
         parse(exp, 0);
         return operands.pop();
      }
   }

   static int findAllCombinations(final ExpressionNode exp) {
      int count = 0;

      final Map<Character, Integer> vars = new TreeMap<>();
      for (int p = 0; p < 3; ++p) {
         vars.put('P', p);
         for (int q = 0; q < 3; ++q) {
            vars.put('Q', q);
            for (int r = 0; r < 3; ++r) {
               vars.put('R', r);
               if (exp.eval(vars) == 2) {
                  ++count;
               }
            }
         }
      }

      return count;
   }

   public static void main(final String[] args) throws IOException {
      final StringBuilder sb = new StringBuilder();

      try (final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {
         while (true) {
            final String line = stdin.readLine();
            if (line.equals(".")) {
               break;
            }

            final ExpressionNode exp = new ExpressionParser().parse(line);
            sb.append(findAllCombinations(exp));
            sb.append('\n');
         }
      }

      System.out.print(sb);
   }

}
