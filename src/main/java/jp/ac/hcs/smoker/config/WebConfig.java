package jp.ac.hcs.smoker.config;

import java.util.List;

import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * システム全体の設定を行う管理クラス。
 * <p>
 * DIの設定やシステム環境設定、システムに関わる定数を設定するために利用し、 その他の設定に関しては application.properties
 * ファイルに記述をします。
 *
 * @author s20203029
 *
 */
@Configuration
public class WebConfig
	implements WebMvcConfigurer, WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

	public static final int MAX_PAGE_SIZE = 20;

	/**
	 * RestTemplateライブラリのインスタンス
	 *
	 * <p>
	 * こちらはDIのために利用されることを想定しています。
	 * <p>
	 * <strong>{@code controller}や{@code service}から呼び出さないでください。</strong><br>
	 * 設定することで、{@code @Autowired}が設定されたプロパティへ自動的にインスタンスが設定されます。
	 *
	 * @return RestTemplateインスタンス
	 */
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	/**
	 * ページネーション有効なページで、１ページあたりの最大件数表示数が設定されていなければ実行されます。 初期状態だと
	 */
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
		resolver.setMaxPageSize(MAX_PAGE_SIZE);
		argumentResolvers.add(resolver);
	}

	/**
	 * MessageSourceライブラリのインスタンスを生成します。
	 *
	 * <p>
	 * こちらはDIのために利用されることを想定しています。
	 * <p>
	 * <strong><code>controller</code>や<code>service</code>から呼び出さないでください。</strong><br>
	 * 設定することで、<code>@Autowired</code>が設定されたプロパティへ自動的にインスタンスが設定されます。
	 *
	 * @return MessageSourceインスタンス
	 */
	@Bean
	public MessageSource messageSource() {

		// メッセージプロパティのファイル設定
		ReloadableResourceBundleMessageSource bean = new ReloadableResourceBundleMessageSource();
		bean.setBasename("classpath:messages");
		bean.setDefaultEncoding("UTF-8");

		return bean;
	}

	/**
	 * LocalValidatorFactoryBeanライブラリのインスタンスを生成します。
	 *
	 * <p>
	 * こちらはDIのために利用されることを想定しています。
	 * <p>
	 * <strong><code>controller</code>や<code>service</code>から呼び出さないでください。</strong><br>
	 * 設定することで、<code>@Autowired</code>が設定されたプロパティへ自動的にインスタンスが設定されます。
	 *
	 * @return LocalValidatorFactoryBeanインスタンス
	 */
	@Bean
	public LocalValidatorFactoryBean localValidatorFactoryBean() {

		// バリデーションのメッセージ設定
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.setValidationMessageSource(this.messageSource());

		return localValidatorFactoryBean;
	}

	@Override
	public void customize(ConfigurableServletWebServerFactory factory) {
		// factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND,
		// ActivityDefine.MAPPING_NEW_ACTIVITY_LIST));
	}
}
