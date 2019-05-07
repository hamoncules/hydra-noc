package de.escalon.hypermedia.sample.springboot.config;

import de.escalon.hypermedia.spring.HypermediaTypes;
import de.escalon.hypermedia.spring.hydra.HydraMessageConverter;
import de.escalon.hypermedia.spring.hydra.JsonLdDocumentationProvider;
import de.escalon.hypermedia.spring.siren.SirenMessageConverter;
import de.escalon.hypermedia.spring.uber.UberJackson2HttpMessageConverter;
import de.escalon.hypermedia.spring.xhtml.XhtmlResourceMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.core.AnnotationRelProvider;
import org.springframework.hateoas.core.DefaultRelProvider;
import org.springframework.hateoas.core.DelegatingRelProvider;
import org.springframework.hateoas.core.EvoInflectorRelProvider;
import org.springframework.hateoas.hal.CurieProvider;
import org.springframework.hateoas.hal.DefaultCurieProvider;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.plugin.core.config.EnablePluginRegistries;
import org.springframework.util.ClassUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by ryan on 7/31/16.
 */
@Configuration
@EnablePluginRegistries(RelProvider.class)
public class HydraConfig extends WebMvcConfigurerAdapter {

    private static final boolean EVO_PRESENT =
            ClassUtils.isPresent("org.atteo.evo.inflector.English", null);

    @Autowired
    private PluginRegistry<RelProvider, Class<?>> relProviderRegistry;


    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(hydraMessageConverter());
        converters.add(sirenMessageConverter());
        converters.add(uberConverter());
        //converters.add(halConverter());
        //converters.add(jsonConverter());
        // disabling so we always get a JSON format
        //converters.add(xhtmlMessageConverter());

    }

    @Bean
    public HttpMessageConverter<?> uberConverter() {
        UberJackson2HttpMessageConverter converter = new UberJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(HypermediaTypes.UBER_JSON));
        return converter;
    }


    private HttpMessageConverter<?> xhtmlMessageConverter() {
        XhtmlResourceMessageConverter xhtmlResourceMessageConverter = new XhtmlResourceMessageConverter();
        xhtmlResourceMessageConverter.setStylesheets(
                Arrays.asList(
                        "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css"
                ));
        xhtmlResourceMessageConverter.setDocumentationProvider(new JsonLdDocumentationProvider());
        return xhtmlResourceMessageConverter;
    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        final ExceptionHandlerExceptionResolver resolver = new ExceptionHandlerExceptionResolver();
        resolver.setWarnLogCategory(resolver.getClass()
                .getName());
        exceptionResolvers.add(resolver);
    }

    @Bean
    public HydraMessageConverter hydraMessageConverter() {
        return new HydraMessageConverter();
    }

    @Bean
    public SirenMessageConverter sirenMessageConverter() {
        SirenMessageConverter sirenMessageConverter = new SirenMessageConverter();
        sirenMessageConverter.setRelProvider(new DelegatingRelProvider(relProviderRegistry));
        sirenMessageConverter.setDocumentationProvider(new JsonLdDocumentationProvider());
        sirenMessageConverter.setSupportedMediaTypes(Collections.singletonList(HypermediaTypes.SIREN_JSON));
        return sirenMessageConverter;
    }


//    @Bean
//    public MappingJackson2HttpMessageConverter jsonConverter() {
//        MappingJackson2HttpMessageConverter jacksonConverter = new
//                MappingJackson2HttpMessageConverter();
//        jacksonConverter.setSupportedMediaTypes(Arrays.asList(MediaType.valueOf("application/json")));
//        jacksonConverter.setObjectMapper(objectMapper);
//        return jacksonConverter;
//    }


    @Bean
    public CurieProvider curieProvider() {
        return new DefaultCurieProvider("ex", new UriTemplate("http://localhost:8080/rels/{rels}"));
    }

//    @Bean
//    public MappingJackson2HttpMessageConverter halConverter() {
//        CurieProvider curieProvider = curieProvider();
//
//        RelProvider relProvider = new DelegatingRelProvider(relProviderRegistry);
//        ObjectMapper halObjectMapper = new ObjectMapper();
//
//        halObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
//
//        halObjectMapper.registerModule(new Jackson2HalModule());
//        halObjectMapper.setHandlerInstantiator(new
//                Jackson2HalModule.HalHandlerInstantiator(relProvider, curieProvider,resourceDescriptionMessageSourceAccessor));
//
//        MappingJackson2HttpMessageConverter halConverter = new
//                MappingJackson2HttpMessageConverter();
//        halConverter.setSupportedMediaTypes(Arrays.asList(MediaTypes.HAL_JSON));
//        halConverter.setObjectMapper(halObjectMapper);
//        return halConverter;
//    }

    @Bean
    RelProvider defaultRelProvider() {
        return EVO_PRESENT ? new EvoInflectorRelProvider() : new DefaultRelProvider();
    }

    @Bean
    RelProvider annotationRelProvider() {
        return new AnnotationRelProvider();
    }
}
