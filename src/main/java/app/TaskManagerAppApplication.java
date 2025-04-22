package app;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskManagerAppApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().load();
        dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));
        //System.out.println(System.getProperty("MYSQLDB_LOCAL_PORT"));
        SpringApplication.run(TaskManagerAppApplication.class, args);
    }
}
