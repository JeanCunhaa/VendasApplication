package domain;

import domain.entity.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import domain.repository.Clientes;

import java.util.List;

@SpringBootApplication
public class VendasApplication {

    @Bean
    public CommandLineRunner init(@Autowired Clientes clientes){
        return args -> {
            System.out.println("Salvando clientes");

            clientes.save(new Cliente("Jean"));
            clientes.save(new Cliente("Kenji Yasuda"));

            List<Cliente> todosClientes = clientes.findAll();
            todosClientes.forEach(System.out::println);


    };
    }

    public static void main(String[] args) {
        SpringApplication.run(VendasApplication.class, args);
    }
}