package com;

import com.args.CountPatternArgs;
import com.args.ListPatternArgs;
import com.args.SearchPrimerArgs;
import com.common.Annotation;
import org.apache.commons.cli.*;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Set;

public class Main {
    static SearchPrimer searchPrimer = new SearchPrimer();
    static ListPattern listPattern = new ListPattern();
    static CountPattern countPattern = new CountPattern();

    public static void main(String[] args) throws Exception {
        System.setProperty("java.awt.headless", "true");

        if (args != null && args[0] != null && !"".equals(args[0])) {
            if (args[0].equals("searchPrimer")) {
                SearchPrimerArgs searchPrimeryArgs = parseSearchPrimer(args);
                if (searchPrimeryArgs != null) {
                    searchPrimer.searchPrimer(searchPrimeryArgs);
                }
            } else if (args[0].equals("listPattern")) {
                ListPatternArgs listPatternArgs = parseListPattern(args);
                if (listPatternArgs != null) {
                    listPattern.listPattern(listPatternArgs);
                }
            } else if (args[0].equals("countPattern")) {
                CountPatternArgs countPatternArgs = parseCountPattern(args);
                if (countPatternArgs != null) {
                    countPattern.countPattern(countPatternArgs);
                }
            } else {
                System.out.println("unrecognized command:" + args[0]);
            }
        } else { // show the help message

        }
    }

    private static SearchPrimerArgs parseSearchPrimer(String[] args) throws ParseException {
        Options options = new Options();
        Option helpOption = OptionBuilder.withLongOpt("help").withDescription("help").create("h");
        options.addOption(helpOption);
        Field[] fields = SearchPrimerArgs.class.getDeclaredFields();
        for(Field field : fields) {
            String annotation = field.getAnnotation(Annotation.class).value();
            Option option = null;
            if (field.getType().equals(boolean.class)) {
                option = OptionBuilder.withLongOpt(field.getName()).withDescription(annotation).create(field.getName());
            } else {
                option = OptionBuilder.withLongOpt(field.getName()).hasArg().withDescription(annotation).create(field.getName());
            }
            options.addOption(option);
        }

        BasicParser parser = new BasicParser();
        SearchPrimerArgs searchPrimerArgs = new SearchPrimerArgs();
        CommandLine commandLine = parser.parse(options, args);
        if (commandLine.getOptions().length > 0) {
            if (commandLine.hasOption('h')) {
                HelpFormatter helpFormatter = new HelpFormatter();
                helpFormatter.printHelp("Options", options);
                return null;
            } else {
                searchPrimerArgs.setMhapPathT(commandLine.getOptionValue("mhapPathT"));
                searchPrimerArgs.setMhapPathN(commandLine.getOptionValue("mhapPathN"));
                searchPrimerArgs.setCpgPath(commandLine.getOptionValue("cpgPath"));
                if (commandLine.hasOption("region")) {
                    searchPrimerArgs.setRegion(commandLine.getOptionValue("region"));
                }
                if (commandLine.hasOption("bedPath")) {
                    searchPrimerArgs.setBedPath(commandLine.getOptionValue("bedPath"));
                }
                if (commandLine.hasOption("outputDir")) {
                    searchPrimerArgs.setOutputDir(commandLine.getOptionValue("outputDir"));
                }
                if (commandLine.hasOption("tag")) {
                    searchPrimerArgs.setTag(commandLine.getOptionValue("tag"));
                }
                if (commandLine.hasOption("fLength")) {
                    searchPrimerArgs.setfLength(Integer.valueOf(commandLine.getOptionValue("fLength")));
                }
                if (commandLine.hasOption("rLength")) {
                    searchPrimerArgs.setrLength(Integer.valueOf(commandLine.getOptionValue("rLength")));
                }
                if (commandLine.hasOption("minT")) {
                    searchPrimerArgs.setMinT(Double.valueOf(commandLine.getOptionValue("minT")));
                }
                if (commandLine.hasOption("maxN")) {
                    searchPrimerArgs.setMaxN(Double.valueOf(commandLine.getOptionValue("maxN")));
                }
                if (commandLine.hasOption("minFC")) {
                    searchPrimerArgs.setMinFC(Double.valueOf(commandLine.getOptionValue("minFC")));
                }
                if (commandLine.hasOption("minInsertSize")) {
                    searchPrimerArgs.setMinInsertSize(Integer.valueOf(commandLine.getOptionValue("minInsertSize")));
                }
                if (commandLine.hasOption("maxInsertSize")) {
                    searchPrimerArgs.setMaxInsertSize(Integer.valueOf(commandLine.getOptionValue("maxInsertSize")));
                }
                if (commandLine.hasOption("minCov")) {
                    searchPrimerArgs.setMinCov(Integer.valueOf(commandLine.getOptionValue("minCov")));
                }
            }
        } else {
            System.out.println("The paramter is null");
        }

        return searchPrimerArgs;
    }

