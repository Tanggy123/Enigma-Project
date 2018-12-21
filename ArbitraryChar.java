package enigma;
import static enigma.EnigmaException.*;

/** An Alphabet consisting of the Unicode characters in a certain range (not in
 *  order).
 *  @author James (Dayuan) Tang
 */
class ArbitraryChar extends Alphabet {

    /** A String that stores the alphabet.*/
    private String _arbAlphabet;

    /** An alphabet consisting of characters in the
     * string _arbAlphabet.
     * @param arbAlphabet input string*/
    ArbitraryChar(String arbAlphabet) {
        _arbAlphabet = arbAlphabet;
        for (int i = 0; i < _arbAlphabet.length(); i++) {
            if (_arbAlphabet.charAt(i) == '('
                    || _arbAlphabet.charAt(i) == ')'
                    || _arbAlphabet.charAt(i) == '-'
                    || _arbAlphabet.charAt(i) == '*'
                    || (_arbAlphabet.charAt(i) >= 'a'
                            && _arbAlphabet.charAt(i) <= 'z')) {
                throw error("Alphabet cannot contain "
                        + "(, ), -, * or lower case letters");
            }
        }
    }

    @Override
    int size() {
        return _arbAlphabet.length();
    }

    @Override
    boolean contains(char ch) {
        return _arbAlphabet.indexOf(ch) != -1;
    }

    @Override
    char toChar(int index) {
        if (index >= size()) {
            throw error("Character index out of range");
        }
        return _arbAlphabet.charAt(index);
    }

    @Override
    int toInt(char ch) {
        if (!contains(ch)) {
            throw error("Character is not in alphabet");
        }
        return _arbAlphabet.indexOf(ch);
    }


}
