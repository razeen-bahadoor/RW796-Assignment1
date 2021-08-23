

import java.util.Arrays;

public class Stack {

    private int topOfStack;
    // index of the base of the run
    private int[] base;
    // length of the run
    private int[] length;

    public Stack(int size) {
        this.base = new int[size];
        this.length = new int[size];

    }

    /**
     * Push a run onto the stack
     *
     * @param base   index of the base of the run
     * @param length length of the run (including starting position)
     */

    public void pushRun(int base, int length) {
        ensureCapacity();
        this.base[topOfStack] = base;
        this.length[topOfStack] = length;
        topOfStack++;
    }


    /**
     * Size of the stack
     *
     * @return
     */
    public int size() {
        return this.topOfStack;
    }

    /**
     * Decrease size of stack
     */
    public void decrementSize() {
        this.topOfStack--;
    }

    /**
     * Return index of the to of the stack
     *
     * @return top of stack.
     */
    public int top() {
        return this.topOfStack;
    }

    /**
     * Return length of the given run
     *
     * @param index of run
     * @return length of run at index
     */
    public int length(int index) {
        return this.length[index];
    }

    /**
     * Get the starting index of a run.
     *
     * @param index of run
     * @return base of the run
     */
    public int base(int index) {
        return base[index];
    }

    /**
     * Update the length of a run, useful when 2 runs are merged.
     *
     * @param index  of the run to update
     * @param length new length.
     */
    public void updateLength(int index, int length) {
        this.length[index] = length;
    }

    /**
     * Shift a run.
     *
     * @param from src index
     * @param to   dest index
     */
    public void shift(int from, int to) {
        this.base[to] = this.base[from];
        this.length[to] = this.length[from];

    }

    /**
     * Ensure the stack has sufficient capacity , dynamically doubles  the stack, just a sanity check
     */
    private void ensureCapacity() {

        if (topOfStack == base.length - 1) {
            //  calculate new size
            int new_size = base.length << 1;
            base = Arrays.copyOf(this.base, new_size);
            length = Arrays.copyOf(length, new_size);
        }
    }

}
