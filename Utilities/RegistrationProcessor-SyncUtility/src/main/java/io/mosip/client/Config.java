package io.mosip.client;

import io.mosip.registration.processor.packet.manager.decryptor.Decryptor;
import io.mosip.registration.processor.status.service.impl.RegistrationStatusServiceImpl;
import io.mosip.registration.processor.status.service.impl.SyncRegistrationServiceImpl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    /*@Bean
    public RegistrationStatusServiceImpl registrationStatusServiceImpl() {
        return new RegistrationStatusServiceImpl();
    }

    @Bean
    public SyncRegistrationServiceImpl syncRegistrationServiceImpl() {
        return new SyncRegistrationServiceImpl();
    }*/

   /* @Bean
    public Decryptor decryptor() {
        return new Decryptor();
    }*/
}
