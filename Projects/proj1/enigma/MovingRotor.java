package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Alana Li
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
    }

    @Override
    boolean atNotch() {
        for (int i = 0; i < _notches.length(); i++) {
            if (this.setting() == alphabet().toInt(_notches.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    @Override
    boolean rotates()  {
        return true;
    }

    @Override
    void advance() {
        set(permutation().wrap(this.setting() + 1));
    }

    @Override
    String notches() {
        return _notches;
    }

    /** Notch locations in rotor. */
    private String _notches;

}
