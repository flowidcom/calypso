package com.flowid.shell;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

public class JTestShell {
    @Test
    public void testShellCommands() {
        JShell shell = new JShell();
        // add the internal commands
        shell.addCommandsFrom(shell);
        // run the internal commands
        int retCode = shell.execute(new String[] {"commands"});
        // check that this command returns OK
        assertEquals(0, retCode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAtLeastFail() {
        Params
            .args(new String[] {})
            .atLeast(1);
    }

    @Test
    public void testNumberOfParams() {
        Params
            .args(new String[] {})
            .exactly(0);

        Params
            .args(new String[] {"a"})
            .exactly(1)
            .atLeast(1);
    }

    @Test
    public void testIsFile() {
        Params params = Params
            .args(new String[] {"src/test/data/in/country-codes-1.csv"})
            .atLeast(1);
        File f = params.argAsFile(0);
        assertNotNull(f);
    }

    /**
     * One way to set local variables
     */
    @Test
    public void testAsString() {
        Ref<String> arg = new Ref<>();
        Params
            .args(new String[] {"a"})
            .atLeast(1)
            .copy(0, arg);
        assertEquals("a", arg.get());
    }

    /**
     * A third way to get an argument, no side-effects
     */
    @Test
    public void testStringArgs() {
        Params params = Params
            .args(new String[] {"a"})
            .atLeast(1);

        assertEquals("a", params.arg(0));
    }

    @Test
    public void testOptions() {
        Params params = Params
            .args()
            .options("o", "1");

        assertEquals("1", params.option("o"));
        assertTrue(params.hasOption("o"));
    }
}
