package enigma;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Alana Li
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        cycles = cycles.replace(")(", ") (");
        if (_alphabet.size() == 0) {
            throw error("Empty alphabet.");
        }
        if (!cycles.matches("(\\(.+\\)\\s?)*")) {
            throw error("Incorrect cycle format.");
        }
        cycles = cycles.replace("(", "").replace(")", "");
        String noSpaces = cycles.replace(" ", "");
        for (char c : noSpaces.toCharArray()) {
            if (!alphabet.contains(c)) {
                throw error("Character " + c + "not in alphabet.");
            }
        }
        _cycles = cycles.split(" ");
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    void addCycle(String cycle) {
        String[] newCyc = new String[_cycles.length + 1];
        for (int i = 0; i < _cycles.length; i++) {
            for (char c : cycle.toCharArray()) {
                if (_cycles[i].contains(String.valueOf(c))) {
                    throw error("Permutation of " + c
                            + " already exists in " + _cycles[i]);
                }
            }
            newCyc[i] = _cycles[i];
        }
        newCyc[_cycles.length] = cycle;
        _cycles = newCyc;
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
        char c = _alphabet.toChar(wrap(p));
        for (String cycle : _cycles) {
            for (int s = 0; s < cycle.length(); s++) {
                if (cycle.charAt(s) == c) {
                    if (s != cycle.length() - 1) {
                        return _alphabet.toInt(cycle.charAt(s + 1));
                    }
                    return _alphabet.toInt(cycle.charAt(0));
                }
            }
        }
        return p;
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        char p = _alphabet.toChar(wrap(c));
        for (String cycle : _cycles) {
            for (int s = 0; s < cycle.length(); s++) {
                if (cycle.charAt(s) == p) {
                    if (s != 0) {
                        return _alphabet.toInt(cycle.charAt(s - 1));
                    }
                    return _alphabet.toInt(cycle.charAt(cycle.length() - 1));
                }
            }
        }
        return c;
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        if (!_alphabet.contains(p)) {
            throw error("Character " + p + " not in alphabet.");
        }
        for (String cycle : _cycles) {
            for (int s = 0; s < cycle.length(); s++) {
                if (cycle.charAt(s) == p) {
                    if (s != cycle.length() - 1) {
                        return cycle.charAt(s + 1);
                    }
                    return cycle.charAt(0);
                }
            }
        }
        return p;
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        if (!_alphabet.contains(c)) {
            throw error("Character " + c + " not in alphabet.");
        }
        for (String cycle : _cycles) {
            for (int s = 0; s < cycle.length(); s++) {
                if (cycle.charAt(s) == c) {
                    if (s != 0) {
                        return cycle.charAt(s - 1);
                    }
                    return cycle.charAt(cycle.length() - 1);
                }
            }
        }
        return c;
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        if (size() == 1) {
            return false;
        }
        int count = 0;
        for (String s : _cycles) {
            count += s.length();
        }
        return count == size();
    }

    /** Alphabet of this permutation. */
    private final Alphabet _alphabet;

    /** Cycles of this permutation. */
    private String[] _cycles;

}
