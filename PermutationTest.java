package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author James (Dayuan) Tang
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                    Character.toUpperCase(e), perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                    Character.toUpperCase(c), perm.invert(e));
            int ci = alpha.indexOf(Character.toUpperCase(c)),
                    ei = alpha.indexOf(Character.toUpperCase(e));
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void checkIdSwitch() {
        perm = new Permutation("      (ABCDEFGHIJKLMNOPQRSTUVWXYZ)", UPPER);
        checkPerm("switch", UPPER_STRING, "BCDEFGHIJKLMNOPQRSTUVWXYZA");
        assertEquals(perm.invert('C'), 'B');
    }

    @Test
    public void checkIdDerange() {
        perm = new Permutation("(ABCDEFGHIJKLMNOPQRSTUVWX)", UPPER);
        checkPerm("non-derange", UPPER_STRING, "BCDEFGHIJKLMNOPQRSTUVWXAYZ");
        assertEquals(perm.invert('A'), 'X');
    }

    @Test
    public void checkIdLowerCase() {
        perm = new Permutation("      (ABCDEFGHIJKLMNOPQRSTUVWXYZ)", UPPER);
        checkPerm("switch",
                "abcdefghijklmnopqrstuvwxyz", "BCDEFGHIJKLMNOPQRSTUVWXYZA");
        assertEquals(perm.permute('c'), 'D');
        assertEquals(perm.invert('C'), 'B');
    }

}
