package com.experto.seguridad.service;

import com.experto.seguridad.dto.UsuarioSessionDto;
import org.springframework.stereotype.Service;

@Service
public class DashboardAccessService {

    public boolean canAccess(UsuarioSessionDto user, String path) {
        if (user == null || user.menus() == null || path == null) {
            return false;
        }

        return user.menus().stream()
                .anyMatch(menu -> menu.accion() != null && path.equalsIgnoreCase(menu.accion()));
    }
}
