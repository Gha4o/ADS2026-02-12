package by.it.group551002.kuzmenia.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/*
 * ИДЕЯ РЕШЕНИЯ
 *
 * Все элементы лежат в обычном массиве data, и этот массив ВСЕГДА отсортирован по возрастанию.
 * Пример: после add(30), add(10), add(20) массив выглядит так:  [10, 20, 30, null, null, ...]
 *                                                               (занято size = 3 ячейки)
 *
 * Благодаря сортировке нам нужны всего две идеи:
 *
 * 1) БИНАРНЫЙ ПОИСК - быстро находим, где в массиве должен стоять элемент.
 *    Берём середину, сравниваем: если искомый элемент больше - ищем только в правой половине,
 *    иначе - только в левой. Каждый шаг отбрасывает половину массива.
 *    (подробный разбор на примере - у метода findPosition)
 *
 * 2) СДВИГ ЭЛЕМЕНТОВ - чтобы вставить элемент в середину, раздвигаем соседей вправо,
 *    чтобы удалить - задвигаем дырку соседями влево.
 *
 * ВАЖНО: чтобы сравнивать элементы "больше / меньше", они должны быть Comparable
 * (String, Integer и т.п. - все такие). Как и в настоящем TreeSet, элемент null не разрешён.
 * Два элемента считаются одинаковыми, если compareTo(...) вернул 0.
 */
public class MyTreeSet<E> implements Set<E> {

    private Object[] data = new Object[16];
    private int size = 0;

    /////////////////////////////////////////////////////////////////////////
    //////                      Вспомогательные методы                 //////
    /////////////////////////////////////////////////////////////////////////

    @SuppressWarnings("unchecked")
    private static int compare(Object a, Object b) {
        return ((Comparable<Object>) a).compareTo(b);
    }

    private int findPosition(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        int left = 0;
        int right = size;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (compare(data[mid], o) < 0) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }

    private boolean isFoundAt(int pos, Object o) {
        return pos < size && compare(data[pos], o) == 0;
    }

    private void grow() {
        Object[] bigger = new Object[data.length * 2];
        for (int i = 0; i < size; i++) {
            bigger[i] = data[i];
        }
        data = bigger;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                result.append(", ");
            }
            result.append(data[i]);
        }
        return result.append("]").toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            data[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E e) {
        int pos = findPosition(e);
        if (isFoundAt(pos, e)) {
            return false;
        }
        if (size == data.length) {
            grow();
        }

        for (int i = size; i > pos; i--) {
            data[i] = data[i - 1];
        }
        data[pos] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int pos = findPosition(o);
        if (!isFoundAt(pos, o)) {
            return false;
        }

        for (int i = pos; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        size--;
        data[size] = null;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        return isFoundAt(findPosition(o), o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object x : c) {
            if (!contains(x)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E x : c) {
            if (add(x)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == this) {
            boolean changed = size > 0;
            clear();
            return changed;
        }
        boolean changed = false;
        for (Object x : c) {
            if (remove(x)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == this) {
            return false;
        }
        int kept = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(data[i])) {
                data[kept] = data[i];
                kept++;
            }
        }
        boolean changed = kept != size;
        for (int i = kept; i < size; i++) {
            data[i] = null;
        }
        size = kept;
        return changed;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }
}