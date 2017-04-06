package com.fossgalaxy.games.fireworks.cluster.cli;

import org.apache.commons.cli.*;

/**
 * Created by webpigeon on 06/04/17.
 */
public class GenericRunner {

    public static void main(String[] args) {
        try {
            CommandLine cli = parseArgs(args);
            if (cli.hasOption("--help")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("runner", getOptions());
                System.exit(0);
            }

            if (cli.hasOption("seed")) {
                long seed = Long.parseLong(cli.getOptionValue("seed"));
            }
        } catch (ParseException ex) {
            System.err.println("error: "+ex.getMessage());

            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("runner", getOptions());

            System.exit(1);
        }



    }

    public static Options getOptions(){
        Options options = new Options();
        options.addOption(new Option("help", "print this message"));
        options.addOption(Option.builder("seed").hasArg().type(Long.class).required().build());
        options.addOption(Option.builder("agents").hasArg(true).numberOfArgs(5).type(Long.class).required().build());
        options.addOption(Option.builder("nplayers").hasArg(true).type(Long.class).required().build());
        return options;
    }

    public static CommandLine parseArgs(String[] args) throws ParseException {
        CommandLineParser parser = new DefaultParser();


        return parser.parse(getOptions(), args);
    }


}
