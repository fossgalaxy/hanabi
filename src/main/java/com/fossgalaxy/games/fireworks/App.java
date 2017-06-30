package com.fossgalaxy.games.fireworks;

import com.fossgalaxy.games.fireworks.ai.Agent;
import com.fossgalaxy.games.fireworks.ai.iggi.IGGIFactory;
import com.fossgalaxy.games.fireworks.ai.mcts.InfoSetStats;
import com.fossgalaxy.games.fireworks.ai.mcts.MCTS;
import com.fossgalaxy.games.fireworks.ai.mcts.MCTSPredictor;
import com.fossgalaxy.games.fireworks.ai.rule.Rule;
import com.fossgalaxy.games.fireworks.ai.rule.RuleSet;
import com.fossgalaxy.games.fireworks.utils.AgentUtils;
import com.fossgalaxy.games.fireworks.utils.GameUtils;
import com.fossgalaxy.games.fireworks.utils.SetupUtils;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Hello world!
 */
public class App {

    public static final String PREDICTOR_MCTS = "pmcts";
    public static final String IGGI_RISKY = "iggi_risky";
    public static final String MCTS = "mcts";
    public static final String PREDICTOR_MCTSND = "pmctsND";

    private static PrintStream ps;
    private static List<ISEnvolope> debugLog;

    //utility class - don't create instances of it
    private App() {

    }

    /**
     * Plays a series of games with a single agent mixed with another agent
     *
     * @param args Ignored
     */
    public static void main(String[] args) throws FileNotFoundException {

        File file = new File("uncertainty.csv");
        ps = new PrintStream(file);
        debugLog = new ArrayList<>();

        double sum = 0;
        int games = 0;
        System.out.println("Start");

        Random r = new Random();

        String[] agentPairedArr = new String[] {"iggi", "flawed", "internal", "vdb-paper"};

        for (int run = 0; run < 100; run++) {
            long seed = r.nextLong();
            for (String agentPaired : agentPairedArr) {

                String pairedStr = "";
                for (int i=0; i<5; i++) {
                    if (i!=0) {
                        pairedStr += "|";
                    }
                    pairedStr += agentPaired;
                }

                GameStats stats = playMixed(run, seed, "pmcts["+pairedStr+"]", agentPaired);
                sum += stats.score;
                games++;

            }
        }

        ps.close();

        //dump active game data to json for later analysis
        Gson gson = new Gson();
        PrintStream ps = new PrintStream(new File("is-debug.json"));
        ps.println(gson.toJson(debugLog));
        ps.close();

        if (games == 0) {
            return;
        }

        System.out.println("avg: " + sum / games);
    }

    public static String join(int[] inta) {
        String name = "";
        for (int i=0; i<inta.length; i++) {
            if (i!=0) {
                name += ",";
            }
            name += inta[i];
        }

       return name;
    }

    /**
     * Plays a game with the given agent
     *
     * @param agent The given agent to play the game
     * @return GameStats for the game.
     */
    public static GameStats playGame(String agent) {
        String[] names = new String[5];
        Agent[] players = new Agent[5];

        for (int i = 0; i < 5; i++) {
            names[i] = agent;
            players[i] = AgentUtils.buildAgent(names[i]);
        }

        GameStats stats = GameUtils.runGame("", null, SetupUtils.toPlayers(names, players));
        System.out.println("the agents scored: " + stats);
        return stats;
    }

    /**
     * Plays a mixed game with the agent under test and all other agents as the agent
     *
     * @param agentUnderTest The agent to be player 0
     * @param agent          The agent to make all the others
     * @return GameStats for the game
     */
    public static GameStats playMixed(int run, long seed, String agentUnderTest, String agent) {
        Random r = new Random(seed);
        int whereToPlace = r.nextInt(2);

        String[] names = new String[2];
        for (int i = 0; i < names.length; i++) {
            names[i] = whereToPlace == i ? agentUnderTest : agent;
        }

        Agent[] players = new Agent[2];
        for (int i = 0; i < names.length; i++) {
            players[i] = buildAgent(names[i], i, agent, names.length);
        }


        MCTSPredictor.statList.clear();

        GameStats stats = GameUtils.runGame("", seed, SetupUtils.toPlayers(names, players));

        int move=0;

        //record all game data for python scripts to look at
        ISEnvolope envolope = new ISEnvolope();
        envolope.run = run;
        envolope.playerPos = whereToPlace;
        envolope.seed = seed;
        envolope.agentTest = agentUnderTest;
        envolope.agentPaired = agent;
        envolope.stats = new ArrayList<>(MCTSPredictor.statList);
        envolope.result = stats;
        debugLog.add(envolope);

        //record data into CSV file for analysis
        for (InfoSetStats statObject : MCTSPredictor.statList) {
            ps.println(String.format("%d,%d,%d,%d,%s,%s,%d,%d,%s,%s",
                    run,
                    move,
                    whereToPlace,
                    seed,
                    agentUnderTest,
                    agent,
                    statObject.cardsInDeck,
                    statObject.uniqueCardsInDeck,
                    join(statObject.cardPossibilities),
                    join(statObject.cardUniques)
            ));
            move++;
        }

        System.out.println(MCTSPredictor.statList);

        System.out.println("the agents scored: " + stats);
        return stats;
    }

