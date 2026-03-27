package ch1_fundamentals.bags_queues_stacks;

public class Parentheses {
  public static boolean isValid(String parens) {
    Stack<Character> s = new Stack<>();
    char c;

    for (int i = 0; i < parens.length(); i++) {
      c = parens.charAt(i);
      if (isOpening(c)) {
        s.push(c);
      } else {
        if (s.isEmpty())
          return false;
        if (!areMatching(s.pop(), c))
          return false;
      }
    }

    if (!s.isEmpty())
      return false;

    return true;
  }

  private static boolean isOpening(char c) {
    return c == '(' || c == '[' || c == '{';
  }

  private static boolean areMatching(char a, char b) {
    return (a == '(' && b == ')') ||
        (a == '[' && b == ']') ||
        (a == '{' && b == '}');
  }

  public static void main(String[] args) {
    System.out.println(isValid(args[0]));
  }
}
