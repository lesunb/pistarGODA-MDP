package br.unb.cic.integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import java.util.concurrent.Callable;
import java.io.File;

@Command(name = "goda", mixinStandardHelpOptions = true, version = "3.0.0",
        description = "Run the goda framework of an Goal Model file to STDOUT.")
public class CLI implements Callable<Integer> {

    @Parameters(index = "0", description = "The Goal Model to decompose.")
    private File file;
    @Option(names = {"-p", "--pistar"}, description = "Run piStar Server instead of decomposing", defaultValue = "false")
    private boolean pistar = false;
    @Override
    public Integer call() throws Exception { // your business logic goes here...
        if(pistar){
            Application.server();
        }else{
            System.out.println("teste");
        }
        return 0;
    }

    public static void main(String[] args) {
        new CommandLine(new CLI()).execute(args);
    }
}