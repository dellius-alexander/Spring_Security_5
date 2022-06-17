package com.example.spring_security_web_service.Security.Custom;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;

@Configuration("CustomPasswordEncoder")
public class CustomPasswordEncoder extends BCryptPasswordEncoder{
    private static final Logger log = LoggerFactory.getLogger(CustomPasswordEncoder.class);

    @Autowired
    public CustomPasswordEncoder(){ super(-1); }

    public CustomPasswordEncoder(
            BCryptVersion version,
            int strength,
            SecureRandom random
    ) {
        super(version, strength, random);
        log.info(toString());
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return super.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return super.matches(rawPassword, encodedPassword);
    }

    @Override
    public boolean upgradeEncoding(String encodedPassword) {
        return super.upgradeEncoding(encodedPassword);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomPasswordEncoder)) return false;
        CustomPasswordEncoder that = (CustomPasswordEncoder) o;
        return this.equals(that);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}