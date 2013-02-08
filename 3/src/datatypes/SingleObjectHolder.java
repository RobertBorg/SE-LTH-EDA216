package datatypes;

import java.util.ArrayList;

public class SingleObjectHolder<E> {
	public int key;
	public ArrayList<E> values;
	
	public SingleObjectHolder(int key, E... value) {
		this.key = key;
		values = new ArrayList<E>();
		for(E e : value) {
			values.add(e);
		}
	}
	
	public E getFirstValue() {
		return values.get(0);
	}
}
