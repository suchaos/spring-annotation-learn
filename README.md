# Spring 驱动注解开发

> 具体代码可以到 github spring-annotation-learn 这个项目中去看

### 1. 给容器中注册组件

1. 包扫描 + 组件标注注解（`@Component/@Controller/@Service/@Repository`）

   * 可以自定义 `Filter`
   * 这种方式局限于自己写的类，第三方包中的类无法注册
   * 这种注册方式，bean 名字就是类名小写

2. `@Bean` 

   * 导入的第三方包里面的组件
   * id 默认是用方法名作为 id

3. `@Import`

   * 快速地给容器中导入一个组件
   * 使用方式：
     * `@Import(要导入的 bean)`：容器中就会有自动注册这个组件，id 默认是组件的全类名
     * `@Import(要导入的配置类)`：容器中就会有自动注册这个配置类中的组件
     * `@Import(ImportSelector)`：返回需要导入的组件的全类名数组
     * `@Import(ImportBeanDefinitionRegistrar)`：手动注册 bean 到容器中

4. 使用 Spring 提供的 `FactoryBean`（工厂 Bean）

   * 实现 `FactoryBean` 接口

   * 将实现的类作为 @Bean 注册（实际上注册的是 `getObject()` 返回的对象 ）

   * 默认得到的是 工厂 Bean 调用 `getObject` 创建的对象

   * 使用 `getBean("&id")` 可以得到这个 `FactoryBean` 本身

     > `BeanFactory#FACTORY_BEAN_PREFIX` 可以看到 `&` 这个的定义

   > A bean that implements this interface cannot be used as a normal bean. A FactoryBean is defined in a bean style, but the object exposed for bean references (getObject()) is always the object that it creates.
   >
   > `FactoryBean`其实也是一个 Bean，不过和普通 Bean 不同的是，它是通过 getObject() 这个方法来暴露一个 Bean 去进行注册
   >
   > TODO：感觉和 工厂模式 类似，注意一下

默认加载 IoC 容器中的组件，容器启动会调用无参构造器创建对象，再进行初始化赋值等操作

### 2. bean 的生命周期：bean 创建 --- 初始化 --- 销毁的过程

容器管理 bean 的生命周期
我们可以自定义初始化和销毁方法， 容器在 bean 进行到当前生命周期的时候来调用我们自定义的初始化和销毁方法



构建（对象创建）

* 单实例：在容器启动的时候创建对象
* 多实例：在每次获取的时候创建对象

初始化：

* 对象创建完成，并赋值好，调用初始化方法

销毁：

* 对于单实例：容器关闭的时候
* 对于多实例：容器不会管理整个 bean，容器不会调用销毁方法

#### 指定初始化和销毁的方法

2. `@Bean(initMethod = "init", destroyMethod = "destory")` 或者 xml 中定义
3. 通过让 bean 实现 ` InitializingBean ` 这个接口，来定义初始化逻辑；销毁则是实现 ` DisposableBean ` 接口，来定义销毁逻辑
4. 可以使用  JSR250 规范定义的注解 
   * `@PostConstruct`：在 bean 创建完成并且属性赋值完成，来执行初始化
   * `@PreDestroy`：在容器销毁 bean 之前，通知我们进行清理工作
5. ` BeanPostProcessor `：bean 的后置处理器，在 bean 初始化前后进行一些清理工作
   * `postProcessBeforeInitialization `：在初始化之前工作
   * `postProcessAfterInitialization`：在初始化之后工作

#### Spring Bean 的生命周期

- 创建过程：
  - 实例化 Bean（这里创建的不完整的 Bean 会提前暴露出去，这是解决循环依赖的关键）
  - 设置 Bean 属性
  - Aware（注入 Bean ID、BeanFactory、AppCtx）
  - BeanPostProcessor.postProcessBeforeInitialization
  - 执行初始化自定义方法 
    - @PostConstruct 方法 
    - InitializingBean.afterPropertiesSet，实现 InitializingBean 接口（官方文档不推荐使用）
    - 定制化的 Bean init 方法（initMethod = "init"）
  - BeanPostProcessor.postProcessAfterInitialization
  - 创建完成，已经可以使用 Bean 了

- 销毁过程：
  - @PreDestroy 方法
  - 如果实现了 DisposableBean 接口，就会调用 destory 方法
  - 若配置了 destry-method 属性，就会调用其配置的销毁方法

##### 源码记录：

> 设置 Bean 属性是在 `AbstractAutowireCapableBeanFactory#doCreateBean` 中的 `populateBean(beanName, mbd, instanceWrapper)`

> Aware 底层也是通过 BeanPostProcessor 来处理的

> `AbstractAutowireCapableBeanFactory#initializeBean()` 方法包括了从上面步骤中的 Aware 到 创建完成
>
> 其中初始化在 `AbstractAutowireCapableBeanFactory#initializeBean()` 中的 `invokeInitMethods` 调用

> BeanPostProcessor 具体调用
>
> 遍历得到容器中所有的 BeanPostProcessor，挨个执行 `postProcessBeforeInitialization` 一旦其中一个返回 null，跳出循环，不会执行后面的 BeanPostProcessor
>
> `AbstractAutowireCapableBeanFactory#applyBeanPostProcessorsBeforeInitialization`

