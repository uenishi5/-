package jp.ac.hcs.smoker.config;

import java.util.Collections;

import javax.servlet.SessionTrackingMode;
import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring().antMatchers("/css/∗∗", "/img/∗∗", "/h2-console/∗∗");
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// ログイン不要ページの設定
		http.authorizeHttpRequests()
			// css js アクセス全部許可
			.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()

			// ホーム画面はアクセス全部許可
			.anyRequest().permitAll();
		// .anyRequest().authenticated();

		// // ログイン処理
		// http.formLogin().loginProcessingUrl("/signin") // ログイン処理のパス
		// .loginPage("/signin") // ログインページの指定
		// .failureUrl("/signin") // ログイン失敗時の遷移先
		// .usernameParameter("user_id") // ログインページのユーザID
		// .passwordParameter("password") // ログインページのパスワード
		// .defaultSuccessUrl(HomeController.MAPPING_HOME, true); // ログイン成功後の遷移先

		// // ログアウト処理
		// http.logout().logoutRequestMatcher(new
		// AntPathRequestMatcher("/signout")).logoutUrl("/signout") // ログアウト処理のパス
		// .logoutSuccessUrl("/signin"); // ログアウト成功後の遷移先

		// (開発用)CSRF対策 無効設定
		// XXX h2-console使用時は有効にする.
		http.csrf().disable();
		http.headers().frameOptions().disable();

		return http.build();
	}

	@Bean
	public UserDetailsManager users(DataSource dataSource) {
		JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
		users.setUsersByUsernameQuery("");
		users.setAuthoritiesByUsernameQuery("");
		return users;
	}

	/**
	 * 初回アクセス時に、URLにjsessionidが付与されるのを防ぐためのBean定義
	 *
	 * @return ServletContextInitializerオブジェクト
	 */
	@Bean
	public ServletContextInitializer servletContextInitializer() {
		return servletContext -> servletContext.setSessionTrackingModes(Collections.singleton(SessionTrackingMode.COOKIE));
	}
}