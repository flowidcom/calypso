package com.flowid.refd.shell;

/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

/**
 * Shell skeleton that can be used in utility modules to test functionality and provide operational
 * support
 */
public class JShell {
    private static final Logger logger = LoggerFactory.getLogger(JShell.class);

    // The following are regular name characters. They appear in option names and property names
    static final String NAME_CHAR = "[a-z]|[A-Z]|[0-9]|_|\\.|\\-";
    // Option names for options with no arguments start with upper case or digits
    static final String OPTION_NAME_NO_ARG_REGEX = "[A-Z0-9](" + NAME_CHAR + ")*";
    // Option names for options with arguments start with lower case
    static final String OPTION_NAME_WITH_ARG_REGEX = "[a-z](" + NAME_CHAR + ")*";
    // Properties have name=value definitions. There are FOUR groups in this definition.
    // The name is in group 1 and the value in the group 3!
    static final String PROP_DEF_REGEX = "((" + NAME_CHAR + ")+)=((" + NAME_CHAR + ")+)";
    static final String COMMAND_NAME_REGEX = "\\w+";

    // Precompiled patterns
    static final Pattern OPTION_NAME_NO_ARG_PATTERN = Pattern.compile("-"
            + OPTION_NAME_NO_ARG_REGEX);
    static final Pattern OPTION_NAME_WITH_ARG_PATTERN = Pattern.compile("-"
            + OPTION_NAME_WITH_ARG_REGEX);
    static final Pattern PROP_DEF_PATTERN = Pattern.compile("-D" + PROP_DEF_REGEX);
    static final Pattern COMMAND_NAME_PATTERN = Pattern.compile(COMMAND_NAME_REGEX);

    // Methods that accept a list of arguments (no options)
    HashMap<String, Object> cmdObjects = new HashMap<String, Object>();
    HashMap<String, Method> cmdMethods = new HashMap<String, Method>();

    // code state used in the automata for indicating an error
    private static final int ERROR_STATE = 999;