    /**
     * Build an agent
     *
     * @param name    The name the agent will believe it has
     * @param agentID The AgentID it will have
     * @param paired  Who it is paired with
     * @param size    The size of the game
     * @return The agent created
     */
    public static Agent buildAgent(String name, int agentID, String paired, int size) {
        switch (name) {
            case PREDICTOR_MCTS:
            case PREDICTOR_MCTSND:
                Agent[] agents = AgentUtils.buildPredictors(agentID, size, paired);
                if (name.contains("ND")) {
                    return new MCTSPredictor(agents, 50_000, 100, 100);
                }
                return new MCTSPredictor(agents);
            default:
                return AgentUtils.buildAgent(name);
        }
    }

    /**
     * Build an agent
     *
     * @param name    The name the agent will believe it has
     * @param agentID The AgentID it will have
     * @param paired  Who it is paired with
     * @return The agent created
     */
    public static Agent buildAgent(String name, int agentID, String[] paired) {
        switch (name) {
            case PREDICTOR_MCTS:
            case PREDICTOR_MCTSND:
                Agent[] agents = AgentUtils.buildPredictors(agentID, paired);
                if (name.contains("ND")) {
                    return new MCTSPredictor(agents, 50_000, 100, 100);
                }
                return new MCTSPredictor(agents);
            default:
                return AgentUtils.buildAgent(name);
        }
    }

    public static Agent buildAgent(String name, int agentID, int[] model){
        switch (name){
            case PREDICTOR_MCTS:
            case PREDICTOR_MCTSND:
                Agent[] agents = new Agent[5];
                for(int i = 0; i < agents.length; i++){
                    if(i != agentID) {
                        agents[i] = AgentUtils.buildAgent(model);
                    }
                }
                if(name.contains("ND")){
                    return new MCTSPredictor(agents, 50_000, 100, 100);
                }
                return new MCTSPredictor(agents);
            default:
                return AgentUtils.buildAgent(name);
        }
    }


    /**
     * Allows for creating MCTS specifically with some fields
     *
     * @param name         The name of the agent
     * @param roundLength  The round length to use for MCTS
     * @param rolloutDepth The rollout depth to use for MCTS
     * @param treeDepth    The tree depth to use for MCTS
     * @return The Agent
     */
    public static Agent buildAgent(String name, int roundLength, int rolloutDepth, int treeDepth) {
        return MCTS.equals(name) ? new MCTS(roundLength, rolloutDepth, treeDepth) : AgentUtils.buildAgent(name);
    }

    /**
     * Allows for creating Predictor MCTS with some fields
     *
     * @param name         The name for the agent
     * @param agentID      The agent id
     * @param paired       Who the agent is paired with
     * @param size         The size of the game
     * @param roundLength  The round length to use for MCTS
     * @param rolloutDepth The rollout depth to use for MCTS
     * @param treeDepth    The tree depth to use for MCTS
     * @return The agent
     */
    public static Agent buildAgent(String name, int agentID, String paired, int size, int roundLength, int rolloutDepth, int treeDepth) {
        if (!PREDICTOR_MCTS.equals(name)) {
            return AgentUtils.buildAgent(name);
        }
        Agent[] agents = new Agent[size];
        for (int i = 0; i < size; i++) {
            if (i == agentID) {
                agents[i] = null;
            }
            //TODO is this ever paired with MCTS? if not this should be AgentUtils.buildAgent(agentID, size, paired)
            agents[i] = buildAgent(paired, roundLength, rolloutDepth, treeDepth);
        }
        return new MCTSPredictor(agents);
    }

    /**
     * Builds a risky agent with a given threshold
     *
     * @param name      The name for the agent
     * @param threshold The threshold to give to the agent
     * @return The agent
     */
    public static Agent buildAgent(String name, double threshold) {
        return IGGI_RISKY.equals(name) ? IGGIFactory.buildRiskyPlayer(threshold) : AgentUtils.buildAgent(name);
    }


}
