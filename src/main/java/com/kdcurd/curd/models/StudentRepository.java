package com.kdcurd.curd.models;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    void deleteByName(String name);

    List<Student> findByName(String name);

    List<Student> findBySid(int sid);
}
