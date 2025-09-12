package com.example.userservice.config;

import com.example.userservice.dto.AuthUserDto;
import com.example.userservice.model.User;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class BeanConfig {

        @Bean
        public ModelMapper modelMapper() {
            ModelMapper mm = new ModelMapper();
            mm.getConfiguration()
                    .setMatchingStrategy(MatchingStrategies.STRICT)
                    .setAmbiguityIgnored(true)
                    .setPropertyCondition(Conditions.isNotNull());
            

            return mm;
        }


    @Bean
            public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