    private static ListPatternArgs parseListPattern(String[] args) throws ParseException {
        Options options = new Options();
        Option helpOption = OptionBuilder.withLongOpt("help").withDescription("help").create("h");
        options.addOption(helpOption);
        Field[] fields = ListPatternArgs.class.getDeclaredFields();
        for(Field field : fields) {
            String annotation = field.getAnnotation(Annotation.class).value();
            Option option = null;
            if (field.getType().equals(boolean.class)) {
                option = OptionBuilder.withLongOpt(field.getName()).withDescription(annotation).create(field.getName());
            } else {
                option = OptionBuilder.withLongOpt(field.getName()).hasArg().withDescription(annotation).create(field.getName());
            }
            options.addOption(option);
        }

        BasicParser parser = new BasicParser();
        ListPatternArgs listPatternArgs = new ListPatternArgs();
        CommandLine commandLine = parser.parse(options, args);
        if (commandLine.getOptions().length > 0) {
            if (commandLine.hasOption('h')) {
                HelpFormatter helpFormatter = new HelpFormatter();
                helpFormatter.printHelp("Options", options);
                return null;
            } else {
                listPatternArgs.setMhapPath(commandLine.getOptionValue("mhapPath"));
                String mhapPaths = commandLine.getOptionValue("mhapPath");
                if (commandLine.getArgs().length > 1) {
                    for (int i = 1; i < commandLine.getArgs().length; i++) {
                        mhapPaths += " " + commandLine.getArgs()[i];
                    }
                }
                // 去除重复的metrics
                String[] mhapPathsList = mhapPaths.split(" ");
                Set<Object> haoma = new LinkedHashSet<Object>();
                for (int i = 0; i < mhapPathsList.length; i++) {
                    haoma.add(mhapPathsList[i]);
                }
                String realMhapPaths = "";
                for (int i = 0; i < haoma.size(); i++) {
                    realMhapPaths += " " + haoma.toArray()[i];
                }
                listPatternArgs.setMhapPath(realMhapPaths.trim());

                listPatternArgs.setCpgPath(commandLine.getOptionValue("cpgPath"));
                listPatternArgs.setFPrimer(commandLine.getOptionValue("FPrimer"));
                listPatternArgs.setRPrimer(commandLine.getOptionValue("RPrimer"));
                if (commandLine.hasOption("outputDir")) {
                    listPatternArgs.setOutputDir(commandLine.getOptionValue("outputDir"));
                }
                if (commandLine.hasOption("tag")) {
                    listPatternArgs.setTag(commandLine.getOptionValue("tag"));
                }
            }
        } else {
            System.out.println("The paramter is null");
        }

        return listPatternArgs;
    }

    private static CountPatternArgs parseCountPattern(String[] args) throws ParseException {
        Options options = new Options();
        Option helpOption = OptionBuilder.withLongOpt("help").withDescription("help").create("h");
        options.addOption(helpOption);
        Field[] fields = CountPatternArgs.class.getDeclaredFields();
        for(Field field : fields) {
            String annotation = field.getAnnotation(Annotation.class).value();
            Option option = null;
            if (field.getType().equals(boolean.class)) {
                option = OptionBuilder.withLongOpt(field.getName()).withDescription(annotation).create(field.getName());
            } else {
                option = OptionBuilder.withLongOpt(field.getName()).hasArg().withDescription(annotation).create(field.getName());
            }
            options.addOption(option);
        }

        BasicParser parser = new BasicParser();
        CountPatternArgs countPatternArgs = new CountPatternArgs();
        CommandLine commandLine = parser.parse(options, args);
        if (commandLine.getOptions().length > 0) {
            if (commandLine.hasOption('h')) {
                HelpFormatter helpFormatter = new HelpFormatter();
                helpFormatter.printHelp("Options", options);
                return null;
            } else {
                countPatternArgs.setMhapPath(commandLine.getOptionValue("mhapPath"));
                String mhapPaths = commandLine.getOptionValue("mhapPath");
                if (commandLine.getArgs().length > 1) {
                    for (int i = 1; i < commandLine.getArgs().length; i++) {
                        mhapPaths += " " + commandLine.getArgs()[i];
                    }
                }
                // 去除重复的metrics
                String[] mhapPathsList = mhapPaths.split(" ");
                Set<Object> haoma = new LinkedHashSet<Object>();
                for (int i = 0; i < mhapPathsList.length; i++) {
                    haoma.add(mhapPathsList[i]);
                }
                String realMhapPaths = "";
                for (int i = 0; i < haoma.size(); i++) {
                    realMhapPaths += " " + haoma.toArray()[i];
                }
                countPatternArgs.setMhapPath(realMhapPaths.trim());

                countPatternArgs.setCpgPath(commandLine.getOptionValue("cpgPath"));
                countPatternArgs.setFPrimer(commandLine.getOptionValue("FPrimer"));
                countPatternArgs.setRPrimer(commandLine.getOptionValue("RPrimer"));
                countPatternArgs.setFPattern(commandLine.getOptionValue("FPattern"));
                countPatternArgs.setRPattern(commandLine.getOptionValue("RPattern"));
                if (commandLine.hasOption("outputDir")) {
                    countPatternArgs.setOutputDir(commandLine.getOptionValue("outputDir"));
                }
                if (commandLine.hasOption("tag")) {
                    countPatternArgs.setTag(commandLine.getOptionValue("tag"));
                }
            }
        } else {
            System.out.println("The paramter is null");
        }

        return countPatternArgs;
    }
}