##### BeanPostProcessor  在 Spring 底层的应用

* `ApplicationContextAwareProcessor` 是一个 BeanPostProcessor ，负责给实现了 Aware 接口的类赋值（BeanPostProcessor  之前不是已经赋值了吗？？？TODO：记得看一下源码）
* `BeanValidationPostProcessor` 是一个 BeanPostProcessor 
* `InitDestroyAnnotationBeanPostProcessor` 是一个 BeanPostProcessor ，就是 @PostConstruct 方法  和  @PreDestroy 方法 起作用的根源
* `AutowiredAnnotationBeanPostProcessor` 是一个 BeanPostProcessor，是`@Autowired` 起作用的根源
* `AsyncAnnotationBeanPostProcessor` 是一个 BeanPostProcessor，处理 ` @Async`



### 3. 属性赋值

#### @Value 赋值方式：

          1. 基本数值
          2. SpEL, #{}
          3. ${}, 取出 Environment 中的属性值
             * 使用 `@PropertySource` 读取外部配置文件中的 k/v 保存到运行的环境中
             * `@PropertySource(value = {"classpath:student.properties"}, encoding = "UTF-8")`



### 4. 自动装配 Autowired

Spring 利用依赖注入（DI），完成对 IoC 容器中各个组件的依赖关系赋值

> 底层是`AutowiredAnnotationBeanPostProcessor` ：解析完成自动装配功能的

1. `@Autowired` 自动注入

   1. 默认有限按照类型去容器中找对应的组件，找到，并且只有一个就赋值
   2. 如果找到多个相同类型的组件，再将声明这个属性的名称作为组件的 id 去容器中查找

   > `@Autowired BookDao bookDao`  就是先按照 `getBean(BookDao.class)` 去找，如果找到多个的话，再使用 getBean("bookDao") 这个名字去找

   3. `@Qualifier("xxx")` ：结合 `@Autowired` 一起使用，明确指定需要装配的组件的 id，而不是使用属性名
   4. 自动装配默认一定要将属性赋值好，没有就会报错；可以使用 `@Autowired(required = false)`，如果没找到就不装，不对属性进行赋值了
   5. `@Primary`：让 Spring 进行自动装配的时候，默认使用首选的 bean（当然，如果使用 `@Qualifier` 明确指定 id，还是回去找明确指定的那个去）

2. Spring 还支持 `@Resource` JSR250 和 `@Inject` JSR330，是属于 Java 规范的，`@Autowired` 是 Spring 定义的

   * `@Resource` 
     * 可以和  `@Autowired` 一样实现自动装配功能；
     * 默认是按照组件名称进行装配的，也可以使用 `@Resource(name=)`属性
     * 没有能支持 `@Primary` 功能，也没有 `(required = false)`这样的功能
   * `@Inject`：
     * 需要 `javax.inject` 依赖
     * 支持 `@Primary` 功能
     * 没有 `(required = false)`这样的功能

3. `@Autowired`：构造器，参数，方法，属性；都是从容器中获取参数组件的值

   1. 标注在方法位置
      1. Spring 容器创建当前对象，就会调用方法，完成赋值
      2. 方法使用的参数，自定义类型的值从 ioc 容器中获取
      3. `@Bean` 标注的方法创建对象的时候，方法参数的值从容器中获取，`@Autowired`（标注在方法或者参数）是可以省略的
   2. 标注在有参构造器位置
      1. 构造器要用的组件，都是从容器中获取
      2. 而且如果组件只有一个有参构造器，`@Autowired`（标注在方法或者参数）是可以省略的，该位置的组件还是可以自动从容器中获取的
   3. 标注在参数位置

4. 自定义组件想要使用 Spring 容器底层的一些组件，比如 （ApplicationContext, BeanFactory, xxx 等），自定义组件实现 xxxAware 接口；在创建对象的时候，会调用接口规定的方法注入相关组件。把 Spring 底层一些组件注入到自定义的 bean 中去

   * `xxxAware` 是通过 `xxxProcessor` 来处理的
   * `ApplicationContextAware` 接口是通过 `ApplicationContextAwareProcessor` 这个 `BeanPostProcessor` 来处理的

5. `@Profile`：Spring 为我们提供的可以根据当前环境，动态的激活和切换一系列组件的功能；指定组件在哪个环境的情况下才能被注册到容器中，不指定，任何情况下都能注册这个组件

   * 比如，开发环境，测试环境，生产环境的数据源：分别使用不同的数据源

   1. 加了环境标识的 bean，只有这个环境被激活才能注册到容器中，默认是 default 环境

   2. 如何切换环境：

      1. 使用命令行动态参数的方式：在虚拟机参数位置加， `-Dspirng.profiles.active=test`

      2. 使用代码的方式：

         ```java
         // 1. 创建一个 applicationContext，使用无参构造函数
         AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
         // 2. 设置需要激活的环境
         applicationContext.getEnvironment().setActiveProfiles("test", "dev");
         // 3. 注册主配置类
         applicationContext.register(MainConfigOfProfile.class);
         // 4. 启动刷新容器
         applicationContext.refresh();
         ```

      3. 如果写在配置类上，只有是指定的环境的时候，整个配置类里面的所有配置才能开始生效

      4. 没有标注环境标识的 bean，任何环境都是加载