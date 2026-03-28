package ch1_fundamentals.bags_queues_stacks;

public class ParenthesesTest {
  public static void main(String[] args) {
    assert Parentheses.isValid("()");
    assert !Parentheses.isValid("(");
    assert Parentheses.isValid("(())");
    assert Parentheses.isValid("(()())((()())())");

    assert Parentheses.isValid("{}");
    assert Parentheses.isValid("[]");
    assert !Parentheses.isValid("[{]}");

    assert !Parentheses.isValid("(}}][]()");
    assert Parentheses.isValid("[{[][]}()]{[[]({()[]})]}");
  }
}
