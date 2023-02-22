package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.NoSuchElementException;

import ucb.util.CommandArgs;

import static enigma.EnigmaException.*;


/** Enigma simulator.
 *  @author Alana Li
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
            CommandArgs options =
                new CommandArgs("--verbose --=(.*){1,3}", args);
            if (!options.ok()) {
                throw error("Usage: java enigma.Main [--verbose] "
                            + "[INPUT [OUTPUT]]");
            }

            _verbose = options.contains("--verbose");
            new Main(options.get("--")).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Open the necessary files for non-option arguments ARGS (see comment
      *  on main). */
    Main(List<String> args) {
        _config = getInput(args.get(0));

        if (args.size() > 1) {
            _input = getInput(args.get(1));
        } else {
            _input = new Scanner(System.in);
        }

        if (args.size() > 2) {
            _output = getOutput(args.get(2));
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
        Machine m = readConfig();

        while (_input.hasNextLine()) {
            boolean lastline = true;
            String line = _input.nextLine();
            if (line != "" && line.charAt(0) != '*') {
                throw error("No settings in input.");
            }
            while (line.length() != 0 && line.charAt(0) == '*') {
                String setting = line.substring(1);
                setUp(m, setting);
                if (!_input.hasNextLine()) {
                    lastline = false;
                    break;
                }
                line = _input.nextLine();
            }
            if (_input.hasNext() || lastline) {
                while (true) {
                    line = line.replaceAll("\\s", "");
                    printMessageLine(m.convert(line));
                    if (_input.hasNext("\\*.*") || !_input.hasNextLine()) {
                        break;
                    } else {
                        line = _input.nextLine();
                    }
                }
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            _alphabet = new Alphabet(_config.next());
            if (!_config.hasNext("\\d+")) {
                throw error("Invalid number of rotors.");
            }
            int numRotors = Integer.parseInt(_config.next());
            if (!_config.hasNext("\\d+")) {
                throw error("Invalid number of pawls.");
            }
            int numPawls = Integer.parseInt(_config.next());
            Collection<Rotor> allRotors = new ArrayList<>();
            Rotor first = readRotor();
            allRotors.add(first);
            while (_config.hasNext()) {
                Rotor next = readRotor();
                if (!allRotors.isEmpty() && allRotors.contains(next.name())) {
                    throw error("Rotor " + next.name()
                            + " already in config.");
                }
                allRotors.add(next);
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
            Rotor r;
            if (name.contains("(") || name.contains(")")) {
                throw error("Illegal rotor name.");
            }
            String type = _config.next();
            String notches = type.substring(1);
            String perms = "";
            while (_config.hasNext(".*[\\(|\\)]+.*")) {
                perms += _config.next() + " ";
            }
            Permutation perm = new Permutation(perms, _alphabet);
            switch (type.charAt(0)) {
            case 'M':
                if (notches.length() == 0) {
                    throw error("No notches for moving rotor "
                            + type.charAt(0) + ".");
                }
                for (int i = 0; i < notches.length(); i++) {
                    if (!_alphabet.contains(notches.charAt(i))) {
                        throw error("Notch " + notches.charAt(i)
                                + " not in alphabet.");
                    }
                }
                r = new MovingRotor(name, perm, notches);
                break;
            case 'N':
                if (notches.length() != 0) {
                    throw error("Notches for fixed motor.");
                }
                r = new FixedRotor(name, perm);
                break;
            case 'R':
                if (notches.length() != 0) {
                    throw error("Notches for reflector.");
                }
                r = new Reflector(name, perm);
                if (!r.permutation().derangement()) {
                    throw error("Permutation for reflector not complete.");
                }
                break;
            default:
                throw error("Rotor type of 'M'/'N'/'R' not found, instead "
                        + type.charAt(0) + ".");
            }
            return r;
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        int ind = 0;
        String[] rotorsList = new String[M.numRotors()];
        String[] set = settings.substring(1).split(" ");
        for (int i = 0; i < M.numRotors(); i++) {
            rotorsList[i] = set[i];
            ind = i;
        }
        M.insertRotors(rotorsList);

        ind += 1;
        String initial = set[ind];
        M.setRotors(initial);

        ind += 1;
        String rings = "";
        if (ind <= set.length - 1 && set[ind].charAt(0) != '(') {
            rings = set[ind];
            M.setRings(rings);
            ind += 1;
        }

        String perms = "";
        for (int j = ind; j < set.length; j++) {
            perms += set[j] + " ";
        }
        Permutation p = new Permutation(perms, _alphabet);
        M.setPlugboard(p);
    }

    /** Return true iff verbose option specified. */
    static boolean verbose() {
        return _verbose;
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        for (int i = 0; i < msg.length(); i++) {
            _output.print(msg.charAt(i));
            if (((i + 1) % 5 == 0) && (i != msg.length() - 1)) {
                _output.print(" ");
            }
        }
        _output.print("\n");
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private final Scanner _input;

    /** Source of machine configuration. */
    private final Scanner _config;

    /** File for encoded/decoded messages. */
    private final PrintStream _output;

    /** True if --verbose specified. */
    private static boolean _verbose;
}
