package com.example.hibernateinaction;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Generated;
import lombok.extern.log4j.Log4j2;
import org.hibernate.annotations.Formula;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@SpringBootApplication
@Log4j2
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(final CustomerRepository customerRepository) {
		return args -> {
			Customer customer = new Customer();
			customer.setName("ahmet");
			customer.setType(CustomerType.ELITE);
			customer.setBalance(15);

			Product p1 = new Product();
			p1.setTitle("iphone");
			p1.setPrice(BigDecimal.valueOf(65432.12));

			Product p2 = new Product();
			p2.setTitle("galaxy");
			p2.setPrice(BigDecimal.valueOf(22000.12));

			customer.setProducts(List.of(p1, p2));
			customerRepository.save(customer);
			log.error(customer);

		};
	}
}

interface CustomerRepository extends JpaRepository<Customer, Long> {

}

@Data
@Entity
class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Enumerated(value = EnumType.STRING)
	private CustomerType type;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Product> products;

	private int balance;
	@Formula(value = "balance * 0.18")
	private int tax;

}

@Data
@Entity
class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;
	@Formula("price*0.18")
	private BigDecimal price;
	private Metadata metadata;
}

@Embeddable
class Metadata {
	private Timestamp createdAt;

	private Timestamp updateAt;
}

enum CustomerType {
	ELITE,
	SUPER,
	BASE
}