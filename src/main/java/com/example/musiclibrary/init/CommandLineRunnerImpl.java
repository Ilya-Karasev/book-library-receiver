package com.example.musiclibrary.init;
import com.example.musiclibrary.*;
import com.example.musiclibrary.grpc.FileUtil;
import com.example.musiclibrary.rabbitmq.*;
import com.example.musiclibrary.services.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        UserServiceGrpc.UserServiceBlockingStub userStub = UserServiceGrpc.newBlockingStub(channel);
        BookServiceGrpc.BookServiceBlockingStub bookStub = BookServiceGrpc.newBlockingStub(channel);
        RentalServiceGrpc.RentalServiceBlockingStub rentalStub = RentalServiceGrpc.newBlockingStub(channel);
        ReservationServiceGrpc.ReservationServiceBlockingStub reservationStub = ReservationServiceGrpc.newBlockingStub(channel);

        try {
            String name = "John Doe";
            System.out.println("<Поиск пользователя по имени " + name +">");
            UserNameRequest request = UserNameRequest.newBuilder().setName(name).build();
            var response = userStub.findUser(request);
            System.out.println("[User] \n" + response);
        } catch (Exception e) {
            System.err.println("Ошибка запроса: " + e.getMessage());
        }

        try {
            System.out.println("<Вывод всех пользователей>");
            UserListResponse userListResponse = userStub.getAllUsers(EmptyRequest.newBuilder().build());
            userListResponse.getUsersList().forEach(user -> System.out.println("[User] \n" + user));
        } catch (Exception e) {
            System.err.println("Ошибка запроса: " + e.getMessage());
        }

        try {
            String title = "Clean Code";
            System.out.println("<Поиск книги под названием " + title +">");
            BookTitleRequest request = BookTitleRequest.newBuilder().setTitle(title).build();
            BookResponse response = bookStub.findBook(request);
            System.out.println("[Book] \n" + response);
        } catch (Exception e) {
            System.err.println("Ошибка запроса: " + e.getMessage());
        }

        try {
            System.out.println("<Вывод всех книг>");
            BookListResponse response = bookStub.getAllBooks(EmptyRequest.newBuilder().build());
            response.getBooksList().forEach(book -> System.out.println("[Book] \n" + book));
        } catch (Exception e) {
            System.err.println("Ошибка запроса: " + e.getMessage());
        }

        try {
            String id = "ac146001-93ba-14a1-8193-ba04cbc10004";
            System.out.println("<Поиск записи аренды " + id +">");
            RentalRequest request = RentalRequest.newBuilder().setId(id).build();
            RentalResponse response = rentalStub.getRental(request);
            System.out.println("[Rental] \n" + response);
        } catch (Exception e) {
            System.err.println("Ошибка запроса: " + e.getMessage());
        }

        try {
            System.out.println("<Вывод всех записей аренды>");
            RentalListResponse response = rentalStub.getAllRentals(EmptyRequest.newBuilder().build());
            response.getRentalsList().forEach(rent -> System.out.println("[Rental] \n" + rent));
        } catch (Exception e) {
            System.err.println("Ошибка запроса: " + e.getMessage());
        }

        try {
            String id = ":/";
            System.out.println("<Поиск записи бронирования " + id +">");
            ReservationRequest request = ReservationRequest.newBuilder().setId(id).build();
            ReservationResponse response = reservationStub.getReservation(request);
            System.out.println("[Reservation] \n" + response);
        } catch (Exception e) {
            System.err.println("Ошибка запроса: " + e.getMessage());
        }

        try {
            System.out.println("<Вывод всех записей бронирования>");
            ReservationListResponse response = reservationStub.getAllReservations(EmptyRequest.newBuilder().build());
            response.getReservationsList().forEach(book -> System.out.println("[Reservation] \n" + book));
        } catch (Exception e) {
            System.err.println("Ошибка запроса: " + e.getMessage());
        }

        RentalCheckRequest request = RentalCheckRequest.newBuilder()
                .setUser("John Doe")
                .setBook("Effective Java")
                .build();

        try {
            RentalCheckResponse response = rentalStub.addRental(request);

            if (response.getSuccess()) {
                System.out.println("Аренда успешно создана.");
            } else {
                System.out.println("Не удалось создать аренду: " + response.getReceipt());
            }

            FileUtil.saveReceiptToFile(response.getReceipt(), "аренда");
        } catch (Exception e) {
            System.err.println("Ошибка при выполнении аренды: \n" + e.getMessage());
        }

        ReservationCheckRequest request2 = ReservationCheckRequest.newBuilder()
                .setUser("Anna Librarian")
                .setBook("Clean Code")
                .build();

        try {
            ReservationCheckResponse response = reservationStub.addReservation(request2);

            if (response.getSuccess()) {
                System.out.println("Бронирование успешно создано.");
            } else {
                System.out.println("Не удалось создать бронирование: \n" + response.getReceipt());
            }

            FileUtil.saveReceiptToFile(response.getReceipt(), "бронирование");
        } catch (Exception e) {
            System.err.println("Ошибка при выполнении бронирования: " + e.getMessage());
        }
    }
}