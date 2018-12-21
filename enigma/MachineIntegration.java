package enigma;

import org.junit.Test;
import java.util.ArrayList;

import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of Integration tests for the Machine class.
 *  @author James (Dayuan) Tang
 */
public class MachineIntegration {

    /** Testing time limit. */

    private Alphabet _alphabet =
            new ArbitraryChar("ABCDEFGHIJKLMNOPQRSTUVWXYZ");

    /**A list of available rotors. */
    private ArrayList<Rotor> allRotors = new ArrayList<>();

    /** Create rotors. */
    private void createRotors() {
        allRotors.add(new MovingRotor("I",
                new Permutation("(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) "
                        + "(JZ) (S)", _alphabet), "Q"));
        allRotors.add(new MovingRotor("II",
                new Permutation("(FIXVYOMW) (CDKLHUP) (ESZ) (BJ) (GR) "
                        + "(NT) (A) (Q)", _alphabet), "E"));
        allRotors.add(new MovingRotor("III",
                new Permutation("(ABDHPEJT) (CFLVMZOYQIRWUKXSG) "
                        + "(N)", _alphabet), "V"));
        allRotors.add(new MovingRotor("IV",
                new Permutation("(AEPLIYWCOXMRFZBSTGJQNH) "
                        + "(DV) (KU)", _alphabet), "J"));
        allRotors.add(new MovingRotor("V",
                new Permutation("(AVOLDRWFIUQ)(BZKSMNHYC) "
                        + "(EGTJPX)", _alphabet), "Z"));
        allRotors.add(new MovingRotor("VI",
                new Permutation("(AJQDVLEOZWIYTS) (CGMNHFUX) "
                        + "(BPRK) ", _alphabet), "ZM"));
        allRotors.add(new MovingRotor("VII",
                new Permutation("(ANOUPFRIMBZTLWKSVEGCJYDHXQ) ",
                        _alphabet), "ZM"));
        allRotors.add(new MovingRotor("VIII",
                new Permutation("(AFLSETWUNDHOZVICQ) (BKJ) (GXY) (MPR)",
                        _alphabet), "ZM"));
        allRotors.add(new FixedRotor("Beta",
                new Permutation("(ALBEVFCYODJWUGNMQTZSKPR) (HIX)", _alphabet)));
        allRotors.add(new FixedRotor("Gamma",
                new Permutation("(AFNIRLBSQWVXGUZDKMTPCOYJHE)", _alphabet)));
        allRotors.add(new Reflector("B",
                new Permutation("(AE) (BN) (CK) (DQ) (FU) (GY) (HW) (IJ)"
                        + " (LO) (MP)\n           (RX) (SZ) (TV)", _alphabet)));
        allRotors.add(new Reflector("C",
                new Permutation("(AR) (BD) (CO) (EJ) (FN) (GT) (HK) (IV)"
                        + " (LM) (PW)\n           (QZ) (SX) (UY)", _alphabet)));

    }

    /** Integration Test for Machine. */
    @Test
    public void testMachine() {
        createRotors();
        Machine enigma = new Machine(_alphabet, 5, 3, allRotors);
        enigma.insertRotors(new String[] {"B", "BETA", "III", "IV", "I"});
        enigma.setRotors("AXLE");
        assertEquals("HYIHLBKOMLIUYDCMPPSFSZWSQCNJEXNUOJYRZEKTCNBDGU",
                enigma.convert("FROM his shoulder Hiawatha "
                       + "Took the camera of rosewood"));
    }
}
