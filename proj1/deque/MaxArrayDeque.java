package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private final Comparator<T> comparator;
    public MaxArrayDeque(Comparator<T> c) {
        this.comparator = c;
    }
    public T max() {
        if (this.size() == 0) {
            return null;
        } else {
            T item = this.get(0);
            for (int i = 0; i < this.size(); i += 1) {
                if (comparator.compare(this.get(i), item) > 0) {
                    item = this.get(i);
                }
            }
            return item;
        }
    }
    public T max(Comparator<T> c) {
        if (this.size() == 0) {
            return null;
        }
        T item = this.get(0);
        for (int i = 1; i < size(); i += 1) {
            if (c.compare(get(i), item) > 0) {
                item = get(i);
            }
        }
        return item;
    }
}
