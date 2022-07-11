package com.toptal.soccer.repo;

import com.toptal.soccer.model.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class BaseRepoTest {
    private final static String EMAIL = "email.@email.com";
    private final static String PASSWORD = "password";

    @Autowired
    private TestEntityManager entityManager;


    @Transactional
    protected  <T>  T save(final T entity){
         entityManager.getEntityManager().persist(entity);
         return entity;
    }



}
