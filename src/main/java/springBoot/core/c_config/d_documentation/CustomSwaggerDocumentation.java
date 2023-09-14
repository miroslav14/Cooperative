package springBoot.core.c_config.d_documentation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collection;
import java.util.Collections;

@Configuration
public class CustomSwaggerDocumentation {

    @Bean
    public Docket swaggerConfiguration(){
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(true)
                .select()
                .paths(PathSelectors.ant("/public/auth/api/*").and(PathSelectors.ant("/next-working-day/*")))
                .apis(RequestHandlerSelectors.basePackage("springBoot.core"))
                .build()
                .apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo() {
        return new ApiInfo(
              "Spingboot Authenticated calls via JWT"
                ,"Springboot app documentation "
                ,"1.0"
                ,"Personal service terms"
                ,new Contact("Miroslav Lukac","http://...","lukacmiroslav@yahoo.co.uk")
                ,"GNU licence"
                ,"http://www....empty.licence"
                , Collections.emptyList()
        );
    }
}
