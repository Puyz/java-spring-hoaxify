package com.hoaxify.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoaxify.ws.entities.Token;

public interface ITokenRepository extends JpaRepository<Token, String>{

}
