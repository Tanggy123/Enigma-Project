package enigma;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author James (Dayuan) Tang
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        if (numRotors <= 1) {
            throw error("Enigma Machine must have more than 1 rotor");
        }
        if (pawls < 0) {
            throw error("Enigma Machine cannot have negative pawls");
        }
        if (numRotors <= pawls) {
            throw error("Enigma Machine must have more rotors than pawls");
        }
        _alphabet = alpha;
        _numRotors = numRotors;
        _numPawls = pawls;
        for (Rotor r : allRotors) {
            _allRotors.put(r.name(), r);
        }
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _numPawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        _rotorSlots = new ArrayList<>();
        for (int i = 0; i < rotors.length; i++) {
            Rotor r = _allRotors.get(rotors[i]);
            _rotorSlots.add(r);
        }
    }

    /** Checks settings that are going to initialize the
     * machine.
     * @param setting input settings*/
    void checkSetting(String setting) {
        Scanner s = new Scanner(setting);
        s.next();
        if (!_allRotors.get(s.next()).reflecting()) {
            throw error("The first rotor must be a reflector");
        }
    }

    /** Checks rotors in the machine are not violating
     * rules. */
    void checkRotors() {
        int numMovingRotors = 0;
        for (Rotor r :_rotorSlots) {
            if (r.rotates()) {
                numMovingRotors += 1;
            }
        }
        if (numMovingRotors != numPawls()) {
            throw error("must have same number of pawls and moving rotors");
        }

    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 upper-case letters. The first letter refers to the
     *  leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != numRotors() - 1) {
            throw error("Setting must have numRotors()-1 letters");
        }
        for (int i = 0; i < setting.length(); i++) {
            Rotor r = _rotorSlots.get(i + 1);
            if (!alphabet().contains(setting.charAt(i))) {
                throw error("Invalid settings");
            }
            r.set(setting.charAt(i));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        if (c < 0 || c >= alphabet().size()) {
            throw error("input c must be in the range 0 to alphabet size -1");
        }
        machineAdvance();
        int output = _plugboard.permute(c);
        for (int i = numRotors() - 1; i >= 0; i = i - 1) {
            output = _rotorSlots.get(i).convertForward(output);
        }
        for (int i = 1; i < numRotors(); i++) {
            output = _rotorSlots.get(i).convertBackward(output);
        }
        output = _plugboard.invert(output);
        return output;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String result = "";
        msg = msg.toUpperCase();
        for (int i = 0; i < msg.length(); i++) {
            if (msg.charAt(i) == ' ') {
                continue;
            }
            int next = convert(alphabet().toInt(msg.charAt(i)));
            result += _alphabet.toChar(next);
        }
        return result;
    }

    /** Advance the machine by 1 step.*/
    void machineAdvance() {
        int i = 0;
        Boolean[] status = new Boolean[numRotors()];
        for (Rotor r: _rotorSlots) {
            if (!r.rotates()) {
                status[i] = false;
            } else if (i == numRotors() - 1) {
                status[i] = true;
                break;
            } else if (r.rotates()
                    && _rotorSlots.get(_rotorSlots.indexOf(r) + 1).atNotch()) {
                status[i] = true;
            } else if (r.rotates() && r.atNotch()
                    && _rotorSlots.get(_rotorSlots.indexOf(r) - 1).rotates()) {
                status[i] = true;
            } else {
                status[i] = false;
            }
            i++;

        }

        for (int a = 0; a < status.length; a++) {
            if (status[a]) {
                _rotorSlots.get(a).advance();
            }
        }
    }

    /** Returns alphabet used by my machine. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Returns rotor slots containing rotors in my machine. */
    ArrayList<Rotor> rotorSlots() {
        return _rotorSlots;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Number of rotors allowed in my machine and number of pawls. */
    private int _numRotors, _numPawls;

    /** All available rotors that can be inserted into my machine. */
    private HashMap<String, Rotor> _allRotors = new HashMap<>();

    /** Rotors in my machine. */
    private ArrayList<Rotor> _rotorSlots;

    /** Plugboard of my machine. */
    private Permutation _plugboard = new Permutation("",
            new CharacterRange('A', 'Z'));
}
