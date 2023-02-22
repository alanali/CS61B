import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/** A set of String values.
 *  @author Alana Li
 */
class ECHashStringSet implements StringSet {
    private int _size;
    private LinkedList<String>[] _list;
    private int _hashcode;

    public ECHashStringSet(int buckets) {
        _size = 0;
        _list = (LinkedList<String>[])  new LinkedList[buckets];
        for (int i = 0; i < buckets; i++) {
            _list[i] = new LinkedList<>();
        }
    }

    public ECHashStringSet() {
        this(5);
    }

    @Override
    public void put(String s) {
        _size += 1;
        if (s != null) {
            if (_size > _list.length * 5) {
                larger();
            }
            _hashcode = (s.hashCode() & 0x7fffffff) % _list.length;
            if (!_list[_hashcode].contains(s)) {
                _list[_hashcode].add(s);
            }
        }
    }

    public void larger() {
        int size = _size * 5;
        ECHashStringSet big = new ECHashStringSet(size);
        for (int i = 0; i < _list.length; i++) {
            for (String s : _list[i]) {
                big.put(s);
            }
        }
        _list = big._list;
    }

    @Override
    public boolean contains(String s) {
        if (s == null) {
            return false;
        }
        return _list[(s.hashCode() & 0x7fffffff) % _list.length].contains(s);
    }

    @Override
    public List<String> asList() {
        ArrayList<String> ret = new ArrayList<>();
        for (int i = 0; i < _list.length; i++) {
            if (_list[i] != null) {
                ret.addAll(_list[i]);
            }
        }
        return ret;
    }
}
