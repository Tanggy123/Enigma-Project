package enigma;

import java.util.Scanner;
import java.util.HashMap;
import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author James (Dayuan) Tang
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = cycles;
        Scanner c = new Scanner(_cycles);
        while (c.hasNext()) {
            String next = c.next();
            if (next.charAt(0) != '('
                    || next.charAt(next.length() - 1) != ')') {
                throw error("Invalid cycles");
            }
        }
        int startIndex = 0;
        for (int i = 0; i < _cycles.length(); i++) {
            if (_cycles.charAt(i) == '(') {
                startIndex = i + 1;
            } else if (_cycles.charAt(i) == ')') {
                addCycle(_cycles.substring(startIndex, i));
            }
        }
        for (int i = 0; i < alphabet.size(); i++) {
            char ch = alphabet.toChar(i);
            if (!_pmap.containsKey(ch)) {
                addCycle(Character.toString(ch));
                _derangement = false;
            }
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        for (int i = 0; i < cycle.length(); i++) {
            if (cycle.charAt(i) == ' ') {
                throw error("Cycle cannot contain whitespaces");
            }
            if (i == cycle.length() - 1) {
                _pmap.put(cycle.charAt(i), cycle.charAt(0));
                _reversePmap.put(cycle.charAt(0), cycle.charAt(i));
                break;
            }
            _pmap.put(cycle.charAt(i), cycle.charAt(i + 1));
            _reversePmap.put(cycle.charAt(i + 1), cycle.charAt(i));
        }
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        char ch = _alphabet.toChar(wrap(p));
        return alphabet().toInt(_pmap.get(ch));
    }

    /** Return the result of applying the inverse of this permutation
     *  to C modulo the alphabet size. */
    int invert(int c) {
        char ch = _alphabet.toChar(wrap(c));
        return alphabet().toInt(_reversePmap.get(ch));
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        p = Character.toUpperCase(p);
        int index = permute(_alphabet.toInt(p));
        return alphabet().toChar(index);
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        c = Character.toUpperCase(c);
        int index = invert(_alphabet.toInt(c));
        return alphabet().toChar(index);
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        return _derangement;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** Cycles of permutations. */
    private String _cycles;

    /***/
    private HashMap<Character, Character> _pmap = new HashMap<>();

    /** A reverse hashmap to store permutation. */
    private HashMap<Character, Character> _reversePmap = new HashMap<>();

    /** If the permutation is deranged. */
    private boolean _derangement = true;
}
