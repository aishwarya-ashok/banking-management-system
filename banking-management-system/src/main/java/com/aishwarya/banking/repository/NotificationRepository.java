package com.aishwarya.banking.repository;

import com.aishwarya.banking.entity.Notification;
import com.aishwarya.banking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUser(User user);

    List<Notification> findByUserAndRead(User user, boolean read);
}
