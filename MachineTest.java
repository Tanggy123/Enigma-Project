package enigma;

import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;
import static org.junit.Assert.*;
import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Machine class.
 *  @author James (Dayuan) Tang
 */
public class MachineTest {

    /* ***** TESTING UTILITIES ***** */
    private Alphabet alph = new ArbitraryChar("ACDEFH");
    private ArrayList<Rotor> allRotors;

    private void addRotors1() {
        allRotors = new ArrayList<>();
        addMRotors("I", NAVALA, "");
        addMRotors("III", NAVALA, "");
        addFRotors("II", NAVALA);
        addReflector("B", NAVALA);
    }

    private void addRotors2() {
        allRotors = new ArrayList<>();
        addMRotors("Beta", NAVALA, "");
        addMRotors("III", NAVALA, "");
        addMRotors("I", NAVALA, "");
        addFRotors("II", NAVALA);
        addReflector("B", NAVALA);
    }

    private void addMRotors(String name,
                            HashMap<String, String> rotors, String notches) {
        MovingRotor mrotor = new MovingRotor(name,
                new Permutation(rotors.get(name), UPPER), notches);
        allRotors.add(mrotor);
    }

    private void addFRotors(String name, HashMap<String, String> rotors) {
        FixedRotor frotor = new FixedRotor(name,
                new Permutation(rotors.get(name), UPPER));
        allRotors.add(frotor);
    }

    private void addReflector(String name, HashMap<String, String> rotors) {
        Reflector r = new Reflector(name,
                new Permutation(rotors.get(name), UPPER));
        allRotors.add(r);
    }

    private String getSetting(Alphabet a, Rotor[] rotors) {
        String result = "";
        for (int i = 0; i < rotors.length; i++) {
            result = result + a.toChar(rotors[i].setting());
        }
        return result;
    }

    Machine enigma1;

    private void checkMachine1() {
        enigma1 = new Machine(UPPER, 4, 2, allRotors);
    }

    @Test
    public void checkIdInsert1() {
        addRotors1();
        checkMachine1();
        enigma1.insertRotors(new String[] {"B", "II", "III", "I"});
        assertEquals(msg("Insert", "wrong insertRotor method"),
                allRotors.get(3), enigma1.rotorSlots().get(0));
    }
}
