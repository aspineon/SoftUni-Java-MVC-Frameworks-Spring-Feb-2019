# Project: Resident Evil
[Task](https://github.com/Martin-BG/SoftUni-Java-MVC-Frameworks-Spring-Feb-2019/blob/master/04.%20Thymeleaf%20and%20Controllers/04.%20Thymeleaf%20and%20Controllers%20-%20Exercises.pdf) 
is to create a 5-page Spring Boot application with input forms, using Thymeleaf.

Project structure is similar to the one used in [EXODIA](https://github.com/Martin-BG/SoftUni-Java-MVC-Frameworks-Spring-Feb-2019/tree/master/02.%20Spring%20Essentials/Exercises/exodia), 
but greatly extended and improved by applying best practices and new techniques.

My [solution](https://github.com/Martin-BG/SoftUni-Java-MVC-Frameworks---Spring/tree/master/03.%20Java%20MVC%20Frameworks%20-%20Thymeleaf%20Engine/Resident%20Evil) from 2018 on the same task

___
## Setup
Tested on Ubuntu 18.0.4
### Versions
* Java **11.0.2**
* Spring Boot **2.1.3.RELEASE**
### Other tools:
* [ModelMapper](http://modelmapper.org/)
* [Lombok](https://projectlombok.org/)

System and IDE should be configured to use:
* [Java **11.0.2**](https://docs.oracle.com/cd/E19509-01/820-3208/inst_cli_jdk_javahome_t/) - [IntelliJ](https://stackoverflow.com/questions/18987228/how-do-i-change-the-intellij-idea-default-jdk)
* [mysql-connector-java **8.0.15**](https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-installing-classpath.html) - [IntelliJ](https://www.jetbrains.com/help/idea/connecting-to-a-database.html)

Configure IDE to recognize [Lombok](https://projectlombok.org/) - [instructions](https://projectlombok.org/setup/overview)
___
#### Project configuration
* [pom.xml](https://github.com/Martin-BG/SoftUni-Java-MVC-Frameworks-Spring-Feb-2019/blob/master/04.%20Thymeleaf%20and%20Controllers/Exercise/Resident%20Evil/pom.xml)
* [application.properties](https://github.com/Martin-BG/SoftUni-Java-MVC-Frameworks-Spring-Feb-2019/blob/master/04.%20Thymeleaf%20and%20Controllers/Exercise/Resident%20Evil/src/main/resources/application.properties) - 
customized **[MySQL8UnicodeDialect](https://github.com/Martin-BG/SoftUni-Java-MVC-Frameworks-Spring-Feb-2019/blob/master/04.%20Thymeleaf%20and%20Controllers/Exercise/Resident%20Evil/src/main/java/org/softuni/residentevil/config/MySQL8UnicodeDialect.java)**, 
**logging**, messages settings, **compression**, static resources [**caching**](https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#headers-cache-control) 
[**[1]**](https://stackoverflow.com/a/35640540/7598851)
[**[2]**](https://www.baeldung.com/cachable-static-assets-with-spring-mvc)
    ```properties
    #Enable response compression
    server.compression.enabled=true
    server.compression.mime-types=text/html,text/xml,text/plain,text/css,text/javascript,application/javascript,application/json,application/xml,image/x-icon
    server.compression.min-response-size=512B

    #Caching and versioning of static resources
    spring.resources.static-locations=classpath:/static/
    spring.resources.cache.period=365d
    spring.resources.cache.cachecontrol.max-age=365d
    spring.resources.chain.html-application-cache=true
    spring.resources.chain.enabled=true
    spring.resources.chain.strategy.content.enabled=true
    spring.resources.chain.strategy.content.paths=/**
    spring.resources.chain.compressed=true
    ```
___
## Takeaways
* Project structure is based on [EXODIA](https://github.com/Martin-BG/SoftUni-Java-MVC-Frameworks-Spring-Feb-2019/tree/master/02.%20Spring%20Essentials/Exercises/exodia) project:
  * [Customized MySQL dialect](https://stackoverflow.com/a/54993738/7598851) - changed default charset and collation 
  ```java
    public class MySQL8UnicodeDialect extends MySQL8Dialect {
    
        @Override
        public String getTableTypeString() {
            return " ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
        }
    }
  ```
  * Entities and view models without setters (getters are optional too) - better encapsulation and less boilerplate code
  * [Customized](https://github.com/Martin-BG/SoftUni-Java-MVC-Frameworks-Spring-Feb-2019/blob/master/04.%20Thymeleaf%20and%20Controllers/Exercise/Resident%20Evil/src/main/java/org/softuni/residentevil/config/ApplicationConfig.java) ModelMapper to support field mapping
  ```java
    @Configuration
    @EnableCaching
    public class ApplicationConfig {
        //..
        @Bean
        ModelMapper createModelMapper() {
            ModelMapper modelMapper = new ModelMapper();
    
            modelMapper.getConfiguration()
                    .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                    .setFieldMatchingEnabled(true);
    
            return modelMapper;
        }
    }
  ```
  * [Optimized](https://github.com/Martin-BG/SoftUni-Java-MVC-Frameworks-Spring-Feb-2019/blob/master/04.%20Thymeleaf%20and%20Controllers/Exercise/Resident%20Evil/src/main/java/org/softuni/residentevil/domain/entities/BaseUuidEntity.java) UUID primary keys representation in database - use BINARY(16) type instead of VARCHAR(36)
  ```java
    @Setter(AccessLevel.PRIVATE)
    @Getter
    @MappedSuperclass
    abstract class BaseUuidEntity extends BaseEntity<UUID> {
    
        @Id
        @GeneratedValue(generator = "uuid2")
        @GenericGenerator(name = "uuid2", strategy = "uuid2")
        @Column(unique = true, nullable = false, insertable = false, updatable = false, columnDefinition = "BINARY(16)")
        @Access(AccessType.PROPERTY)
        private UUID id;
    }
  ```
  * Exported common repository work logic to [BaseService](https://github.com/Martin-BG/SoftUni-Java-MVC-Frameworks-Spring-Feb-2019/blob/master/04.%20Thymeleaf%20and%20Controllers/Exercise/Resident%20Evil/src/main/java/org/softuni/residentevil/service/BaseService.java) class, using of generics for type-safety
  * Thymeleaf [templating](https://github.com/Martin-BG/SoftUni-Java-MVC-Frameworks-Spring-Feb-2019/tree/master/04.%20Thymeleaf%20and%20Controllers/Exercise/Resident%20Evil/src/main/resources/templates) and fragments
  * Custom [@Layout](https://github.com/Martin-BG/SoftUni-Java-MVC-Frameworks-Spring-Feb-2019/blob/master/04.%20Thymeleaf%20and%20Controllers/Exercise/Resident%20Evil/src/main/java/org/softuni/residentevil/web/annotations/Layout.java) annotation and [Interceptor](https://github.com/Martin-BG/SoftUni-Java-MVC-Frameworks-Spring-Feb-2019/blob/master/04.%20Thymeleaf%20and%20Controllers/Exercise/Resident%20Evil/src/main/java/org/softuni/residentevil/web/interceptors/ThymeleafLayoutInterceptor.java) for templating and fragments insert
  * Implemented Builder Pattern for [ThymeleafLayoutInterceptor](https://github.com/Martin-BG/SoftUni-Java-MVC-Frameworks-Spring-Feb-2019/blob/master/04.%20Thymeleaf%20and%20Controllers/Exercise/Resident%20Evil/src/main/java/org/softuni/residentevil/web/interceptors/ThymeleafLayoutInterceptor.java)
  ```java
    @Configuration
    public class WebConfig implements WebMvcConfigurer {
        //...
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(
                    ThymeleafLayoutInterceptor
                            .builder()
                            .withDefaultLayout("/layouts/default")
                            .withViewAttribute("view")
                            .withViewPrefix("/views/")
                            .build());
        }
    }
  ```
  * [Composite](https://github.com/Martin-BG/SoftUni-Java-MVC-Frameworks-Spring-Feb-2019/tree/master/04.%20Thymeleaf%20and%20Controllers/Exercise/Resident%20Evil/src/main/java/org/softuni/residentevil/domain/validation/annotations/composite) annotations for entity/model validation
  ```java
    @NotBlank(message = "{virus.name.blank}")
    @Size(message = "{virus.name.length}", min = ValidVirusName.MIN_LENGTH, max = ValidVirusName.MAX_LENGTH)
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
    @Retention(RUNTIME)
    @Constraint(validatedBy = {})
    @Documented
    public @interface ValidVirusName {
    
        int MIN_LENGTH = 3;
        int MAX_LENGTH = 10;
    
        String message() default "";
    
        Class<?>[] groups() default {};
    
        Class<? extends Payload>[] payload() default {};
    }
  
    @NotNull
    @Min(ValidVirusTurnoverRate.MIN)
    @Max(ValidVirusTurnoverRate.MAX)
    @ReportAsSingleViolation
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
    @Retention(RUNTIME)
    @Constraint(validatedBy = {})
    @Documented
    public @interface ValidVirusTurnoverRate {
    
      long MIN = 0L;
      long MAX = 100L;
    
      @OverridesAttribute(constraint = Min.class, name = "value") long min() default MIN;
    
      @OverridesAttribute(constraint = Max.class, name = "value") long max() default MAX;
    
      String message() default "{virus.turnover-rate.range}";
    
      Class<?>[] groups() default {};
    
      Class<? extends Payload>[] payload() default {};
    }

  ```
  * Validate parameters on repository methods to filter-out invalid requests
  * Use of transactions (read/write) for all public service methods to promote data integrity 
  and as optimization for methods that make multiple DB calls
  * Use [@SessionAttributes](https://github.com/Martin-BG/SoftUni-Java-MVC-Frameworks-Spring-Feb-2019/tree/master/04.%20Thymeleaf%20and%20Controllers/Exercise/Resident%20Evil/src/main/java/org/softuni/residentevil/web/controllers/virus/BaseVirusController.java) 
  for store and reuse of [@ModelAttribute](https://github.com/Martin-BG/SoftUni-Java-MVC-Frameworks-Spring-Feb-2019/tree/master/04.%20Thymeleaf%20and%20Controllers/Exercise/Resident%20Evil/src/main/java/org/softuni/residentevil/web/controllers/virus/AddVirusController.java) 
  by [Controller](https://github.com/Martin-BG/SoftUni-Java-MVC-Frameworks-Spring-Feb-2019/tree/master/04.%20Thymeleaf%20and%20Controllers/Exercise/Resident%20Evil/src/main/java/org/softuni/residentevil/web/controllers), 
  avoiding unnecessary Service/Repository calls for getting the same data
  * Use **caching** on selected [service](https://github.com/Martin-BG/SoftUni-Java-MVC-Frameworks-Spring-Feb-2019/tree/master/04.%20Thymeleaf%20and%20Controllers/Exercise/Resident%20Evil/src/main/java/org/softuni/residentevil/service/VirusServiceImpl.java) 
  methods (@EnableCaching + @CacheConfig + @Cacheable / @CacheEvict)
  ```java
    @Log
    @Service
    @Validated
    @Transactional
    @CacheConfig(cacheNames = {VirusServiceImpl.ALL_VIRUSES, VirusServiceImpl.VIRUSES})
    public class VirusServiceImpl extends BaseService<Virus, UUID, VirusRepository> implements VirusService {
    
        public static final String ALL_VIRUSES = "allVirusesCache";
        public static final String VIRUSES = "virusesCache";
    
        @Autowired
        public VirusServiceImpl(VirusRepository repository, ModelMapper mapper) {
            super(repository, mapper);
        }
    
        @Override
        protected Logger logger() {
            return log;
        }
    
        @Override
        @Cacheable(cacheNames = ALL_VIRUSES, sync = true)
        public List<VirusSimpleViewModel> getViruses() {
            return repository.findAllSimpleView();
        }
    
        @Override
        @CacheEvict(cacheNames = ALL_VIRUSES, allEntries = true)
        public void createVirus(@NotNull VirusBindingModel virus) {
            create(virus);
        }
    
        @Override
        @Cacheable(cacheNames = VIRUSES, key = "#id")
        public Optional<VirusBindingModel> readVirus(@NotNull UUID id) {
            return findById(id, VirusBindingModel.class);
        }
    
        @Override
        @Caching(evict = {
                @CacheEvict(cacheNames = ALL_VIRUSES, allEntries = true),
                @CacheEvict(cacheNames = VIRUSES, key = "#virus.id")})
        public void updateVirus(@NotNull VirusBindingModel virus) {
            if (repository.getOne(virus.getId()) != null) {
                create(virus);
            }
        }
    
        @Override
        @Caching(evict = {
                @CacheEvict(cacheNames = ALL_VIRUSES, allEntries = true),
                @CacheEvict(cacheNames = VIRUSES, key = "#id")})
        public void deleteVirus(@NotNull UUID id) {
            deleteById(id);
        }
    }
  ```
* Base Entity class equals and hashCode methods implemented as recommended by The Expert :)
```java
/**
 * Base Entity class
 * Defines equals() and hashCode() methods according to best practices by Vlad Mihalcea
 *
 * @see <a href="https://vladmihalcea.com/the-best-way-to-implement-equals-hashcode-and-tostring-with-jpa-and-hibernate/">
 * The best way to implement equals, hashCode, and toString with JPA and Hibernate</a>
 */
abstract class BaseEntity<I> implements Identifiable<I> {

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        return getId() != null && getId().equals(((Identifiable) o).getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
```
* Custom ENUM mapping in entities by [converters](https://github.com/Martin-BG/SoftUni-Java-MVC-Frameworks-Spring-Feb-2019/tree/master/04.%20Thymeleaf%20and%20Controllers/Exercise/Resident%20Evil/src/main/java/org/softuni/residentevil/domain/converters). <br>
Ex: Magnitude.LOW will be stored as "Low" in DB:
```java
public enum Magnitude {

    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High");

    private static final Map<String, Magnitude> LABEL_TO_ENUM_MAP = Stream.of(Magnitude.values())
            .collect(Collectors.toUnmodifiableMap(Magnitude::getLabel, sector -> sector));

    private final String label;

    Magnitude(String label) {
        this.label = label;
    }

    public static Magnitude fromLabel(String label) {
        return label == null ? null : LABEL_TO_ENUM_MAP.get(label);
    }

    public String getLabel() {
        return label;
    }
}

@Converter
public class MagnitudeConverter implements AttributeConverter<Magnitude, String> {

    @Override
    public String convertToDatabaseColumn(Magnitude magnitude) {
        return magnitude == null ? null : magnitude.getLabel();
    }

    @Override
    public Magnitude convertToEntityAttribute(String label) {
        return Magnitude.fromLabel(label);
    }
}

@Entity
@Table(name = "viruses")
public class Virus extends BaseUuidEntity {
    //...
    @Convert(converter = MagnitudeConverter.class)
    private Magnitude magnitude;
}
```
* Custom date/time format binding with Spring's @DateTimeFormat:
```java
public class VirusBindingModel implements Bindable<Virus> {
    // ...
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date releasedOn;
}
```
* Custom HQL queries with direct mapping to view models to avoid unnecessary data pooling, especially for related entities:
```java
@Repository
public interface VirusRepository extends JpaRepository<Virus, UUID> {

    @Query(value = "SELECT " +
            "NEW org.softuni.residentevil.domain.models.view.virus.VirusSimpleViewModel(" +
            "v.id, v.name, v.magnitude, v.releasedOn) " +
            "FROM Virus AS v " +
            "ORDER BY v.releasedOn ASC")
    List<VirusSimpleViewModel> findAllSimpleView();
}

@Getter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
public final class VirusSimpleViewModel implements Viewable<Virus>, Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    private String name;

    private Magnitude magnitude;

    private Date releasedOn;
}
```
* Simplified [controllers](https://github.com/Martin-BG/SoftUni-Java-MVC-Frameworks-Spring-Feb-2019/tree/master/04.%20Thymeleaf%20and%20Controllers/Exercise/Resident%20Evil/src/main/java/org/softuni/residentevil/web/controllers) 
by moving of common logic to parent classes and using of Spring's goodies like @SessionAttributes
```java
@Layout
@Controller
@RequestMapping("/viruses/edit/{id}")
public class EditVirusController extends BaseVirusController {

    @Autowired
    public EditVirusController(VirusService virusService,
                               CapitalService capitalService) {
        super(virusService, capitalService);
    }

    @ModelAttribute(CAPITALS)
    public List<CapitalSimpleViewModel> capitalSimpleViewModelList() {
        return capitalService.getCapitals();
    }

    @GetMapping
    public String get(@PathVariable UUID id, Model model) {
        return virusService
                .findById(id, VirusBindingModel.class)
                .map(virus -> {
                    model.addAttribute(VIRUS, virus);
                    return "virus/edit";
                })
                .orElseThrow();
    }

    @PutMapping
    public String put(@PathVariable UUID id,
                      @Valid @ModelAttribute(VIRUS) VirusBindingModel virus,
                      Errors errors,
                      SessionStatus sessionStatus) {
        if (id.equals(virus.getId())) {
            if (errors.hasErrors()) {
                return "virus/edit";
            }
            virusService.create(virus);
        }

        sessionStatus.setComplete();

        return redirect("/viruses");
    }
}
```
* Use of [converters](https://github.com/Martin-BG/SoftUni-Java-MVC-Frameworks-Spring-Feb-2019/tree/master/04.%20Thymeleaf%20and%20Controllers/Exercise/Resident%20Evil/src/main/java/org/softuni/residentevil/web/converters) 
for mapping of custom types between Spring controllers and Thymeleaf:
```java
@Validated
public class StringToUuidConverter implements Converter<String, UUID> {

    @Override
    public UUID convert(@NotNull String id) {
        return UUID.fromString(id);
    }
}

@Configuration
public class WebConfig implements WebMvcConfigurer {
    // ...

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToUuidConverter());
    }
}
```
* Use text messages from external [messages.properties](https://github.com/Martin-BG/SoftUni-Java-MVC-Frameworks-Spring-Feb-2019/blob/master/04.%20Thymeleaf%20and%20Controllers/Exercise/Resident%20Evil/src/main/resources/languages/messages.properties) file:
```properties
#Configuration in application.properties:

#messages
spring.messages.basename=languages/messages
spring.messages.encoding=UTF-8
spring.messages.cache-duration=3600s

#MVC
spring.mvc.locale=en_US

#resources/languages/messages.properties
application.title=Viruses
nav-bar.title=Resident Evil
```
```html
<th:block th:fragment="head" xmlns:th="http://www.thymeleaf.org">
    <!-- ... -->
    <title th:text="#{application.title}"/>
</th:block>
```
* **Thymeleaf** specific:
  * Access to constants:
    ```html
        action= @{__${T(org.softuni.residentevil.config.WebConfig).URL_VIRUS_ADD}__}
    ```
  * Reuse of Thymeleaf fragments with params:
    ```html
    <!--add.html-->
    <th:block xmlns:th="http://www.thymeleaf.org"
              th:replace="~{fragments/virus-form :: virusForm(
         action= @{__${T(org.softuni.residentevil.config.WebConfig).URL_VIRUS_ADD}__},
         title = #{viruses.add.title},
         method= 'post',
         buttonText = #{viruses.add.button}
         )}"/>
         
    <!--edit.html-->
    <th:block xmlns:th="http://www.thymeleaf.org"
              th:replace="~{fragments/virus-form :: virusForm(
         action= @{__${T(org.softuni.residentevil.config.WebConfig).URL_VIRUS_EDIT}__},
         title = #{viruses.edit.title},
         method= 'put',
         buttonText = #{viruses.edit.button}
         )}"/>
    
    <!--delete.html-->
    <th:block xmlns:th="http://www.thymeleaf.org"
              th:replace="~{fragments/virus-form :: virusForm(
         action= @{__${T(org.softuni.residentevil.config.WebConfig).URL_VIRUS_DELETE}__},
         title = #{viruses.delete.title},
         method= 'delete',
         buttonText = #{viruses.delete.button}
         )}"/>
     
    <!--fragments/virus-form.html-->
    <th:block th:fragment="virusForm (action, title, method, buttonText)"
              xmlns:th="http://www.thymeleaf.org">
        <form class="mt-5 center-block w-75 mx-auto"
              th:method="${method}"
              th:action="${action}"
              th:object="${__${T(org.softuni.residentevil.web.controllers.virus.BaseVirusController).VIRUS}__}">
    
            <input hidden name="id" th:value="*{id}">
    
            <h2 class="header mt-4 mb-4" th:text="${title}"/>
            <!--...-->
            <div class="form-actions mx-auto text-center">
                <button class="btn re-color my-button" th:text="${buttonText}" type="submit"/>
            </div>
        </form>
    </th:block>
    ```
  * Custom **LocalDate** format in Thymeleaf:
    ```html
    <td th:text="${#temporals.format(virus.getReleasedOn(), 'dd-MMM-yyyy')}"/>
    ```
  * Custom **Date** format in Thymeleaf:
    ```html
    <td th:text="${#dates.format(virus.getReleasedOn(), 'dd-MMM-yyyy')}"/>
    ```
  * Indexing in collection:
    ```html
    <th:block th:each="virus, iStat : ${viruses}" th:object="${virus}">
        <p scope="row" th:text="${iStat.index + 1}"/>
    </th:block>
    ```
  * href construct with param:
    ```html
    <th:block th:each="virus, iStat : ${viruses}" th:object="${virus}">
        <tr>
            <td>
                <a class="btn btn-light btn-sm border-dark"
                   th:href="@{/viruses/edit/{id}(id=*{id})}"
                   th:text="#{viruses.all.table-button.edit}"/>
            </td>
        </tr>
    </th:block>
    ```
  * Simulate PUT, DELETE, PATCH HTTP methods on form submit:
      ```html
      <form th:action="@{/viruses/delete}" th:method="delete">
          <input hidden name="id" th:value="*{id}">
          <button class="btn btn-light btn-sm border-dark"
                  th:text="#{viruses.all.table-button.delete}"
                  type="submit"/>
      </form>
  
      Actually it is a POST request, as HTML forms natively support only GET and POST methods.
      Thymeleaf and Spring offer a workaround for that, so the request is processed as DELETE one in controller:
      
      <form th:action="@{/viruses/delete}" th:method="delete">
          <input name="id" hidden th:value="*{id}">
          <button class="btn btn-light btn-sm border-dark" th:text="#{viruses.all.table-button.delete}"
                  type="submit"/>
      </form>
      
      The above code produces an extra hidden input in resulting HTML:
      
      <form action="/viruses/delete" method="post">
          <input type="hidden" name="_method" value="delete">
          <input name="id" hidden="" value="0292414b-a2a8-441e-b017-26cfb686fd3e">
          <button class="btn btn-light btn-sm border-dark" type="submit">Delete</button>
      </form>
      
      In turn the request is treated as DELETE one at the backend:
      
      @DeleteMapping
      public String delete(@RequestParam UUID id) {
          service.deleteById(id);
          return redirect("/viruses");
      }
      ```
* Define validation constraints as constants into [composite validation annotations](https://github.com/Martin-BG/SoftUni-Java-MVC-Frameworks-Spring-Feb-2019/tree/master/04.%20Thymeleaf%20and%20Controllers/Exercise/Resident%20Evil/src/main/java/org/softuni/residentevil/domain/validation/annotations/composite) 
and use these for setting up entities columns, ensuring integrity between actual validation and DB constraints.
```java
@NotBlank(message = "{virus.name.blank}")
@Size(message = "{virus.name.length}", min = ValidVirusName.MIN_LENGTH, max = ValidVirusName.MAX_LENGTH)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface ValidVirusName {

    int MIN_LENGTH = 3;
    int MAX_LENGTH = 10;

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

@Entity
@Table(name = "viruses")
public class Virus extends BaseUuidEntity {

    @ValidVirusName
    @Column(unique = true, nullable = false, length = ValidVirusName.MAX_LENGTH)
    private String name;
    
    @ValidVirusDescription
    @Column(nullable = false, columnDefinition = "TEXT", length = ValidVirusDescription.MAX_LENGTH)
    private String description;
    
    @ValidVirusSideEffects
    @Column(length = ValidVirusSideEffects.MAX_LENGTH)
    private String sideEffects;
    
    @ValidVirusCreator
    @Convert(converter = CreatorConverter.class)
    @Column(nullable = false, length = ValidVirusCreator.MAX_LENGTH)
    private Creator creator;
    
    // ...
}
```
* Use validation messages from external [validation.properties](https://github.com/Martin-BG/SoftUni-Java-MVC-Frameworks-Spring-Feb-2019/blob/master/04.%20Thymeleaf%20and%20Controllers/Exercise/Resident%20Evil/src/main/resources/languages/validation.properties) file:
```java
@Configuration
@EnableCaching
public class ApplicationConfig {
    //...
    
    /**
     * Configure Validator to use validation messages from custom file
     */
    @Bean
    public Validator validator() {
        final String LANGUAGES_VALIDATION_MESSAGES = "languages/validation";

        return Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(
                        new ResourceBundleMessageInterpolator(
                                new PlatformResourceBundleLocator(LANGUAGES_VALIDATION_MESSAGES)
                        )
                )
                .buildValidatorFactory()
                .getValidator();
    }
}

@NotBlank(message = "{virus.name.blank}")
@Size(message = "{virus.name.length}", min = ValidVirusName.MIN_LENGTH, max = ValidVirusName.MAX_LENGTH)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface ValidVirusName {

    int MIN_LENGTH = 3;
    int MAX_LENGTH = 10;

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
```
```properties
#languages/validation.properties
virus.name.blank=Virus name is required and cannot be blank string (empty characters on both sides ignored)
virus.name.length=Virus name length should be between {min} and {max} symbols
```
* Trim string values from input forms with **@InitBinder** and **StringTrimmerEditor**.
Further [customize](https://github.com/Martin-BG/SoftUni-Java-MVC-Frameworks-Spring-Feb-2019/blob/master/04.%20Thymeleaf%20and%20Controllers/Exercise/Resident%20Evil/src/main/java/org/softuni/residentevil/web/controllers/BaseController.java) 
this functionality to prevent trimming of certain fields like **"password"** 
or treating empty texts as null, by protected methods allowing redefinition of the rules by child classes.
```java
public class BaseController {
    //... javadoc omitted too

    private static void preventTextModificationForFields(WebDataBinder binder, List<String> doNotTrimFieldsList) {
        PropertyEditorSupport noTrimPropertyEditor = new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                super.setValue(text);
            }
        };

        doNotTrimFieldsList.forEach(field ->
                binder.registerCustomEditor(String.class, field, noTrimPropertyEditor));
    }

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(isEmptyInputStringsNull()));

        preventTextModificationForFields(binder, getUnmodifiedTextFieldsList());
    }

    protected List<String> getUnmodifiedTextFieldsList() {
        return List.of();
    }

    protected boolean isEmptyInputStringsNull() {
        return false;
    }
}
```
* Use **Javadoc** whenever it is appropriate to ([config files](https://github.com/Martin-BG/SoftUni-Java-MVC-Frameworks-Spring-Feb-2019/blob/master/04.%20Thymeleaf%20and%20Controllers/Exercise/Resident%20Evil/src/main/java/org/softuni/residentevil/config/ApplicationConfig.java), 
[protected methods](https://github.com/Martin-BG/SoftUni-Java-MVC-Frameworks-Spring-Feb-2019/blob/master/04.%20Thymeleaf%20and%20Controllers/Exercise/Resident%20Evil/src/main/java/org/softuni/residentevil/web/controllers/BaseController.java), 
[base classes](https://github.com/Martin-BG/SoftUni-Java-MVC-Frameworks-Spring-Feb-2019/blob/master/04.%20Thymeleaf%20and%20Controllers/Exercise/Resident%20Evil/src/main/java/org/softuni/residentevil/web/controllers/BaseController.java), 
[interfaces](https://github.com/Martin-BG/SoftUni-Java-MVC-Frameworks-Spring-Feb-2019/blob/master/04.%20Thymeleaf%20and%20Controllers/Exercise/Resident%20Evil/src/main/java/org/softuni/residentevil/domain/api/Bindable.java) 
etc.)
* [Generic repository](https://github.com/Martin-BG/SoftUni-Java-MVC-Frameworks-Spring-Feb-2019/blob/master/04.%20Thymeleaf%20and%20Controllers/Exercise/Resident%20Evil/src/main/java/org/softuni/residentevil/repository/GenericRepository.java) - 
provides generic methods for work with [projections](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#projections):
```java
@Validated
@NoRepositoryBean
public interface GenericRepository<E, I> extends JpaRepository<E, I> {

    <T extends Viewable<E>> Optional<T> findProjectedById(@NotNull I id, @NotNull Class<T> projection);

    <T extends Viewable<E>> List<T> findAllProjectedBy(@NotNull Class<T> projection);

    <T extends Viewable<E>> List<T> findAllProjectedBy(@NotNull Class<T> projection, @NotNull Sort sort);
}

@Validated
@Repository
public interface CapitalRepository extends GenericRepository<Capital, Long> {
    // ..
}

public interface CapitalSimpleViewProjection extends Viewable<Capital> {

    Long getId();

    String getName();
}

@Log
@Service
@Validated
@Transactional
@CacheConfig(cacheNames = "capitals")
public class CapitalServiceImpl extends BaseService<Capital, Long, CapitalRepository> implements CapitalService {

    // ...

    @Override
    @Cacheable(sync = true)
    @Transactional(readOnly = true)
    public List<CapitalSimpleViewProjection> getCapitals() {
        return repository.findAllProjectedBy(CapitalSimpleViewProjection.class, Sort.by(Sort.Direction.ASC, "name"));
    }
}
``` 
___
#### Notes to myself
* Implement **Serializable** interface by all entities and models as these could be cached or saved in Session
* **LocalDate** is not **Serializable** friendly and requires extra setup, depending on needs. **Date** can be used instead
* [@EnableTransactionManagement](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/transaction/annotation/EnableTransactionManagement.html) is not required to be explicitly used as it is implicitly [enabled](https://stackoverflow.com/questions/40724100/enabletransactionmanagement-in-spring-boot) by Spring Boot + Spring JPA
* For ManyToMany relations recommended collection is Set instead of a List because with List on each add or remove all elements are removed and then added back:
  * [The best way to use the @ManyToMany annotation with JPA and Hibernate](https://vladmihalcea.com/the-best-way-to-use-the-manytomany-annotation-with-jpa-and-hibernate) by Vlad Mihalcea
  * [JPA many-to-many update efficiency](https://bianjp.com/posts/2017/10/31/jpa-many-to-many-update-efficiency)
* Projections (proxy objects by nature) are not serializable and cannot be stored in HTTP/Spring sessions or Redis cache.
* Disable Tomcat session save/restore on application restart:
  ```java
    @Configuration
    public class ApplicationConfig {
        // ...
        /**
         * Disable HTTP Session data save and restore by Tomcat on application restart.
         *
         * @see <a href="https://stackoverflow.com/questions/27130157/how-to-disable-tomcat-session-persistence-in-spring-boot-via-manager-pathname/51042982#51042982">Stack Overflow</a>
         */
        @Bean
        public TomcatServletWebServerFactory servletContainer() {
            TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
    
            tomcat.addContextCustomizers((TomcatContextCustomizer) context -> {
                if (context.getManager() instanceof StandardManager) {
                    ((StandardManager) context.getManager()).setPathname("");
                }
            });
    
            return tomcat;
        }
    }
  ```
* Enable/Disable session data save/restore on application restart (disabled by default, **Dev Tools** enables it):
  ```properties
    #application.properties
    server.servlet.session.persistent=true
  ```
* Enable Hibernate performance meter - application.properties
  ```properties
    # Hibernate performance meter - use only for testing!!!
    spring.jpa.properties.hibernate.generate_statistics=true
    logging.level.org.hibernate.stat=DEBUG
  ```
  ```log
    2019-03-23 01:25:31.491  INFO 6744 --- [nio-8000-exec-4] i.StatisticalLoggingSessionEventListener : Session Metrics {
        56666 nanoseconds spent acquiring 1 JDBC connections;
        0 nanoseconds spent releasing 0 JDBC connections;
        100749 nanoseconds spent preparing 1 JDBC statements;
        685245 nanoseconds spent executing 1 JDBC statements;
        0 nanoseconds spent executing 0 JDBC batches;
        0 nanoseconds spent performing 0 L2C puts;
        0 nanoseconds spent performing 0 L2C hits;
        0 nanoseconds spent performing 0 L2C misses;
        0 nanoseconds spent executing 0 flushes (flushing a total of 0 entities and 0 collections);
        11334 nanoseconds spent executing 1 partial-flushes (flushing a total of 0 entities and 0 collections)
    }
  ```
* Enable JDBC (MySQL) stored [Spring Session](https://docs.spring.io/spring-session/docs/current/reference/html5/), 
[more](https://pupli.net/2017/03/06/how-to-configure-spring-session-with-jdbc-for-mysql/):
  ```xml
  <!--pom.xml-->
    <dependency>
        <groupId>org.springframework.session</groupId>
        <artifactId>spring-session-jdbc</artifactId>
    </dependency>
  ```
  ```properties
    #application.properties

    #Spring Session
    spring.session.store-type=jdbc
    spring.session.jdbc.initialize-schema=always
    spring.session.jdbc.schema=classpath:org/springframework/session/jdbc/schema-mysql.sql
    spring.session.jdbc.table-name=SPRING_SESSION
    #Check and delete expired sessions every 5 minutes
    spring.session.jdbc.cleanup-cron=0 0/5 * * * *
    #Session duration - 1 hour
    spring.session.timeout=60m
  ```
* [Cache with Redis](https://www.kevinhooke.com/2018/04/22/caching-spring-boot-restcontroller-responses-with-spring-cache-and-redis/)
