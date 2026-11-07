package api.data;

import api.models.booker.AuthRequest;

import static api.config.ApiConfigProvider.CONFIG;

public final class AuthData {

    private AuthData() {
    }

    public static AuthRequest adminCredentials() {
        return AuthRequest.builder()
                .username(CONFIG.bookerUsername())
                .password(CONFIG.bookerPassword())
                .build();
    }

    public static AuthRequest wrongPassword() {
        return AuthRequest.builder()
                .username(CONFIG.bookerUsername())
                .password("wrong-password")
                .build();
    }
}
