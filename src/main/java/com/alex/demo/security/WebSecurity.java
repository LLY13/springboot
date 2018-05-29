package com.alex.demo.security;


import com.alex.demo.security.jwt.JwtAuthenticationFilter;
import com.alex.demo.security.jwt.JwtAuthenticationProvider;
import com.alex.demo.util.JsonUtil;
import java.io.IOException;
import java.util.Arrays;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurity extends WebSecurityConfigurerAdapter {

  public static final String UNAUTHORIZED = "无权访问";

  @Autowired
  private JwtAuthenticationProvider jwtProvider;
  @Autowired
  private JwtAuthenticationFilter jwtFilter;

  public static void authFail(HttpServletResponse res, String msg) throws IOException {
    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    res.setContentType(MediaType.APPLICATION_JSON_VALUE);
//    ErrorResponse error = new ErrorResponse(401, msg == null ? UNAUTHORIZED : msg);
//    JsonUtil.MAPPER.writeValue(res.getOutputStream(), error);
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(jwtProvider);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors().and()
        .csrf().disable()
        .formLogin().disable()
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .authorizeRequests()
        // all options request
        .antMatchers(HttpMethod.GET, "/**").permitAll()
        .antMatchers(HttpMethod.POST, "/authentication").permitAll()
        // static contents
//        .antMatchers(HttpMethod.GET, "/v2/api-docs", "/web/share/*").permitAll()
//        // api tags without auth
//        .antMatchers("/callback/**", "/authentication/**").permitAll()
//        // permitted get apis
//        .antMatchers(HttpMethod.GET, "/app/products/**", "/files/**").permitAll()
//        // data analysis allow anonymous request
//        .antMatchers(HttpMethod.POST, "/app/data-analysis/product/*").permitAll()
//        .antMatchers("/**").permitAll()
        .anyRequest().authenticated()
        .and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .exceptionHandling()
        .accessDeniedHandler((req, res, exp) -> res.sendError(HttpServletResponse.SC_FORBIDDEN))
        .authenticationEntryPoint((req, res, exp) -> {
          // error handled in filter, for jwt exception
          if (res.getStatus() == 200) {
            // if auth fail without auth head, then set response fail
            authFail(res, null);
          }
        });
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "PATCH", "PUT", "DELETE"));
    config.applyPermitDefaultValues();
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }

//  @Bean
//  public CommonsRequestLoggingFilter requestLoggingFilter() {
//    // FOR TEST DEBUGGING
//    CommonsRequestLoggingFilter logFilter = new CommonsRequestLoggingFilter();
//    logFilter.setIncludeHeaders(true);
//    logFilter.setIncludeQueryString(true);
//    logFilter.setIncludePayload(true);
//    logFilter.setMaxPayloadLength(10000);
////    logFilter.setIncludeClientInfo(true);
//    return logFilter;
//  }

  @Bean
  public ErrorProperties errorProperties() {
    return new ErrorProperties();
  }

}
