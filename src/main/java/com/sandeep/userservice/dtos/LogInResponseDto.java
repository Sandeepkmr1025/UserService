package com.sandeep.userservice.dtos;

import com.sandeep.userservice.models.Token;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogInResponseDto {
    private Token token;
}
