package com.app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookingHistoryRequestDTO {
        @Email
        @NotBlank
        private String email;
        public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            this.email = email;
        }
}
