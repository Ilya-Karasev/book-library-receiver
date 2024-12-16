package com.example.musiclibrary.grpc;

import com.example.musiclibrary.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TestClient {
    private final UserServiceGrpc.UserServiceBlockingStub userService;
    private final BookServiceGrpc.BookServiceBlockingStub bookService;
    private final RentalServiceGrpc.RentalServiceBlockingStub rentalService;
    private final ReservationServiceGrpc.ReservationServiceBlockingStub reservationService;

    public TestClient() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
        userService = UserServiceGrpc.newBlockingStub(channel);
        bookService = BookServiceGrpc.newBlockingStub(channel);
        rentalService = RentalServiceGrpc.newBlockingStub(channel);
        reservationService = ReservationServiceGrpc.newBlockingStub(channel);
    }

    public UserResponse findUser(String name) {
        UserNameRequest request = UserNameRequest.newBuilder().setName(name).build();
        return userService.findUser(request);
    }

    public UserListResponse getAllUsers() {
        return userService.getAllUsers(EmptyRequest.newBuilder().build());
    }

    public BookResponse findBook(String name) {
        BookTitleRequest request = BookTitleRequest.newBuilder().setTitle(name).build();
        return bookService.findBook(request);
    }

    public BookListResponse getAllBooks() {
        return bookService.getAllBooks(EmptyRequest.newBuilder().build());
    }

    public RentalResponse findRental(UUID id) {
        RentalRequest request = RentalRequest.newBuilder().setId(String.valueOf(id)).build();
        return rentalService.getRental(request);
    }

    public ReservationListResponse getAllReservations() {
        return reservationService.getAllReservations(EmptyRequest.newBuilder().build());
    }

    public ReservationResponse findReservation(UUID id) {
        ReservationRequest request = ReservationRequest.newBuilder().setId(String.valueOf(id)).build();
        return reservationService.getReservation(request);
    }

    public RentalListResponse getAllRentals() {
        return rentalService.getAllRentals(EmptyRequest.newBuilder().build());
    }
}