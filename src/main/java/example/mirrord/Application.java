package example.mirrord;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    /**
     * Main Entry point for {{StaticDataAppLoader}} service loader.
     *
     * @param args - application entry point parameters
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}