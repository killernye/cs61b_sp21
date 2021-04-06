package gh2;

import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

public class GuitarHero {
    public static final String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";


    public static void main(String[] args) {
        GuitarString[] notes = new GuitarString[37];

        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int index = keyboard.indexOf(key);
                if (index == -1) {
                    continue;
                }

                Double frequency = 440 * Math.pow(2, (index - 24) * 1.0 / 12);

                notes[index]  = new GuitarString(frequency);
                notes[index].pluck();
            }

             // compute the superposition of samples
            double sample = 0.0;
            for (GuitarString note: notes) {
                if (note != null) {
                    sample += note.sample();
                }
            }

            // play the sample on standard audio
            StdAudio.play(sample);

            // advance the simulation of each guitar string by one step
            for (GuitarString note: notes) {
                if (note != null) {
                    note.tic();
                }
            }
        }
    }

}
