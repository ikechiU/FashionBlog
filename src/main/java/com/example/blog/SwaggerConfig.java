//package com.example.blog;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.service.VendorExtension;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Configuration
//@EnableSwagger2
//public class SwaggerConfig {
//    Contact contact = new Contact(
//            "Ikechi Ucheagwu",
//            "https://ikechiu.github.io/",
//            "ikechi@hotmail.com"
//    );
//
//    List<VendorExtension> vendorExtensions = new ArrayList<>();
//
//    ApiInfo apiInfo = new ApiInfo(
//            "Blog RESTful Web Service documentation",
//            "This pages documents a sample Blog RESTful Web Service endpoints",
//            "1.0",
//            "http://www.apache.org/licenses/LICENSE-2.0",
//            contact,
//            "Apache 2.0",
//            "http://www.apache.org/licenses/LICENSE-2.0",
//            vendorExtensions);
//
//
//    @Bean
//    public Docket apiDocket() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo) //Extra documentation features
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.example.blog"))
//                .paths(PathSelectors.any())
//                .build();
//    }
//
//}