    /**
     * Iterate on the methods directly from this class and add the methods the implement the
     * signature int cmd(String ... args) or int cmd(HashMap options, String ... args)
     * 
     * @param clasz
     */
    public void addCommandsFrom(Class<?> clasz) {
        boolean isInternalCommand = (clasz == this.getClass());
        try {
            for (Method method : clasz.getDeclaredMethods()) {
                String cmdName = getCommandName(method);
                if (cmdName != null) {
                    Method existingMethod = cmdMethods.get(cmdName);
                    if (existingMethod != null) {
                        logger.error("A command with the name {} is already defined in class {}",
                                cmdName, existingMethod.getClass());
                    } else {
                        cmdMethods.put(cmdName, method);
                        // for internal commands, set the command object
                        if (isInternalCommand) {
                            cmdObjects.put(cmdName, this);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Exception creating commands for class: {} - {}", clasz.getName(), e);
            logger.debug("Details: ", e);
        }
    }

    /**
     * Check if a method qualifies to be a command. Must have the annotation @Command and the
     * parameters must be either String ... args or HashMap<String, String> props, String ... args
     * 
     * @param method method tested
     * @return true if method qualifies as a command.
     */
    public String getCommandName(Method method) {
        Class<?>[] paramTypes = method.getParameterTypes();

        Command annot = method.getAnnotation(Command.class);
        // if there is no annotation, this is not a command
        if (annot == null) {
            return null;
        }

        // check the method signature
        boolean commandMethodFound = false;

        if (paramTypes.length == 0) {
            // this is a command without arguments
            commandMethodFound = true;
        }
        if (paramTypes.length == 1) {
            // we are looking for operations that have the parameter String[]
            Class<?> type1 = paramTypes[0];
            if (type1.isArray() && type1.getComponentType() == String.class) {
                commandMethodFound = true;
            }
        }
        if (paramTypes.length == 2) {
            Class<?> type1 = paramTypes[0];
            Class<?> type2 = paramTypes[1];

            commandMethodFound = (type1 == HashMap.class);
            commandMethodFound &= (type2.isArray() && (type2.getComponentType() == String.class));
        }

        if (!commandMethodFound) {
            return null;
        }

        String cmdName = annot.name().trim();

        // if the name is not provided in the annotation, otherwise use the method name
        if (cmdName.length() == 0) {
            cmdName = method.getName();
        }
        return cmdName;
    }

    /**
     * Iterate on the classes from this package and add the commands
     * 
     * @param pkg
     */
    public void addCommandsFrom(Package pkg) {
        final ClassLoader cld = Thread.currentThread().getContextClassLoader();
        try {
            Enumeration<URL> resources = cld.getResources(pkg.getName().replace('.', '/'));
            for (URL url = null; resources.hasMoreElements()
                    && ((url = resources.nextElement()) != null);) {
                String path = url.getPath();
                File[] files = (new File(path)).listFiles();
                for (File f : files) {
                    if (f.getName().endsWith(".class")) {
                        String simpleClassName =
                                f.getName().substring(0, f.getName().length() - ".class".length());
                        String className = pkg.getName() + "." + simpleClassName;
                        Class<?> clasz = Class.forName(className);
                        addCommandsFrom(clasz);
                    }
                    logger.debug("Adding commands from class: " + f.getPath());
                }
            }
        } catch (Exception e) {
            logger.error("Error while getting the classes from package {}", pkg.getName());
            logger.debug("Details: ", e);
        }
    }

    /**
     * Execute a command
     * 
     * @param command
     * @param options
     * @param args
     * @return 0 - success, >0 - error
     */
    private int execute(String command, HashMap<String, String> options, List<String> args) {
        Method cmdMethod = cmdMethods.get(command);
        if (cmdMethod == null) {
            // no implementation found for the command
            logger.error("Command not found {}", command);
            return 1;
        }

        // the command was found, invoke the method
        try {
            Object cmdObject = cmdObjects.get(command);
            if (cmdObject == null) {
                Class<?> clasz = cmdMethod.getDeclaringClass();
                logger.debug("Command class: {}", clasz.getName());
                cmdObject = clasz.newInstance();
                cmdObjects.put(command, cmdObject);
            }
            int paramCount = cmdMethod.getParameterTypes().length;
            if (paramCount == 0) {
                if (args.size() > 0) {
                    throw new IllegalArgumentException("Command " + command + " does not accept arguments.");
                }
                cmdMethod.invoke(cmdObject, new Object[] {});
            } else if (paramCount == 1) {
                String[] argsAsArray = new String[args.size()];
                args.toArray(argsAsArray);
                try {
                    cmdMethod.invoke(cmdObject, new Object[] {argsAsArray});
                } catch (Exception e) {
                    logger.error("Error: {}", e.getCause().getMessage());
                    logger.debug("Details: ", e);
                }
            } else if (paramCount == 2) {
                String[] argsAsArray = new String[args.size()];
                args.toArray(argsAsArray);
                try {
                    cmdMethod.invoke(cmdObject, new Object[] {options, argsAsArray});
                } catch (Exception e) {
                    logger.error("Error: {}", e.getCause().getMessage());
                    logger.debug("Details: ", e);
                }
            } else {
                // method not found
                logger.error("Invalid command, no method found.");
                return 1;
            }
        } catch (Exception e) {
            logger.error("Error while executing the command {} - {}", command, e.getMessage());
            logger.debug("Details: ", e);
            return 2;
        }
        return 0;
    }

    /**
     * Execute a command
     * 
     * @param args command name followed by the arguments
     * @return
     */
    public int execute(String[] args) {
        HashMap<String, String> props = new HashMap<String, String>();
        HashMap<String, String> options = new HashMap<String, String>();
        List<String> cmd = new ArrayList<String>();

        List<String> errors = new ArrayList<String>();
        boolean success = parseCommandProperties(Arrays.asList(args), props, cmd, errors);
        if (!success) {
            System.err.println("Error in parsing the command line.");
            for (String error : errors) {
                System.err.println(error);
            }
            return 1;
        }

        // add the properties to SytemProperties
        for (Entry<String, String> em : props.entrySet()) {
            System.setProperty(em.getKey(), em.getValue());
        }

        // Parsed successfully the command line
        List<String> commandHolder = new ArrayList<String>();
        List<String> cmdArgs = new ArrayList<String>();

        // If there is no command in the command line, then run interactively
        if (cmd.size() == 0) {
            // working in shell mode
            Scanner console = new Scanner(System.in);
            System.out.println(">");
            while (console.hasNextLine()) {
                String line = console.nextLine().trim();
                if (line.length() == 0) {
                    // ignore empty lines
                    continue;
                }
                // break the input line in tokens
                cmd = Arrays.asList(line.split("\\s+"));
                if ("exit".equals(cmd.get(0))) {
                    break;
                }
                // clear the holders between commands
                success = parseCommand(cmd, commandHolder, options, cmdArgs, errors);
                if (!success) {
                    for (String error : errors) {
                        System.err.println(error);
                    }
                }
                int retCode = execute(commandHolder.get(0), options, cmdArgs);
                logger.debug("Return code: {}", retCode);
            }
            console.close();
            return 0;
        } else { // the command is in the command line
            success = parseCommand(cmd, commandHolder, options, cmdArgs, errors);
            if (!success) {
                System.err.println("Errors while parsing the command line");
                for (String error : errors) {
                    System.err.println(error);
                }
                return 2;
            }
            int retCode = execute(commandHolder.get(0), options, cmdArgs);
            return retCode;
        }
    }

    /**
     * Parse the system properties shared by all the commands. The properties are saved in the
     * hash-map props and the rest of the command is saved in the list cmd.
     * 
     * This had to be done this way because the script generated by assembler-maven-plugin does not
     * provide an easy way to define the system properties.
     */
    boolean parseCommandProperties(final List<String> args, HashMap<String, String> props,
            List<String> cmd, List<String> errors) {
        props.clear();
        cmd.clear();
        errors.clear();
        int index = 0;
        while (index < args.size()) {
            String token = args.get(index);
            if (token.startsWith("-D")) {
                Matcher propDefMatcher = PROP_DEF_PATTERN.matcher(token);
                if (propDefMatcher.matches()) {
                    String propName = propDefMatcher.group(1);
                    String propValue = propDefMatcher.group(3);
                    props.put(propName, propValue);
                    // advance the cursor
                    index++;
                } else {
                    errors.add(String.format("Invalid property '%s' at index %d.", token, index));
                    return false;
                }
            } else {
                // we finished processing properties
                break;
            }
        }

        // the rest of the arguments are saved in the list cmd
        while (index < args.size()) {
            cmd.add(args.get(index));
            index++;
        }
        return true;
    }

    /**
     * Splits the command parameters in command name, options and arguments
     * 
     * @param args command arguments
     * @param commandHolder wrapper around the string that contains the command to execute
     * @param cmdArgs command arguments
     * 
     *        This method implements a simple lexical parser cmd-line ::= command? option* cmdArg*
     *        option::= option-name option-value?
     */
    boolean parseCommand(final List<String> args, List<String> commandHolder,
            HashMap<String, String> options, List<String> cmdArgs, List<String> errors) {
        // clear the structures between commands as they could have been reused
        commandHolder.clear();
        options.clear();
        cmdArgs.clear();
        errors.clear();

        int state = 1;
        int index = 0;
        while (index < args.size() && state != ERROR_STATE) {
            String token = args.get(index);
            switch (state) {
                case 1: // we are processing the command
                    if (token.matches(COMMAND_NAME_REGEX)) {
                        commandHolder.add(token);
                        index++;
                        state = 2;
                    } else {
                        String error =
                                "Unexpected token '" + token + "' at index " + index
                                        + " in state 0.";
                        error += " Expected letters or characters.";
                        errors.add(error);
                        state = ERROR_STATE;

                    }
                    break;

                case 2: // we are expecting options
                    // options without value
                    if (OPTION_NAME_NO_ARG_PATTERN.matcher(token).matches()) {
                        options.put(token.substring(1, token.length()), "");
                        index++;
                    } else if (OPTION_NAME_WITH_ARG_PATTERN.matcher(token).matches()) {
                        String optionName = token.substring(1, token.length());
                        if (index == args.size() - 1) {
                            errors.add("Missing value for option " + token + ".");
                            state = ERROR_STATE;
                        } else { // advance the index to get the option value
                            index++;
                            String optionValue = args.get(index);
                            options.put(optionName, optionValue);
                        }
                        index++;
                    } else {
                        state = 3; // reading command arguments
                    }
                    break;

                case 3: // we are expecting command arguments
                    cmdArgs.add(token);
                    index++;
                    break;

                default: // unknown state
                    errors.add("Unknown state: " + state);
                    return false;
            }
        }

        return state != ERROR_STATE;
    }

    @Command(name = "commands")
    public int listCommands(String... args) throws IllegalArgumentException {
        SortedSet<String> keys = new TreeSet<String>(cmdMethods.keySet());
        for (String cmdName : keys) {
            Method m = cmdMethods.get(cmdName);
            String methodName = m.getName();
            String cmdClass = m.getDeclaringClass().getSimpleName();
            int paramsCount = m.getParameterTypes().length;
            if (paramsCount == 0) {
                System.out.println(String.format("%s - %s#%s", cmdName, cmdClass, methodName));
            } else if (paramsCount == 1) {
                System.out.println(String
                        .format("%s <args> - %s#%s", cmdName, cmdClass, methodName));
            } else if (paramsCount == 2) {
                System.out.println(String.format("%s <options> <args> - %s#%s", cmdName, cmdClass,
                        methodName));
            } else {
                throw new IllegalArgumentException(
                        "Command associated with a method with more than 2 parameters.");
            }
        }

        return 0;
    }

    @Command(name = "logLevel")
    public int setLogLevel(String... args) {
        String name = "com.flowid";

        final LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        ch.qos.logback.classic.Logger log = lc.getLogger(name);
        if (log == null) {
            logger.error("Could not find a logger wit the name " + name);
            return 1;
        }

        if (args.length == 0) {
            System.out.println("Log level: " + log.getLevel().toString());
        } else if (args.length == 1) {
            String level = args[0].toUpperCase();
            Level l = Level.valueOf(level);
            if (l == null) {
                logger.error("IParamalid log level " + level);
                return 1;
            }
            log.setLevel(l);
            System.out.println("Log level set to " + l);
        } else {
            throw new IllegalArgumentException("Invalid number of parameters - " + args.length);
        }
        return 0;
    }

    @Command(name = "env")
    public int listEnv() throws IllegalArgumentException {
        SortedSet<String> keys = new TreeSet<String>(System.getProperties().stringPropertyNames());
        for (String key : keys) {
            System.out.println(key + "=" + System.getProperty(key));
        }
        return 0;
    }

    static public void main(String... args) {
        JShell shell = new JShell();
        shell.addCommandsFrom(JShell.class.getPackage());
        shell.execute(args);
    }

    static public void assertTrue(boolean condition, String message) throws Exception {
        if (!condition) {
            throw new Exception("Assertion failed: " + message);
        }
    }
}
