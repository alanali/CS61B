package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a reflector in the enigma.
 *  @author Alana Li
 */
class Reflector extends FixedRotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is PERM. */
    Reflector(String name, Permutation perm) {
        super(name, perm);
        this.set(0);
    }

    @Override
    void set(int posn) {
        if (posn != 0) {
            throw error("Reflector has only one position");
        }
    }

    @Override
    boolean reflecting() {
        return true;
    }

    @Override
    public String toString() {
        return "Reflector " + name();
    }
}
