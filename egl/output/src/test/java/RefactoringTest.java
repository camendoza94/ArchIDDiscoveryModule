import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;
import rules.R1RefactoringRule;

public class RefactoringTest {

    public void test() {
        JavaCheckVerifier.verify("src/main/java/rules/Refactoring.java", new R1RefactoringRule());
    }
}
