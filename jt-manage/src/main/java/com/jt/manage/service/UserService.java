package com.jt.manage.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jt.manage.pojo.User;

public interface UserService {

	List<User> findAll();
}
