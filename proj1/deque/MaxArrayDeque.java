package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T>{
    private final Comparator<T> comparator;
    public MaxArrayDeque(Comparator<T> c){
        this.comparator = c;
    }
    public T max(){
        if(items.length == 0){
            return null;
        }
        T item = items[front];
        for(int i = front ; i < end; i += 1){
            if(comparator.compare(item,items[i]) == -1){
                item = items[i];
            }
        }
        return item;
    }
}
