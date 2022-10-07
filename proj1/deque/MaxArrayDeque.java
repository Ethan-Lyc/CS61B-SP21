package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T>{
    private final Comparator<T> comparator;
    public MaxArrayDeque(Comparator<T> c){
        this.comparator = c;
    }
    public T max(){
        if(size == 0){
            return null;
        }
        T item = items[nextFirst + 1];
        for(int i = nextFirst + 1; i < nextLast; i += 1){
            if(comparator.compare(items[i],item) == 1){
                item = items[i];
            }
        }
        return item;
    }
    public T max(Comparator<T> c){
        if(size == 0){
            return null;
        }
        T item = items[nextFirst + 1];
        for(int i = nextFirst + 1; i < nextLast; i += 1){
                if(c.compare(items[i],item) == 1){
                    item = items[i];
                }
        }
        return item;
    }
}
