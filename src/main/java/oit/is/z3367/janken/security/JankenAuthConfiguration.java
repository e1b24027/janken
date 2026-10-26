package oit.is.z3367.janken.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * じゃんけんアプリの認証・認可の設定
 */
@Configuration
@EnableWebSecurity
public class JankenAuthConfiguration {

  /**
   * 認可処理に関する設定（認証されたユーザがどこにアクセスできるか）
   *
   * @param http
   * @return
   * @throws Exception
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.formLogin(login -> login
        .permitAll()) // Spring Securityのログインフォームを利用する
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")) // ログアウト後は / (トップページ)に戻る
        .authorizeHttpRequests(authz -> authz
            .requestMatchers("/janken*/**").authenticated() // /jankenで始まるURLは認証が必要
            .requestMatchers("/match/**").authenticated() // /matchも認証が必要
            .requestMatchers("/fight/**").authenticated() // /fightも認証が必要
            .anyRequest().permitAll()) // それ以外は誰でもアクセスできる
        .csrf(csrf -> csrf
            .ignoringRequestMatchers("/h2-console*/**")) // h2-consoleはCSRF対策を無効化する
        .headers(headers -> headers
            .frameOptions(frameOptions -> frameOptions
                .sameOrigin())); // h2-consoleはframeを使うため同一オリジンのframeを許可する
    return http.build();
  }

  /**
   * 認証処理に関する設定（誰がどのようなロールでログインできるか）
   *
   * @return
   */
  @Bean
  public InMemoryUserDetailsManager userDetailsService() {
    // パスワード(isdev)はBCryptでハッシュ化しているため，先頭に{bcrypt}をつける
    UserDetails user1 = User.withUsername("user1")
        .password("{bcrypt}$2a$10$FU0P.0vLocGHFslP08n1OueZmt5AHDWlKBS0BFQl0YUiwb9DeT.ge").roles("USER").build();
    UserDetails user2 = User.withUsername("user2")
        .password("{bcrypt}$2a$10$pZFfCxAwm3nkXIdqaHEM2.wP0mqaUJ2dCCtltBxSUyM17mPYPKv3u").roles("USER").build();
    // ほんだ (パスワード isdev)
    UserDetails honda = User.withUsername("ほんだ")
        .password("{bcrypt}$2a$10$FU0P.0vLocGHFslP08n1OueZmt5AHDWlKBS0BFQl0YUiwb9DeT.ge").roles("USER").build();

    // 生成したユーザをInMemoryUserDetailsManagerに渡す
    return new InMemoryUserDetailsManager(user1, user2, honda);
  }
}
