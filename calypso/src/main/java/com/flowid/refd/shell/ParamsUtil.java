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
package com.flowid.refd.shell;

import java.io.File;
import java.util.HashMap;

public class ParamsUtil {
    // arguments that are validated
    private String[] args;

    // options
    HashMap<String, String> options;

    // to allow validation of individual arguments, this is the index of the argument
    private int argIndex = -1;
    private String option = "";

    static public ParamsUtil validate(String[] args) {
        ParamsUtil pu = new ParamsUtil();
        pu.args = args;
        return pu;
    }

    static public ParamsUtil validate(HashMap<String, String> options) {
        ParamsUtil pu = new ParamsUtil();
        pu.options = options;
        return pu;
    }

    /**
     * Set the current argument to validate
     * 
     * @param i index of the argument to validate
     * @return
     * @throws IllegalArgumentException
     */
    public ParamsUtil arg(int i) {
        argIndex = i;
        getCurrentArg(); // validate the index
        return this;
    }

    public ParamsUtil option(String o) {
        option = o;
        return this;
    }

    public ParamsUtil mandatory() {
        String val = options.get(option);
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
    public ParamsUtil atLeast(int n) throws IllegalArgumentException {
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
    public ParamsUtil exactly(int n) throws IllegalArgumentException {
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
    private String getCurrentArg() {
        if (argIndex < 0) {
            throw new IllegalArgumentException("Argument to check not set.");
        }
        if (argIndex >= args.length) {
            throw new IllegalArgumentException("No argument at index " + argIndex);
        }
        return args[argIndex];
    }

    /**
     * Check that the current argument is a file
     * 
     * @return
     * @throws IllegalArgumentException
     */
    public ParamsUtil isFile() {
        String arg = getCurrentArg();
        File f = new File(arg);
        if (!f.isFile()) {
            throw new IllegalArgumentException(String.format("Argument %s at index %d must be a file", arg,
                    argIndex));
        }
        return this;
    }

    /**
     * Check that the argument selected matches a regular expression
     * 
     * @param regex
     * @return
     * @throws IllegalArgumentException
     */
    public ParamsUtil matches(String regex) {
        String arg = getCurrentArg();
        if (!arg.matches(regex)) {
            throw new IllegalArgumentException("Parameter " + arg + " does not match expression " + regex);
        }
        return this;
    }

    public String getOption(String o) {
        return options.get(o);
    }
}
