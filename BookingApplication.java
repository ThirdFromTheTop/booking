package com.example.booking;

import com.example.booking.domain.model.BookableItem;
import com.example.booking.domain.repository.ItemDomainRepository;
import com.example.booking.infrastructure.entity.UserEntity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;

@Repository
interface InitUserRepository extends JpaRepository<UserEntity, Long> {
    java.util.Optional<UserEntity> findByEmail(String email);
}

@SpringBootApplication
public class BookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookingApplication.class, args);
    }

    @Bean
    public CommandLineRunner initDatabase(InitUserRepository userRepository, ItemDomainRepository itemRepository) {
        return args -> {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            if (userRepository.findByEmail("ivan@mail.ru").isEmpty()) {
                UserEntity client = new UserEntity();
                client.setEmail("ivan@mail.ru");
                client.setPassword(encoder.encode("123")); 
                client.setFullName("Иван Иванов");
                client.setPhone("+79991112233");
                client.setRole("ROLE_CLIENT");
                userRepository.save(client);
            }

            if (userRepository.findByEmail("admin@mail.ru").isEmpty()) {
                UserEntity admin = new UserEntity();
                admin.setEmail("admin@mail.ru");
                admin.setPassword(encoder.encode("123")); 
                admin.setFullName("Главный Администратор");
                admin.setPhone("+79998887766");
                admin.setRole("ROLE_ADMIN");
                userRepository.save(admin);
            }

            if (itemRepository.findAll().isEmpty()) {
                itemRepository.save(new BookableItem(null, "Отель Marriott Сочи, Номер Стандарт", "HOTEL", new BigDecimal("5000.00"), 10));
                itemRepository.save(new BookableItem(null, "Авиабилет SU-2130 Москва - Сочи (Аэрофлот)", "FLIGHT", new BigDecimal("8500.00"), 180));
                itemRepository.save(new BookableItem(null, "Билет на поезд Сапсан СПб - Москва", "TRAIN", new BigDecimal("3500.00"), 54));
            }
        };
    }
}
