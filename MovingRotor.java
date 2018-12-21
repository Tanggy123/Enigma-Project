package enigma;

import java.util.ArrayList;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author James (Dayuan) Tang
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        this.set(0);
        _notches = new ArrayList<>();
        for (int i = 0; i < notches.length(); i++) {
            _notches.add(alphabet().toInt(notches.charAt(i)));
        }
    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    boolean atNotch() {
        return _notches.contains(setting());
    }

    @Override
    void advance() {
        set(permutation().wrap((setting() + 1)));
    }

    /** A list of all notches. */
    private ArrayList<Integer> _notches;
}
