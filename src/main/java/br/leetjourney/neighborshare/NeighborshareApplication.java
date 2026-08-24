package br.leetjourney.neighborshare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class NeighborshareApplication {

    public static void main(String[] args) {
        SpringApplication.run(NeighborshareApplication.class, args);
    }

}
