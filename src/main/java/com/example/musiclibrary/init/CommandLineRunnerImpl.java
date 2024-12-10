package com.example.musiclibrary.init;
import com.example.musiclibrary.dtos.BookDto;
import com.example.musiclibrary.dtos.RentalDto;
import com.example.musiclibrary.dtos.ReservationDto;
import com.example.musiclibrary.dtos.UserDto;
import com.example.musiclibrary.rabbitmq.*;
import com.example.musiclibrary.services.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {
    @Autowired
    private UserService userService;
    @Autowired
    private BookService bookService;
    @Autowired
    private RentalService rentalService;
    @Autowired
    private ReservationService reservationService;
    public CommandLineRunnerImpl(UserService userService, BookService bookService, RentalService rentalService, ReservationService reservationService) {
        this.userService = userService;
        this.bookService = bookService;
        this.rentalService = rentalService;
        this.reservationService = reservationService;
    }
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RentalMessageReceiver rentalReceiver;
    @Autowired
    private ReservationMessageReceiver reservationReceiver;
    @Override
    public void run(String... args) throws Exception {
    }
}