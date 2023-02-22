package enigma;

import static enigma.EnigmaException.*;
import java.util.HashMap;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Alana Li
 */
class Alphabet {

    /** A new alphabet containing CHARS. The K-th character has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        _alphas = new HashMap<Integer, Character>();
        for (int i = 0; i < chars.length(); i++) {
            char c = chars.charAt(i);
            if ((c == '*') || (c == '(') || (c == ')')) {
                throw error("Illegal character " + c);
            }
            if (_alphas.containsValue(c)) {
                throw error("Duplicate character " + c);
            }
            _alphas.put(i, c);
        }
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return _alphas.size();
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        return _alphas.containsValue(ch);
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        return _alphas.get(index);
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        int index = 0;
        if (!(contains(ch))) {
            throw error("Character not in alphabet.");
        }
        for (Integer key : _alphas.keySet()) {
            if (_alphas.get(key).equals(ch)) {
                index = key;
            }
        }
        return index;
    }

    /** Hashmap of letters to corresponding index. */
    private HashMap<Integer, Character> _alphas;

}
