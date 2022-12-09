package com.example.blog.models.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserUpdateRequest {
    @NotBlank(message = "Firstname is mandatory")
    private String firstname;
    @NotBlank(message = "Lastname is mandatory")
    private String lastname;
}
