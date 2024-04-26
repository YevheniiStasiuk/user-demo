package com.example.userdemo.repository;

import com.example.userdemo.exception.EntityAlreadyExistException;
import com.example.userdemo.exception.EntityNotFoundException;
import com.example.userdemo.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserRepository {
    private long idCounter = 0;
    private final List<User> userList = new ArrayList<>();

    public User create(User user) {
        if (existsByEmail(user.getEmail())) {
            throw new EntityAlreadyExistException();
        }

        user.setId(++idCounter);
        userList.add(user);
        return user;
    }

    public boolean existsByEmail(String email) {
        return userList.stream()
                .anyMatch((user -> email.equals(user.getEmail())));
    }

    public User getByEmail(String email) {
        return userList.stream()
                .filter((user -> email.equals(user.getEmail())))
                .findFirst()
                .orElseThrow(EntityNotFoundException::new);
    }

    public User update(User user) {
        final int userIndex = findUserIndexByEmail(user.getEmail());
        userList.set(userIndex, user);
        return user;
    }

    private int findUserIndexByEmail(String email) {
        for (int i = 0; i < userList.size(); i++) {
            if (email.equals(userList.get(i).getEmail())) {
                return i;
            }
        }
        throw new EntityNotFoundException();
    }

    public void deleteByEmail(String email) {
        final int userIndex = findUserIndexByEmail(email);
        userList.remove(userIndex);
    }

    public List<User> getUsersByBirthDateRange(Date from, Date to) {
        return userList.stream()
                .filter((user) -> user.getBirthDate().getTime() >= from.getTime()
                        && user.getBirthDate().getTime() <= to.getTime())
                .toList();
    }
}
