package com.flowid.shell;

import static org.junit.Assert.assertEquals;

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
}
