package com.app.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerInfo {
    private String firstName;
    private String lastName;
    private Integer age;
    private String gender;
}