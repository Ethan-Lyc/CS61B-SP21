package gh2;

// TODO: uncomment the following import once you're ready to start this portion
// import deque.Deque;
// TODO: maybe more imports

import deque.*;

import java.util.Random;


//Note: This file will not compile until you complete the Deque implementations
public class GuitarString {
    /** Constants. Do not change. In case you're curious, the keyword final
     * means the values cannot be changed at runtime. We'll discuss this and
     * other topics in lecture on Friday. */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */
    // TODO: uncomment the following line once you're ready to start this portion
     private Deque<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {

        int size = (int)Math.round(SR / frequency);
        buffer = new LinkedListDeque<>();
        for(int i = 0; i < size ; i += 1){
            buffer.addFirst((double) 0);
        }

    }


    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {

        int size = buffer.size();
        for(int i = 0; i < size; i += 1){
            buffer.removeFirst();
            buffer.addLast(Math.random() - 0.5);
        }
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm.
     */
    public void tic() {

        Double front = buffer.removeFirst();
        Double next = buffer.get(0);
        Double cur = DECAY * (front + next) / 2;
        buffer.addLast(cur);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        return buffer.get(0);
    }
}
