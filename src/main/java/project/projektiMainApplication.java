package project;

import java.util.concurrent.TimeUnit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@EnableCaching
@SpringBootApplication
public class projektiMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(projektiMainApplication.class, args);
    }
}
