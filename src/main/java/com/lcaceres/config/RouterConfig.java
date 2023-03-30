package com.lcaceres.config;

import com.lcaceres.handler.CourseHandler;
import com.lcaceres.handler.StudentHandler;
import com.lcaceres.handler.TuitionHandler;
import com.lcaceres.util.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> routesStudent(StudentHandler studentHandler){
        return route(GET(Constants.URL_V2 + Constants.URL_STUDENTS + "/findAll"), studentHandler::findAll)
                .andRoute(GET(Constants.URL_V2 + Constants.URL_STUDENTS + "/findById" +  "/{id}"), studentHandler::findById)
                .andRoute(GET(Constants.URL_V2 + Constants.URL_STUDENTS + "/findAll/orderByAge" + "/{key}"), studentHandler::orderByAge)
                .andRoute(POST(Constants.URL_V2 + Constants.URL_STUDENTS + "/save"), studentHandler::save)
                .andRoute(PUT(Constants.URL_V2 + Constants.URL_STUDENTS + "/update" + "/{id}"), studentHandler::update)
                .andRoute(DELETE(Constants.URL_V2 + Constants.URL_STUDENTS + "/delete" + "/{id}"), studentHandler::delete);
    }

    @Bean
    public RouterFunction<ServerResponse> routesCourse(CourseHandler courseHandler){
        return route(GET(Constants.URL_V2 + Constants.URL_COURSES + "/findAll"), courseHandler::findAll)
                .andRoute(GET(Constants.URL_V2 + Constants.URL_COURSES + "/findById" +  "/{id}"), courseHandler::findById)
                .andRoute(POST(Constants.URL_V2 + Constants.URL_COURSES + "/save"), courseHandler::save)
                .andRoute(PUT(Constants.URL_V2 + Constants.URL_COURSES + "/update" + "/{id}"), courseHandler::update)
                .andRoute(DELETE(Constants.URL_V2 + Constants.URL_COURSES + "/delete" + "/{id}"), courseHandler::delete);
    }

    @Bean
    public RouterFunction<ServerResponse> routesTuition(TuitionHandler tuitionHandler){
        return route(GET(Constants.URL_V2 + Constants.URL_LICENSEPLATES + "/findAll"), tuitionHandler::findAll)
                .andRoute(POST(Constants.URL_V2 + Constants.URL_LICENSEPLATES + "/save"), tuitionHandler::save);
    }

}
