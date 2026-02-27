package com.rev.app.service;

import com.rev.app.entity.Employee;

public interface AuthService {

    Employee login(String email, String password);

}