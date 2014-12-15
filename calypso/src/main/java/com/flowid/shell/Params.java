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
public class Params {
    // arguments
    private String[] args;

    // options
    private HashMap<String, String> options;

    /**
     * Initialize the validator passing a list of arguments
     * 
     * @param args
     * @return
     */
    static public Params args(String[] args) {
        Params pu = new Params();
        pu.args = args;
        return pu;
    }

    /**
     * Initialize the validator passing a list of arguments
     * 
     * @param args
     * @return
     */
    static public Params args() {
        return args(new String[0]);
    }

    /**
     * Initialize the validator passing a list of options
     * 
     * @param options
     * @return
     */
    public Params options(HashMap<String, String> options) {
        this.options = options;
        return this;
    }

    /**
     * Initialize the validator passing a list of options
     * 
     * @param opts
     * @return
     */
    public Params options(String... opts) {
        this.options = new HashMap<String, String>();
        for (int i = 0; i < opts.length / 2; i++) {
            this.options.put(opts[2 * i], opts[2 * i + 1]);
        }
        return this;
    }

    /**
     * Validate that an option is present
     * 
     * @param option
     * @return
     */
    public Params mandatory(String option) {
        String val = option(option);
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
    public Params atLeast(int n) throws IllegalArgumentException {
        if (args == null || args.length < n) {
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
    public Params exactly(int n) throws IllegalArgumentException {
        if (args == null || args.length != n) {
            throw new IllegalArgumentException("Expected exactly " + n + " arguments");
        }
        return this;
    }

    /**
     * Return an argument, null if index is not found
     * 
     * @return
     * @throws IllegalArgumentException
     */
    public String arg(int i) {
        if (args == null) {
            throw new IllegalArgumentException("Arguments not set");
        }
        if (i < 0) {
            throw new IllegalArgumentException("Invalid index");
        }
        if (i >= args.length) {
            return null;
        }
        return args[i];
    }

    public File argAsFile(int i) {
        String fileName = arg(i);
        File f = new File(fileName);
        if (!f.isFile()) {
            throw new IllegalArgumentException("Argument " + i + " is not a file.");
        }
        return f;
    }

    /**
     * Internal method to get the current argument. Checks that the index is correct.
     * 
     * @return
     * @throws IllegalArgumentException
     */
    public String arg(int i, String dvalue) {
        String val = arg(i);
        return val == null ? dvalue : val;
    }

    /**
     * Copy the argument into a reference structure
     * 
     * @return
     * @throws IllegalArgumentException
     */
    public Params copy(int i, Ref<String> ref) {
        if (args != null && args.length > i) {
            ref.set(args[i]);
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
    public Params matches(int i, String regex) {
        String arg = arg(i);
        if (!arg.matches(regex)) {
            throw new IllegalArgumentException("Parameter " + arg + " does not match expression " + regex);
        }
        return this;
    }

    /**
     * Return the value of an option, null if not found
     * 
     * @param option name
     * @return option value
     */
    public String option(String o) {
        return options.get(o);
    }


    public String option(String o, String dval) {
        String val = options.get(o);
        return val == null ? dval : val;
    }

    public boolean hasOption(String o) {
        return options.containsKey(o);
    }
}
