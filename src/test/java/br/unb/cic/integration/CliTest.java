package br.unb.cic.integration;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemErrRule;
import org.junit.contrib.java.lang.system.SystemOutRule;
import static org.junit.Assert.*;

import picocli.CommandLine;

public class CliTest {
    @Rule
    public SystemErrRule systemErrRule = new SystemErrRule().enableLog().muteForSuccessfulTests();

    @Rule
    public SystemOutRule systemOutRule = new SystemOutRule().enableLog().muteForSuccessfulTests();

    @Test
    public void default_exec() {
        CLI app = new CLI();
        new CommandLine(app).execute("arst");
        String expected = String.format("teste%n");

        assertEquals(expected, systemOutRule.getLog());
        assertEquals("", systemErrRule.getLog());

    }
}
