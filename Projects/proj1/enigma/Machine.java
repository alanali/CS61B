package enigma;

import java.util.ArrayList;
import java.util.Collection;
import static enigma.EnigmaException.error;

/** Class that represents a complete enigma machine.
 *  @author Alana Li
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        if (numRotors <= pawls || pawls <= 0) {
            throw error("Number of pawls or rotors unacceptable.");
        }
        _alphabet = alpha;
        _rotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors;
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _rotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Return Rotor #K, where Rotor #0 is the reflector, and Rotor
     *  #(numRotors()-1) is the fast Rotor.  Modifying this Rotor has
     *  undefined results. */
    Rotor getRotor(int k) {
        return _rotorsList.get(k);
    }

    Alphabet alphabet() {
        return _alphabet;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        _rotorsList = new ArrayList<>();
        for (String rotor : rotors) {
            for (Rotor r : _allRotors) {
                if (rotor.equals(r.name())) {
                    if (_rotorsList.contains(r)) {
                        throw error("Rotor already in list.");
                    }
                    _rotorsList.add(r);
                }
            }
        }
        for (int i = 0; i < _rotorsList.size(); i++) {
            if (i == 0 && !_rotorsList.get(i).reflecting()) {
                throw error("Rotor in reflector " + _rotorsList.get(i).name()
                        + " position");
            } else if (i != 0 && _rotorsList.get(i).reflecting()) {
                throw error("Reflector in invalid position");
            }
        }
        if (_rotorsList.size() != rotors.length) {
            throw error("Rotors not found in config file.");
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != numRotors() - 1) {
            throw error("Setting length mismatch.");
        }
        int i = 1;
        for (char s : setting.toCharArray()) {
            if (!_alphabet.contains(s)) {
                throw error("Character setting of " + s + " not in alphabet.");
            }
            _rotorsList.get(i).set(s);
            i++;
        }
    }

    /** Return the current plugboard's permutation. */
    Permutation plugboard() {
        return _plugboard;
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Set the ring settings for rotors.
     * @param rings - the ring settings */
    void setRings(String rings) {
        if (rings.equals("")) {
            for (int i = 1; i < _rotors; i++) {
                rings += _alphabet.toChar(0);
            }
        }
        for (int i = 1; i < _rotors; i++) {
            _rotorsList.get(i).setRings(rings.charAt(i - 1));
        }
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0...alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        advanceRotors();
        if (Main.verbose()) {
            System.err.print("[");
            for (int r = 1; r < numRotors(); r += 1) {
                System.err.printf("%c",
                        alphabet().toChar(getRotor(r).setting()));
            }
            System.err.printf("] %c -> ", alphabet().toChar(c));
        }
        c = plugboard().permute(c);
        if (Main.verbose()) {
            System.err.printf("%c -> ", alphabet().toChar(c));
        }
        c = applyRotors(c);
        c = plugboard().invert(c);
        if (Main.verbose()) {
            System.err.printf("%c%n", alphabet().toChar(c));
        }
        return c;
    }

    /** Advance all rotors to their next position. */
    private void advanceRotors() {
        boolean[] rotated = new boolean[_rotorsList.size()];
        for (int i = 0; i < _rotorsList.size(); i++) {
            rotated[i] = (i == _rotorsList.size() - 1)
                    || (_rotorsList.get(i).rotates()
                    && _rotorsList.get(i + 1).atNotch());
        }
        for (int i = 0; i < _rotorsList.size(); i++) {
            if (rotated[i]) {
                _rotorsList.get(i).advance();
                if (i < _rotorsList.size() - 1) {
                    _rotorsList.get(i + 1).advance();
                    i++;
                }
            }
        }
    }

    /** Return the result of applying the rotors to the character C (as an
     *  index in the range 0...alphabet size - 1). */
    private int applyRotors(int c) {
        for (int i = _rotors - 1; i >= 0; i--) {
            c = _rotorsList.get(i).convertForward(c);
        }
        for (int j = 1; j < _rotors; j++) {
            c = _rotorsList.get(j).convertBackward(c);
        }
        return c;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String sol = "";
        for (int i = 0; i < msg.length(); i++) {
            char letter = msg.charAt(i);
            sol += alphabet().toChar(convert(alphabet().toInt(letter)));
        }
        return sol;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Number of rotors in machine. */
    private final int _rotors;

    /** Number of pawls in machine. */
    private final int _pawls;

    /** Collection of rotor choices. */
    private final Collection<Rotor> _allRotors;

    /** List of rotors in machine. */
    private ArrayList<Rotor> _rotorsList;

    /** Plugboard of machine. */
    private Permutation _plugboard;
}
