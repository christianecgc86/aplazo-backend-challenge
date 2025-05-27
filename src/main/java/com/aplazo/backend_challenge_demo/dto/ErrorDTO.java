package com.aplazo.backend_challenge_demo.dto;

public record ErrorDTO(int status, String error, String message) {

    @Override
    public String toString() {
        return "{ " +
                "status=" + status +
                ", error='" + error + '\'' +
                ", message='" + message + '\'' +
                " }";
    }

}
