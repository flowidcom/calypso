package com.flowid.shell;

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
import java.util.HashMap;

/**
 * A fluent API to validate shell parameters.
 */
public class ParamsValidator {
    // arguments that are validated
    private String[] args;

    // options that are validated
    private HashMap<String, String> options;

    // to allow validation of individual arguments, this is the index of the argument
    private int argIndex = -1;

    /**
     * Initialize the validator passing a list of arguments
     * 
     * @param args
     * @return
     */
    static public ParamsValidator validate(String[] args) {
        ParamsValidator pu = new ParamsValidator();
        pu.args = args;
        return pu;
    }

    /**
     * Initialize the validator passing a list of options
     * 
     * @param options
     * @return
     */
    static public ParamsValidator validate(HashMap<String, String> options) {
        ParamsValidator pu = new ParamsValidator();
        pu.options = options;
        return pu;
    }

    /**
     * Validate that an option is present
     * 
     * @param option
     * @return
     */
    public ParamsValidator mandatory(String option) {
        String val = getOption(option);
        if (val == null) {
            throw new IllegalArgumentException("Missing required option " + option);
        }
        return this;
    }

    /**
     * Validate the number of arguments
     * 
     * @param n
     * @return
     * @throws IllegalArgumentException
     */
    public ParamsValidator atLeast(int n) throws IllegalArgumentException {
        if (args.length < n) {
            throw new IllegalArgumentException("Expected at least " + n + " arguments");
        }
        return this;
    }

    /**
     * The number of arguments must be exactly n
     * 
     * @param n
     * @return
     * @throws IllegalArgumentException
     */
    public ParamsValidator exactly(int n) throws IllegalArgumentException {
        if (args.length != n) {
            throw new IllegalArgumentException("Expected exactly " + n + " arguments");
        }
        return this;
    }

    /**
     * Internal method to get the current argument. Checks that the index is correct.
     * 
     * @return
     * @throws IllegalArgumentException
     */
    private String arg(int i) {
        if (argIndex < 0) {
            throw new IllegalArgumentException("Argument to check not set.");
        }
        if (argIndex >= args.length) {
            throw new IllegalArgumentException("No argument at index " + argIndex);
        }
        return args[argIndex];
    }

    /**
     * Check that the argument at index i is a file
     * 
     * @return
     * @throws IllegalArgumentException
     */
    public ParamsValidator isFile(int i) {
        String arg = arg(i);
        File f = new File(arg);
        if (!f.isFile()) {
            throw new IllegalArgumentException(
                    String.format("Argument %s at index %d must be a file", arg, argIndex));
        }
        return this;
    }

    /**
     * Check that the argument selected matches a regular expression
     * 
     * @param index of the argument
     * @param regular expression to check
     * @return
     * @throws IllegalArgumentException
     */
    public ParamsValidator matches(int i, String regex) {
        String arg = arg(i);
        if (!arg.matches(regex)) {
            throw new IllegalArgumentException("Parameter " + arg + " does not match expression " + regex);
        }
        return this;
    }

    public String getOption(String o) {
        return options.get(o);
    }
}
