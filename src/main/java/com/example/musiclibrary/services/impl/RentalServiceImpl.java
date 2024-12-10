package com.example.musiclibrary.services.impl;

import com.example.musiclibrary.dtos.RentalDto;
import com.example.musiclibrary.dtos.show.RentalShow;
import com.example.musiclibrary.models.Book;
import com.example.musiclibrary.models.Rental;
import com.example.musiclibrary.models.User;
import com.example.musiclibrary.rabbitmq.RentalMessageReceiver;
import com.example.musiclibrary.repositories.BookRepository;
import com.example.musiclibrary.repositories.RentalRepository;
import com.example.musiclibrary.repositories.UserRepository;
import com.example.musiclibrary.services.RentalService;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
public class RentalServiceImpl implements RentalService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private RentalRepository rentalRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RentalMessageReceiver rentalReceiver;
    @Override
    public RentalDto addRental(RentalDto rental, String user, String book) throws InterruptedException {
        Rental r = modelMapper.map(rental, Rental.class);
        User u = userRepository.findByName(user).get();
        Book b = bookRepository.findByTitle(book).get();
        r.setUser(u);
        r.setBook(b);
        return modelMapper.map(rentalRepository.save(r), RentalDto.class);
    }

    @Override
    public Optional<RentalShow> findRental(UUID id) throws InterruptedException {
        return Optional.ofNullable(modelMapper.map(rentalRepository.findById(id), RentalShow.class));
    }

    @Override
    public Optional<RentalDto> findRentalDto(UUID id) throws InterruptedException {
        return Optional.ofNullable(modelMapper.map(rentalRepository.findById(id), RentalDto.class));
    }

    @Override
    public List<RentalShow> getAllRentals() throws InterruptedException {
        return rentalRepository.findAll().stream().map((r) -> modelMapper.map(r, RentalShow.class)).collect(Collectors.toList());
    }

    @Override
    public Optional<RentalDto> editRental(UUID id, RentalDto rental) throws InterruptedException {
        RentalDto r = modelMapper.map(rentalRepository.findById(id), RentalDto.class);
        r.setRental_date(rental.getRental_date());
        r.setDue_date(rental.getDue_date());
        r.setExtended_times(rental.getExtended_times());
        r.setIs_returned(rental.getIs_returned());
        r.setReturn_date(rental.getReturn_date());
        r.setModified(LocalDateTime.now());
        rentalRepository.save(modelMapper.map(r, Rental.class));
        return Optional.ofNullable(modelMapper.map(rentalRepository.findById(id), RentalDto.class));
    }

    @Override
    public void deleteRental(UUID id) throws InterruptedException {
        rentalRepository.delete(modelMapper.map(rentalRepository.findById(id), Rental.class));
    }
}