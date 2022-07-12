package org.test.codegen.app.cli;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

class CliOptsTest {
    private CommandLine commandLine;
    private TestCommand testCommand;

    @CommandLine.Command(mixinStandardHelpOptions = true)
    static class TestCommand implements Runnable {
        @CommandLine.Mixin
        private CliOpts cliOpts;

        @Override
        public void run() {

        }

        public CliOpts getCliOpts() {
            return cliOpts;
        }
    }

    @BeforeEach
    void setUp() {
        testCommand = new TestCommand();
        commandLine = new CommandLine(testCommand);
    }

    @Test
    void testCliOptsDefaultValues() {
        String[] args = new String[]{"-i", "src/test/java/org/test/codegen/app/cli/testconfigfile.yml", "-a", "sampleapplication"};
        commandLine.execute(args);
        Assertions.assertEquals("org.sample.test", testCommand.getCliOpts().getPackageName());
        Assertions.assertEquals("outgenerated", testCommand.getCliOpts().getOutputDir());
    }

    @Test
    void testCliOptsRequiredMissing() {
        String[] args = new String[]{};
        Assertions.assertEquals(commandLine.execute(args), CommandLine.ExitCode.USAGE);
    }

    @Test
    void testCliOptsRequired() {
        String[] args = new String[]{"-i", "src/test/java/org/test/codegen/app/cli/testconfigfile.yml", "-a", "sampleapplication"};
        commandLine.execute(args);
        Assertions.assertEquals("src/test/java/org/test/codegen/app/cli/testconfigfile.yml", testCommand.getCliOpts().getOpenAPI().toString());
        Assertions.assertEquals("sampleapplication", testCommand.getCliOpts().getAppName());
    }
}