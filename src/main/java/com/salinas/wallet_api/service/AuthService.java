package com.salinas.wallet_api.service;

import com.salinas.wallet_api.dto.request.LoginRequestDTO;

public interface AuthService {
    String autenticar(LoginRequestDTO loginDTO);
}
