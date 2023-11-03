package org.didenko;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class ApplicationConfiguration {

    @Bean
    PortfolioBot portfolioBot (@Value("6366178187:AAEU9-R3lXBPeEFagC-4b1c6h-iHEyCRSIk") String botToken){
        return new PortfolioBot(botToken);
    }

}
