# Spring Boot #

## 1、下载 ##
[start.spring.io](http://start.spring.io/)
Group 为  com.alex   (com + 个人/公司名)
Artifact 为  项目名

Dependencies 可包括 Web 和 Devtools

## 2、初始化 ##

### DemoApplication ###
```
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
```
> @SpringBootApplication = (默认属性)@Configuration + @EnableAutoConfiguration + @ComponentScan
> > **1.@Configuration**的注解类标识这个类可以使用Spring IoC容器作为bean定义的来源。@Bean注解告诉Spring，一个带有@Bean的注解方法将返回一个对象，该对象应该被注册为在Spring应用程序上下文中的bean。
> > **2.@EnableAutoConfiguration**：能够自动配置spring的上下文，试图猜测和配置你想要的bean类，通常会自动根据你的类路径和你的bean定义自动配置。
> > **3.@ComponentScan**：会自动扫描指定包下的全部标有@Component的类，并注册成bean，当然包括@Component下的子注解@Service,@Repository,@Controller


### pom.xml ###

```
    <!--支持全栈式Web开发，包括Tomcat和spring-webmvc-->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

    <!--热部署-->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-devtools</artifactId>
			<scope>runtime</scope>
		</dependency>

    <!--支持常规的测试依赖，包括JUnit、Hamcrest、Mockito以及spring-test模块-->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
```

### 引入第一个controller ###
```
@RestController
@RequestMapping("/hello")
public class TestController {

  @GetMapping
  public String index() {
    return "Hello World";
  }
}
```

> @RestController 会返回一个json


## 3、其余配置 ##

### application.yml ###
1. 在properties文件中是以”.”进行分割的， 在yml中是用”:”进行分割; 
2. yml的数据格式和json的格式很像，都是K-V格式，并且通过”:”进行赋值； 
3. 在yml中缩进一定不能使用TAB，否则会报很奇怪的错误；（缩进特么只能用空格！！！！） 
4. 每个k的冒号后面一定都要加一个空格； 

> 将yml与properties对比之后发现，yml最大的好处在于其拥有天然的树状结构，所以着手尝试将properties文件更改为yml文件


### mysql配置 ###
```
spring:
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost:3306/ammour
```

**driver-class-name 要导包 **
> ```
>     <!--mysql connector-->
    <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>5.1.45</version>
      <scope>runtime</scope>
    </dependency>
> ```


**推荐使用数据库连接池hikari**
```
spring:
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost:3306/test?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8&useSSL=false
    hikari:
      username: root
      password: root
      auto-commit: true
      transaction-isolation: TRANSACTION_READ_COMMITTED
      connection-timeout: 60000
      maximum-pool-size: 20
      idle-timeout: 60000
      max-lifetime: 1800000
    initialization-mode: always
```


### lombok ###
```
    <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
      <optional>true</optional>
    </dependency>
```

### 自定义的配置 ###
#### 第一步： application.yml ####
```
name:
  lastname: li
  firstname:
    english: alex
    chinese: longyi

myenglishname: ${name.firstname.english}${name.lastname}
```

#### **第二步： CustomConfig.java** ####
**别忘记@Configuration，不然会报错**
```
@Getter
@Configuration
public class CustomConfig {

  @Value("${name.lastname}")
  private String lastname;

  @Value("${name.firstname.english}")
  private String english;

  @Value("${name.firstname.chinese}")
  private String chinese;

  @Value("${myenglishname}")
  private String fullname;

}
```

#### 第三步： 使用之前先注入 ####
```
  @Autowired
  private CustomConfig config;

  @GetMapping
  public String index() {
    return "Hello World " + config.getFullname();
  }
```


### 多环境的配置 ###
**application.yml**
```
spring:
  profiles:
    active: dev
```
> 还有两个yml文件  application-dev.yml  application-prod.yml


### MyBatis 配置 ###
@MapperScan 要改

```
@Configuration
@MapperScan("com.alex.demo.dao")
public class MybatisConfig {

  @Autowired
  private HikariDataSource dataSource;

  @Bean
  public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
    SqlSessionFactoryBean session = new SqlSessionFactoryBean();
    session.setDataSource(dataSource);

    // session configuration
    org.apache.ibatis.session.Configuration config = new org.apache.ibatis.session.Configuration();
    config.setMapUnderscoreToCamelCase(true);
    config.setUseGeneratedKeys(true);

    // config type handler
//    scanTypeHandler(config.getTypeHandlerRegistry());
    session.setConfiguration(config);

    // xml location configuration
    PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    session.setMapperLocations(resolver.getResources("classpath*:mapper/**/*.xml"));

    // get and return
    return session.getObject();
  }

  @Bean
  public PlatformTransactionManager transactionManager() {
    return new DataSourceTransactionManager(dataSource);
  }
}
```


### mybatis 对于枚举类的处理 ###
mybatisconfig 中添加
```
private void scanTypeHandler(TypeHandlerRegistry registry) {
    // custom enums
    Reflections enums = new Reflections("com.alex.demo.constant");
    register(registry, enums, FirstChar.class, FirstCharHandler.class);
    register(registry, enums, Ordinal.class, EnumOrdinalTypeHandler.class);
    register(registry, enums, Normal.class, EnumTypeHandler.class);
////     custom types
//    Reflections types = new Reflections("com.alex.demo.pojo.po");
//    register(registry, types, JsonType.class, JsonTypeHandler.class);
  }

  private <T, H extends BaseTypeHandler> void register(TypeHandlerRegistry registry,
      Reflections reflections, Class<T> clz, Class<H> handler) {
    Set<Class<? extends T>> classes = reflections.getSubTypesOf(clz);
    for (Class<?> c : classes) {
      registry.register(c, handler);
    }
```
其中Ordinal 和 normal mybatis已自带
对于firstchar需要写一个
如下
```
/**
 * mybatis {@link FirstChar} type handler
 */
public class FirstCharHandler<E extends FirstChar> extends BaseTypeHandler<E> {

  private final Class<E> type;
  private final E[] enums;

  public FirstCharHandler(Class<E> type) {
    if (type == null) {
      throw new IllegalArgumentException("Type argument cannot be null");
    }
    this.type = type;
    this.enums = type.getEnumConstants();
    if (this.enums == null) {
      throw new IllegalArgumentException(
          type.getSimpleName() + " does not represent an enum type.");
    }
  }

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType)
      throws SQLException {
    ps.setString(i, String.valueOf(parameter.v()));
  }

  @Override
  public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
    String ch = rs.getString(columnName);
    return rs.wasNull() ? null : valueOf(ch);
  }

  @Override
  public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    String ch = rs.getString(columnIndex);
    return rs.wasNull() ? null : valueOf(ch);
  }

  @Override
  public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    String ch = cs.getString(columnIndex);
    return cs.wasNull() ? null : valueOf(ch);
  }

  private E valueOf(String ch) {
    if (ch.isEmpty()) {
//      throw TException.of(ResponseEnum.DB_JUNK_DATA);
    }

    char c = ch.charAt(0);
    for (E en : enums) {
      if (c == en.v()) {
        return en;
      }
    }
    return null;
//    throw TException.of(ResponseEnum.DB_EXCEPTION);
  }
```

```
/**
   * 首字母
   */
  public interface FirstChar {

    String name();

    default char v() {
      return name().charAt(0);
    }
  }

  public interface Normal {

    String name();

    default String v() {
      return name();
    }
  }

  public interface Ordinal {

    int ordinal();

    default int v() {
      return ordinal();
    }
  }
```

**必须引入google guava包**
```
    <!-- guava google Java开发工具 -->
    <dependency>
      <groupId>com.google.guava</groupId>
      <artifactId>guava</artifactId>
      <version>23.0</version>
    </dependency>
```


### swagger2 配置 ###
#### 1.0 ####

```
package com.alex.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2 {

  //swagger2的配置文件，这里可以配置swagger2的一些基本的内容，比如扫描的包等等
  @Bean
  public Docket createRestApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(apiInfo())
        .select()
        //为当前包路径
        .apis(RequestHandlerSelectors.basePackage("com.alex.demo.controller"))
        .paths(PathSelectors.any())
        .build();
  }

  //构建 api文档的详细信息函数,注意这里的注解引用的是哪个
  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        //页面标题
        .title("Spring Boot 测试使用 Swagger2 构建RESTful API")
        //创建人
        .contact(new Contact("Alex",
            "https://weibo.com/1696882020/profile?rightmod=1&wvr=6&mod=personinfo",
            "nilongyi13@gmail.com"))
        //版本号
        .version("1.0")
        //描述
        .description("API 描述")
        .build();
  }


}

```

### 时间处理 ###
1. 实体中 推荐使用LocalDateTime、LocalDate、LocalTime.
> 格式为 ```2018-03-08T14:27:35```
2. 配置时间格式 否则传递时间报错
> ```Failed to convert value of type 'java.lang.String' to required type 'java.time.LocalDateTime'```

#### 代码 ####
    private LocalDateTime createdOn;

```
@Configuration
public class DateTimeConfig {

//  @Bean
//  @Primary
//  public ObjectMapper serializingObjectMapper() {
//    return JsonUtil.MAPPER;
//  }

  @Bean
  public Formatter<LocalDate> localDateFormatter() {
    return new Formatter<LocalDate>() {
      @Override
      public LocalDate parse(String text, Locale locale) throws ParseException {
        return LocalDate.parse(text, Constant.DATE_FORMAT);
      }

      @Override
      public String print(LocalDate object, Locale locale) {
        return Constant.DATE_FORMAT.format(object);
      }
    };
  }

  @Bean
  public Formatter<LocalDateTime> localDateTimeFormatter() {
    return new Formatter<LocalDateTime>() {
      @Override
      public LocalDateTime parse(String text, Locale locale) throws ParseException {
        return LocalDateTime.parse(text, Constant.DATETIME_FORMAT);
      }

      @Override
      public String print(LocalDateTime object, Locale locale) {
        return Constant.DATETIME_FORMAT.format(object);
      }
    };
  }

}
```

### redis缓存 ###
#### 1.0 使用spring cache ####
1、 maven
```
    <!--redis缓存-->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
```
2、 yml 配置
```
spring
  redis:
    host: 
    port: 
    database: 2
    password: 
    timeout: 60000
    jedis:
      pool:
        max-idle: 100    #最大空闲数
        min-idle: 20
        max-active: 50
        max-wait: 150000
  cache:
    redis:
      time-to-live: 3600000
```

3、RedisConfig 配置
```
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

  @Bean
  @Primary
  @Autowired
  public RedisTemplate redisTemplate(RedisConnectionFactory factory) {
    RedisTemplate template = new RedisTemplate<>();
    template.setConnectionFactory(factory);
    template.setDefaultSerializer(new JdkSerializationRedisSerializer());
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new JdkSerializationRedisSerializer());
    template.setHashValueSerializer(new JdkSerializationRedisSerializer());
    return template;
  }
}
```
4、 使用
```
  @Cacheable(value = "customer", key = "#id")
```
>value 必填  key当没有参数时 为0 一个参数时 为参数  多个参数时组合， **记得指定key！**

#### 2.0 使用template ####
1、 介绍
```
StringRedisTemplate与RedisTemplate
两者的关系是StringRedisTemplate继承RedisTemplate。

两者的数据是不共通的；也就是说StringRedisTemplate只能管理StringRedisTemplate里面的数据，
RedisTemplate只能管理RedisTemplate中的数据。

SDR默认采用的序列化策略有两种，一种是String的序列化策略，一种是JDK的序列化策略。

StringRedisTemplate默认采用的是String的序列化策略，保存的key和value都是采用此策略序列化保存的。

RedisTemplate默认采用的是JDK的序列化策略，保存的key和value都是采用此策略序列化保存的。
```
2、 配置
maven
```
    <!--redis缓存-->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
```
AbstractRedisUtil
```
public abstract class AbstractRedisUtil {

  protected RedisTemplate template;

  public Boolean delete(String key) {
    return template.delete(key);
  }

  public Long delete(Collection collection) {
    return template.delete(collection);
  }

  public boolean hasKey(String key) {
    return template.hasKey(key);
  }

  public Set<String> keys(String pattern) {
    return template.keys(pattern);
  }

  public <T> T get(String key) {
    return (T) template.opsForValue().get(key);
  }

  public void increment(String key, long delta) {
    template.opsForValue().increment(key, delta);
  }

  public <T> void set(String key, T value) {
    template.opsForValue().set(key, value);
  }

  public <T> void set(String key, T value, long timeout, TimeUnit timeUnit) {
    template.opsForValue().set(key, value, timeout, timeUnit);
  }

  /**
   * set map value with timeout <p> redis server version MUST BE higher than @2.6.0
   *
   * @param map String map
   */
  public void set(Map<String, String> map, long timeout, TimeUnit timeUnit) {
    long milliseconds = TimeUnit.MILLISECONDS.convert(timeout, timeUnit);
    template.executePipelined(
        new RedisCallback<Object>() {
          @Nullable
          @Override
          public Object doInRedis(RedisConnection connection) throws DataAccessException {
            StringRedisConnection con = (StringRedisConnection) connection;
            map.forEach((k, v) -> con.pSetEx(k, milliseconds, v));
            return null;
          }
        }
    );
  }

  public <K, V> Map<K, V> hentries(String key) {
    return template.<K, V>opsForHash().entries(key);
  }

  public <K, V> void hput(String key, K hashKey, V value) {
    template.opsForHash().put(key, hashKey, value);
  }

  public <K, V> void hputAll(String key, Map<K, V> map) {
    template.opsForHash().putAll(key, map);
  }

  public <K, V> V hget(String key, K hashKey) {
    return (V) template.<K, V>opsForHash().get(key, hashKey);
  }

  public <K> Long hdelete(String key, K... hashKeys) {
    return template.opsForHash().delete(key, hashKeys);
  }

  public <K> void hincrement(String key, K hashKey, long delta) {
    template.opsForHash().increment(key, hashKey, delta);
  }


  public <T> Long sadd(String key, T... values) {
    return template.opsForSet().add(key, values);
  }

  public <T> Set<T> smembers(String key) {
    return (Set<T>) template.opsForSet().members(key);
  }

  public <T> Boolean sisMember(String key, T value) {
    return template.opsForSet().isMember(key, value);
  }

  public <T> T spop(String key) {
    return (T) template.opsForSet().pop(key);
  }

  public <T> List<T> spop(String key, long count) {
    return (List<T>) template.opsForSet().pop(key, count);
  }

  public Long ssize(String key) {
    return template.opsForSet().size(key);
  }

  public <T> Long sremove(String key, T... values) {
    return template.opsForSet().remove(key, values);
  }

}

```
RedisUtil
```
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RedisUtil extends AbstractRedisUtil {

  @Autowired
  public void setTemplate(RedisTemplate<String, Object> template) {
    super.template = template;
  }


}
```
StringRedisUtil
```
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringRedisUtil extends AbstractRedisUtil {

  @Autowired
  public void setTemplate(StringRedisTemplate template) {
    super.template = template;
  }
}
```
3、 使用
**实体的序列化**
```
  private static final long serialVersionUID = 2427404465489542923L;
```
**注入**
```
  @Autowired
  private RedisUtil redis;
```

**具体的使用**
[redis序列化方式](https://blog.csdn.net/ruby_one/article/details/79141940)

**注意！**
貌似这个redisTemplate与spring 热部署  devTools冲突，故使用此功能时需要删除devTools或者[类加载器冲突原因以及解决办法](https://www.cnblogs.com/java-synchronized/p/7236446.html)
```
    <!--&lt;!&ndash;热部署与redisTemplate冲突&ndash;&gt;-->
    <!--<dependency>-->
      <!--<groupId>org.springframework.boot</groupId>-->
      <!--<artifactId>spring-boot-devtools</artifactId>-->
      <!--<scope>runtime</scope>-->
    <!--</dependency>-->
```

### 权限认证 ###
#### 1、 maven ####
```
    <!--JWT-->
    <dependency>
      <groupId>io.jsonwebtoken</groupId>
      <artifactId>jjwt</artifactId>
      <version>0.7.0</version>
    </dependency>

    <!--权限控制-->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <!--YAML 解析-->
    <dependency>
      <groupId>com.fasterxml.jackson.dataformat</groupId>
      <artifactId>jackson-dataformat-yaml</artifactId>
      <version>2.9.4</version>
    </dependency>
```
#### 2、 过程 ####
>用户登陆，会被AuthenticationProcessingFilter拦截，调用AuthenticationManager的实现，而且AuthenticationManager会调用ProviderManager来获取用户验证信息（不同的Provider调用的服务不同，因为这些信息可以是在数据库上，可以是在LDAP服务器上，可以是xml配置文件上等），如果验证通过后会将用户的权限信息封装一个User放到spring的全局缓存SecurityContextHolder中，以备后面访问资源时使用。 访问资源（即授权管理），访问url时，会通过AbstractSecurityInterceptor拦截器拦截，其中会调用FilterInvocationSecurityMetadataSource的方法来获取被拦截url所需的全部权限，在调用授权管理器AccessDecisionManager，这个授权管理器会通过spring的全局缓存SecurityContextHolder获取用户的权限信息，还会获取被拦截的url和被拦截url所需的全部权限，然后根据所配的策略（有：一票决定，一票否定，少数服从多数等），如果权限足够，则返回，权限不够则报错并调用权限不足页面

#### 3、 流程 ####
request----> filter----> Manager---->provider(parseToken)----> interceptor----->metasource--->decisionmanager



#### 4、 代码 ####
**按照上面的顺序**
**JwtUser**
```
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtUser {

  private String id;
  private String phone;
  private String platform;
  private List<String> rights;
  private Date expire;

}
```
**JwtUtil**
```
public class JwtUtil {

  // JWT security config
  public static final String TOKEN_PREFIX = "Bearer ";
  public static final String AUTH_HEADER = "Authorization";

  private static final String SECRET = "gbvvZeIigTktBc1QarwY5CQuxnveeT88FksvyvU14CXXItERdIRgD+012+ifnJZDl6wu/A/4ITNldut1UBNJdA==";
  private static final SignatureAlgorithm ALGORITHM = SignatureAlgorithm.HS512;
  private static final Key KEY = new SecretKeySpec(Base64.getDecoder().decode(SECRET),
      ALGORITHM.getJcaName());

  public static String create(JwtUser auth) {
    return TOKEN_PREFIX + Jwts.builder().setSubject(auth.getId())
        .claim("pn", auth.getPhone())
        .claim("pf", auth.getPlatform())
        .claim("rs", auth.getRights())
        .setExpiration(auth.getExpire()).signWith(ALGORITHM, KEY).compact();
  }

  public static JwtUser parse(String token) {
    if (token.startsWith(TOKEN_PREFIX)) {
      token = token.substring(TOKEN_PREFIX.length());
    } else {
      return null;
    }

    try {
      Claims claims = Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
      String id = claims.getSubject();
      String phone = (String) claims.get("pn");
      String platform = (String) claims.get("pf");
      List<String> rights = (List<String>) claims.get("rs");
      Date expire = claims.getExpiration();
      return new JwtUser(id, phone, platform, rights, expire);
    } catch (JwtException ex) {
      ex.printStackTrace();
      return null;
    }
  }

}
```
**JwtAuthenticationFiler**
```
@Component
public class JwtAuthenticationFilter extends RequestHeaderAuthenticationFilter {

  public JwtAuthenticationFilter() {
    this.setExceptionIfHeaderMissing(false);
    this.setPrincipalRequestHeader(JwtUtil.AUTH_HEADER);
  }

  @Lazy
  @Override
  @Autowired
  public void setAuthenticationManager(AuthenticationManager authenticationManager) {
    super.setAuthenticationManager(authenticationManager);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, AuthenticationException failed)
      throws IOException, ServletException {
    SecurityContextHolder.clearContext();
    request.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, failed);

//    WebSecurity.authFail(response, failed.getMessage());
  }

}
```
**JwtAuthentication**
```
@Getter
public class JwtAuthentication extends AbstractAuthenticationToken {

  private static final long serialVersionUID = -5887874329573955959L;

  private UserDetails principal;
  private String token;

  public JwtAuthentication(UserDetails principal, String jsonWebToken) {
    super(principal.getAuthorities());
    this.principal = principal;
    this.token = jsonWebToken;
    super.setAuthenticated(true);
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return principal;
  }
}
```
**JwtAuthenticationProvider**
```
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//    Authentication authenticatedUser;
    Authentication authenticatedUser = null;
    if (authentication.getClass().isAssignableFrom(PreAuthenticatedAuthenticationToken.class)
        && authentication.getPrincipal() != null) {
      String tokenHeader = (String) authentication.getPrincipal();
      UserDetails userDetails = parseToken(tokenHeader);
      if (userDetails != null) {
//        if (userDetails.getUsername() != null && BlackHouse.exist(userDetails.getUsername())) {
//          throw new AccountExpiredException(ResponseMsg.UNAUTHORIZED + "- Account Expired");
//        }
        authenticatedUser = new JwtAuthentication(userDetails, tokenHeader);
      } else {
//        throw new BadCredentialsException(ResponseMsg.UNAUTHORIZED + "- Bad Credentials");
      }
    } else {
      authenticatedUser = authentication;
    }
    return authenticatedUser;
  }

  private UserDetails parseToken(String tokenHeader) {
    UserDetails principal = null;
    JwtUser detail = JwtUtil.parse(tokenHeader);

    if (detail != null) {
      // sso
//      if (!SingleSignOn.check(detail.getId(), detail.getExpire())) {
//        throw new CredentialsExpiredException(ResponseMsg.SINGLE_SIGN_ON);
//      }

      List<GrantedAuthority> authorities = detail.getRights().stream()
          .filter(e -> e != null && !e.isEmpty())
          .map(e -> new SimpleGrantedAuthority(e)).collect(Collectors.toList());
      principal = new User(detail.getId(), "", authorities);
    }

    return principal;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.isAssignableFrom(PreAuthenticatedAuthenticationToken.class)
        || authentication.isAssignableFrom(JwtAuthentication.class);
  }

}
```
**WebSecurity**
```
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
```
**Group  rights**
```
/**
 * Role Group, keep immutable!
 */
@Getter
@ToString
@NoArgsConstructor
public class Group {

  private String id;
  private String name;
  private String urls;

}


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rights {

  @JsonProperty
  private List<Group> groups;

}
```

**Interceptor**
```
@Component
public class Interceptor extends AbstractSecurityInterceptor implements Filter {

  @Autowired
  private MetadataSource metadataSource;

  @Autowired
  @Override
  public void setAccessDecisionManager(AccessDecisionManager accessDecisionManager) {
    super.setAccessDecisionManager(accessDecisionManager);
  }

  @Autowired
  @Override
  public void setAuthenticationManager(AuthenticationManager authenticationManager) {
    super.setAuthenticationManager(authenticationManager);
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    FilterInvocation fi = new FilterInvocation(request, response, chain);
    invoke(fi);
  }

  private void invoke(FilterInvocation fi) throws IOException, ServletException {
    InterceptorStatusToken token = super.beforeInvocation(fi);
    try {
      fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
    } finally {
      super.afterInvocation(token, null);
    }
  }

  @Override
  public Class<? extends Object> getSecureObjectClass() {
    return FilterInvocation.class;
  }

  @Override
  public SecurityMetadataSource obtainSecurityMetadataSource() {
    return this.metadataSource;
  }

  @Override
  public void destroy() {
  }

  @Override
  public void init(FilterConfig filterconfig) throws ServletException {
  }

}
```
**MetadataSource**
```
@Component
public class MetadataSource implements FilterInvocationSecurityMetadataSource {

  private static Map<String, Collection<ConfigAttribute>> resourceMap = null;

  private Collection<ConfigAttribute> loadResourceDefine() {
    // currently not allow dynamic configuration
    if (resourceMap == null) {
      resourceMap = new HashMap<>();
      List<Group> groups = Constant.RIGHTS.getGroups();
      if (groups == null) {
        return new ArrayList<>();
      }

      for (Group group : groups) {
        ConfigAttribute ca = new SecurityConfig(group.getId());
        List<String> resources = Lists.newArrayList(group.getUrls().split(","));
        for (String url : resources) {
          if (resourceMap.containsKey(url)) {
            Collection<ConfigAttribute> value = resourceMap.get(url);
            value.add(ca);
            resourceMap.put(url, value);
          } else {
            Collection<ConfigAttribute> atts = new ArrayList<>();
            atts.add(ca);
            resourceMap.put(url, atts);
          }
        }
      }
    }
    return resourceMap.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
  }

  @Override
  public Collection<ConfigAttribute> getAllConfigAttributes() {
    return loadResourceDefine();
  }

  @Override
  public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
    HttpServletRequest req = ((FilterInvocation) object).getHttpRequest();
    for (String url : resourceMap.keySet()) {
      RequestMatcher requestMatcher = new AntPathRequestMatcher(url);
      if (requestMatcher.matches(req)) {
        return resourceMap.get(url);
      }
    }
    return null;
  }

  @Override
  public boolean supports(Class<?> arg0) {
    return true;
  }

}
```
**DecisionManager**
```
@Component
public class DecisionManager implements AccessDecisionManager {

  @Override
  public void decide(Authentication authentication, Object object,
      Collection<ConfigAttribute> configAttributes)
      throws AccessDeniedException, InsufficientAuthenticationException {
    if (configAttributes == null) {
      return;
    }
    for (ConfigAttribute ca : configAttributes) {
      String needRole;
      if ((needRole = ca.getAttribute()) != null) {
        needRole = needRole.trim();
        for (GrantedAuthority ga : authentication.getAuthorities()) {
          if (needRole.equals(ga.getAuthority().trim())) {
            return;
          }
        }
      }
    }
//    throw new AccessDeniedException(WebSecurity.UNAUTHORIZED);
  }

  @Override
  public boolean supports(ConfigAttribute attribute) {
    return true;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return true;
  }

}
```
**rights.yml**
```
groups:
#获取所有信息
#- id: 1
#  name: user
#  urls: /customers

#APP用户管理
- id: 2
  name: admin
  urls: /customers/**
```
**constant**
```
  static {
    try {
      RIGHTS = JsonUtil.YML_MAPPER.readValue(
          Constant.class.getResourceAsStream("/rights.yml"), Rights.class);
      Assert.notEmpty(RIGHTS.getGroups(), "Empty Rights!");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
```

### 异常处理 ###
#### 代码 ####












### Tips ###
#### 1、创建随机32位id ####
```
  public static String uuid() {
    return UUID.randomUUID().toString().replaceAll("-", "");
  }

```

#### 缓存时间格式配置 ####
配置如上代码中注释部分

其中JsonUtil为jackson转换时间
maven
```
    <!--处理LocalDateTime-->
    <dependency>
      <groupId>com.fasterxml.jackson.datatype</groupId>
      <artifactId>jackson-datatype-jsr310</artifactId>
      <version>2.9.4</version>
    </dependency>
```


