package com.chronicare.platform.iam.infrastructure.hashing.bcrypt;

import com.chronicare.platform.iam.application.internal.outboundservices.hashing.HashingService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BCryptHashingService extends BCryptPasswordEncoder implements HashingService {
}
