package com.fossgalaxy.games.fireworks.cluster.oneshot;

import com.fossgalaxy.games.fireworks.utils.SetupUtils;

import java.util.Random;

/**
 * Experiment to see how the exploration constant changes MCTS performance with a fixed budget.
 *
 * Created by webpigeon on 01/08/17.
 */
public class ChangeWorlds {
    private static final int ITR_BUDGET = 10_000;

    public static void main(String[] args) {
        String[] agentsPaired = SetupUtils.getPairedNames();
        int numSeeds = SetupUtils.getSeedCount();

        printMatchups(agentsPaired, numSeeds);
    }

    static void printMatchups(String[] agentsPaired, int numSeeds) {

        //allow generation of known seeds (useful for comparisons between pure and mixed games)
        Random r;
        String metaSeed = System.getenv("FIREWORKS_META_SEED");
        if (metaSeed != null) {
            r = new Random(Long.parseLong(metaSeed));
        } else {
            r = new Random();
        }


        for (int seedID = 0; seedID < numSeeds; seedID++) {
            long seed = r.nextLong();

            for (int worlds=0; worlds<=10_000; worlds += 500) {
                    String agentUnderTest = String.format("fixedMcts[%d:%d]", ITR_BUDGET, worlds==0?1:worlds);

                    for (String agentPaired : agentsPaired) {
                        System.out.println(String.format("%s %s %d", agentUnderTest, agentPaired, seed));
                    }
                }
            }
        }
}
