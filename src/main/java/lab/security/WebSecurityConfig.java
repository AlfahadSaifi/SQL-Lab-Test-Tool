package lab.security;


import lab.service.user.UserDetailsServiceCustomImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Set;


@Configuration
@ComponentScan(basePackages = "lab")
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsServiceCustomImpl;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceCustomImpl).passwordEncoder(passwordEncoder);
        auth.jdbcAuthentication()
                .dataSource(
                        dataSource
                )
                .usersByUsernameQuery(
                        "SELECT username, password, enabled FROM users where username = ? "
                )
                .authoritiesByUsernameQuery(
                        "SELECT employee_name, role FROM sql_user_17239 WHERE employee_name= ?"
                );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")     // accessible by Admin
                .antMatchers("/trainee/**").hasRole("TRAINEE")  // accessible by Trainee
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?error=true")// Custom Login Page
                .successHandler((httpServletRequest, httpServletResponse, authentication) -> {
                    Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
                    if (roles.contains("ROLE_ADMIN")) {
                        httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/admin/dashboard");
                    } else if (roles.contains("ROLE_TRAINEE")) {
                        httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/trainee/dashboard");
                    } else {
                        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "UNKNOWN USER FOUND");
                    }
                })
                .and().exceptionHandling().accessDeniedPage("/accessDenied")
                .and()
                .logout()
                .logoutSuccessUrl("/login")
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//                .invalidSessionUrl("/login")
//                .sessionFixation().migrateSession()
//                .maximumSessions(1)
//                .maxSessionsPreventsLogin(false);
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .permitAll();

    }
}
