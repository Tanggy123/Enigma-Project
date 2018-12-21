package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author James (Dayuan) Tang
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine enigma = readConfig();
        if (!_input.hasNext()) {
            throw error("Missing setting");
        } else if (!_input.hasNext("\\*")) {
            throw error("Missing setting");
        }

        while (_input.hasNext() || _input.hasNextLine()) {
            String line = _input.nextLine();
            if (line.isEmpty()) {
                _output.println();
                continue;
            } else if (line.charAt(0) == '*') {
                setUp(enigma, line);
                enigma.checkRotors();
            } else {
                printMessageLine(enigma.convert(line));
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            String alphabet = _config.next();
            if (alphabet.length() == 3 && alphabet.contains("-")) {
                _alphabet = new CharacterRange(alphabet.charAt(0),
                        alphabet.charAt(2));
            } else {
                _alphabet = new ArbitraryChar(alphabet);
            }
            int numRotors = _config.nextInt();
            int numPawls = _config.nextInt();
            _config.nextLine();
            ArrayList<Rotor> allRotors = new ArrayList<>();
            while (_config.hasNextLine()) {
                allRotors.add(readRotor());
            }
            return new Machine(_alphabet, numRotors, numPawls, allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String name = _config.next();
            String type = _config.next();
            String perm = _config.nextLine();
            if (_config.hasNext("\\([A-Za-z]+\\)")) {
                perm = perm + _config.nextLine();
            }
            if (type.charAt(0) == 'M') {
                return new MovingRotor(name,
                        new Permutation(perm, _alphabet), type.substring(1));
            } else if (type.charAt(0) == 'N') {
                return new FixedRotor(name, new Permutation(perm, _alphabet));
            } else if (type.charAt(0) == 'R') {
                return new Reflector(name, new Permutation(perm, _alphabet));
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
        return null;
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        Scanner s = new Scanner(settings);
        s.next();
        String[] rotors = new String[M.numRotors()];
        for (int i = 0; i < rotors.length; i++) {
            rotors[i] = s.next();
        }
        M.checkSetting(settings);
        M.insertRotors(rotors);
        if (!s.hasNext() || s.hasNext("\\(")) {
            throw error("must have settings for rotors");
        }
        String posSetting = s.next();
        if (posSetting.length() != M.numRotors() - 1) {
            throw error("Invalid settings for rotors");
        }
        M.setRotors(posSetting);
        if (s.hasNextLine()) {
            String plugboard = s.nextLine();
            M.setPlugboard(new Permutation(plugboard, _alphabet));
        }
    }


    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        int pos = 0;
        while (pos < msg.length()) {
            for (int i = 0; i < 5; i++) {
                _output.print(msg.charAt(pos));
                pos += 1;
                if (pos == msg.length()) {
                    break;
                }
            }
            _output.print(' ');
        }
        _output.println();
    }


    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;
}
