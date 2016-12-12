package com.fossgalaxy.games.fireworks.cluster;

import com.fossgalaxy.games.fireworks.App;
import com.fossgalaxy.games.fireworks.App2Csv;
import com.fossgalaxy.games.fireworks.ai.Agent;
import com.fossgalaxy.games.fireworks.utils.AgentUtils;
import com.fossgalaxy.games.fireworks.utils.SetupUtils;

import java.util.Random;
import java.io.PrintStream;

/**
 * A runner capable of playing the games for every legal hand size
 * <p>
 * This runner paired with
 */
public class MixedAgentGameSingle {
    private static final String SEPERATOR = "###########################";

    private MixedAgentGameSingle() {

    }

    public static void main(String[] args) {

        int repeats = SetupUtils.getRepeatCount();

        //arguments for script
        String agentUnderTest = args[0];
        String agentPaired = args[1];
        long seed = Long.parseLong(args[2]);

        Random random = new Random(seed);

        String taskId = System.getenv("SGE_TASK_ID");
        PrintStream log = System.err;

        for (int run = 0; run < repeats; run++) {
            for (int nPlayers = 2; nPlayers <= 5; nPlayers++) {
                int agentUnderTestIndex = random.nextInt(nPlayers);

                //figure out if we need to generate a taskID or if one was provided by the runner
                String gameID;
                if (taskId == null) {
                    gameID = String.format("%d-%s-%s-%d-%d", seed, agentUnderTest, agentPaired, nPlayers, run);
                } else {
                    //the same taskId can correspond to multiple games - this helps us track what run the taskID was for
                    gameID = String.format("%s-%d-%d", taskId, nPlayers, run);
                }

                System.err.println(SEPERATOR);
                System.err.println("# begin game " + gameID);
                System.err.println(SEPERATOR);

                Agent[] agents = new Agent[nPlayers];
                String[] agentStr = new String[5];

                //generate agent under test
                agents[agentUnderTestIndex] = App.buildAgent(agentUnderTest, 0, agentPaired, nPlayers);
                agentStr[agentUnderTestIndex] = agentUnderTest;
                for (int i = 0; i < nPlayers; i++) {
                    if(i == agentUnderTestIndex){
                        continue;
                    }
                    agents[i] = AgentUtils.buildAgent(agentPaired);
                    agentStr[i] = agentPaired;
                }

                App2Csv.playGameErrTrace(gameID, agentStr, seed, agents);

                System.err.println(SEPERATOR);
                System.err.println("# end game " + gameID);
                System.err.println(SEPERATOR);
            }
        }
    }

}
