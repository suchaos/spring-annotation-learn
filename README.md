# Spring 驱动注解开发

> 具体代码可以到 github spring-annotation-learn 这个项目中去看

## IoC 相关注解

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
2. 通过让 bean 实现 ` InitializingBean ` 这个接口，来定义初始化逻辑；销毁则是实现 ` DisposableBean ` 接口，来定义销毁逻辑
3. 可以使用  JSR250 规范定义的注解 
   * `@PostConstruct`：在 bean 创建完成并且属性赋值完成，来执行初始化
   * `@PreDestroy`：在容器销毁 bean 之前，通知我们进行清理工作
4. ` BeanPostProcessor `：bean 的后置处理器，在 bean 初始化前后进行一些清理工作
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



## AOP 相关注解

AOP：指在程序运行期间动态的将某段代码切入到指定方法指定位置进行运行的编程方式

### AOP 使用步骤：

1. pom 导入 Spring AOP 模块 `spring-aspects`
2. 定义一个业务逻辑类：在业务逻辑运行的时候（方法之前，方法运行结束，方法出现异常等）执行一些方法（比如打印日志等）
3. 定义一个切面类，比如日志切面类：切面类里面的方法需要动态感知业务逻辑类的方法运行到哪里，然后执行不同的方法
   * 通知方法：（`JoinPoint` 这个参数如果出现的话，一定要出现在参数表的第一位）
     * 前置通知 `@Before`：在目标方法运行之前运行
     * 后置通知 `@After`：在目标方法运行结束之后运行（无论方法正常结束还是异常结束）
     * 返回通知 `@AfterReturing`：在目标方法正常返回之后运行
     * 异常通知 `@AfterThrowing`：在目标方法出现异常之后运行
     * 环绕通知 `@Around`：动态代理，手动推进目标方法运行`proceedingJoinPoint.procced(proceedingJoinPoint.getArgs())`
4. 给切面类的目标方法标注何时何地运行（也就是标注通知注解）
   * `@Pointcut` 抽取切入点表达式，写法格式可以看[官网文档]( https://docs.spring.io/spring/docs/5.2.x/spring-framework-reference/core.html#aop-pointcuts )
     * 如果在本类引用，直接写方法名称加小括号
     * 如果在其他类引用，必须写全限定名加小括号
5. 将切面类和目标方法所在的类（业务逻辑类）加入到容器中
6. 必须告诉 Spring 哪个是切面类 ，使用 `@Aspect` 给切面类加一个注解
7. 给配置类加 `@EnableAspectJAutoProxy` ，开启主机与注解的 AOP 模式

#### 三步：

1. 将业务逻辑类和切面类都加入到容器中；告诉 Spring 哪个是切面类
2. 在切面类上的每一个通知方法标注通知注解，告诉 Spring 何时何地运行（写好切入点表达式）
3. 开启基于注解的 aop  模式

#### 用到的注解：

1. `@Aspect, @Pointcut` 
2. ` @Before/@After/@AfterReturning/@AfterThrowing/@Around `
3. ` @EnableAspectJAutoProxy`



###  AOP 原理

> 所有的原理都是看给容器中注册了什么组件，这个组件什么时候工作，这个组件工作时候的功能是什么

#### 从 ` @EnableAspectJAutoProxy` 开始入手研究

##### 1. ` @EnableAspectJAutoProxy`  导入了 `AspectJAutoProxyRegistrar.class`

* `AspectJAutoProxyRegistrar` 作用就是向容器中注册 `AnnotationAwareAspectJAutoProxyCreator` 这个 Bean
  * `internalAutoProxyCreator=AnnotationAwareAspectJAutoProxyCreator`

##### 2. `AnnotationAwareAspectJAutoProxyCreator`  的作用是什么

> 可以在 IDEA 中右键 Diagrams 查看这个类的继承关系

`AnnotationAwareAspectJAutoProxyCreator`

* `AspectJAwareAdvisorAutoProxyCreator`
  * `AbstractAdvisorAutoProxyCreator`
    * `AbstractAutoProxyCreator`
      * `implements SmartInstantiationAwareBeanPostProcessor, BeanFactoryAware`        

关注后置处理器（在 bean 初始化完成前后做事情）、自动装配 `BeanFactory`
`AbstractAutoProxyCreator.setBeanFactory()`
`AbstractAutoProxyCreator`.有后置处理器的逻辑；
`AbstractAdvisorAutoProxyCreator.setBeanFactory() ----> initBeanFactory()`
`AnnotationAwareAspectJAutoProxyCreator.initBeanFactory()`

##### 流程：

1. 传入配置类，创建容器
2. 注册配置类，调用 `refresh()` 刷新容器
3. `registerBeanPostProcessors(beanFactory)`; 注册 bean 的后置处理器来方便拦截 bean 的创建 `PostProcessorRegistrationDelegate#registerBeanPostProcessors()`
	
	1. 先获取 ioc 容器已经定义了的需要创建对象的所有 `BeanPostProcessor`
	
	2. 给容器中加别的 `BeanPostProcessor`
	
	3. 优先注册实现了 `PriorityOrdered `接口的 `BeanPostProcessor`
	
	4. 再给容器中注册实现了 `Ordered` 接口的 `BeanPostProcessor`；
	
	5. 注册没实现优先级接口的 `BeanPostProcessor`；
	
	6. 注册 `BeanPostProcessor`，实际上就是创建 `BeanPostProcessor ` 对象，保存在容器中；
	
	   创建`internalAutoProxyCreator` 的`BeanPostProcessor`实际上是【`AnnotationAwareAspectJAutoProxyCreator`】这个类型
	
	   1. 创建 Bean 的实例 `createBeanInstance();`
	   2. 给 Bean 的属性赋值`populateBean();`
	   3. 初始化 Bean，`initializeBean();` `AbstractAutowireCapableBeanFactory#initializeBean`
	      1. `AbstractAutowireCapableBeanFactory#invokeAwareMethods`：处理 Aware 接口的方法回调
	      2. `applyBeanPostProcessorsBeforeInitialization();`：应用后置处理器的 `postProcessBeforeInitialization()`
	      3. `invokeInitMethods();`：执行自定义的初始化方法
	      4. `applyBeanPostProcessorsAfterInitialization();`：执行后置处理器的`postProcessAfterInitialization()`
	   4. `AnnotationAwareAspectJAutoProxyCreator` 这个 `BeanPostProcessor` 创建成功；---> `aspectJAdvisorsBuilder`
	   
	7. 把 `BeanPostProcessor` 注册到 `BeanFactory` 中；`beanFactory.addBeanPostProcessor(postProcessor)`

======= 以上是创建和注册 `AnnotationAwareAspectJAutoProxyCreator` 的过程 ========

`AnnotationAwareAspectJAutoProxyCreator` 属于 `BeanPostProcessor` 中的 `InstantiationAwareBeanPostProcessor`

4. `finishBeanFactoryInitialization(beanFactory);` 完成 BeanFactory 的初始化工作，创建剩下的单实例 bean，Instantiate all remaining (non-lazy-init) singletons.

   1. 历获取容器中所有的 Bean，依次创建对象 `getBean(beanName)`;

      `getBean()->doGetBean()->getSingleton()`

   2. 创建 bean

      1. 先从缓存中获取当前bean，如果能获取到，说明 bean 是之前被创建过的，直接使用，否则再创建；

         * 只要创建好的Bean都会被缓存起来

      2. `createBean()`;创建bean

         * >  * 					【BeanPostProcessor是在Bean对象创建完成初始化前后调用的】
           >  * 					【InstantiationAwareBeanPostProcessor是在创建 Bean 实例之前先尝试用后置处理器返回对象的】
           >
           > 所以，`AnnotationAwareAspectJAutoProxyCreator` 会在任何 bean 创建之前先尝试返回bean的实例

         1. `resolveBeforeInstantiation(beanName, mbdToUse) // Give BeanPostProcessors a chance to return a proxy instead of the target bean instance.;`：希望后置处理器再次能返回一个代理对象；如果能返回代理对象就使用，如果不能就继续

            1. `InstantiationAwareBeanPostProcessor`这种类型的后置处理器先尝试返回对象；

            ```java
            // 拿到所有后置处理器，如果是InstantiationAwareBeanPostProcessor;
            // 就执行postProcessBeforeInstantiation
            bean = applyBeanPostProcessorsBeforeInstantiation(targetType, beanName);
            if (bean != null) {
                bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
            }
            ```

            

         2. `AbstractAutowireCapableBeanFactory#doCreateBean`真正的去创建一个bean实例；和3.6流程一样；

---

#### AnnotationAwareAspectJAutoProxyCreator【InstantiationAwareBeanPostProcessor】的作用：

> 下面分析的是一个被增强的业务逻辑类走到这个的逻辑

1. 每一个 bean 创建之前，调用 `postProcessBeforeInstantiation()`

   1. 判断当前 bean 是否在 advisedBeans 中（保存了所有需要增强 bean）

   2. 判断当前bean是否是基础类型的Advice、Pointcut、Advisor、AopInfrastructureBean，或者是否是切面（@Aspect）（意思是这些类型永远不应该被代理）

   3. 判读是否要跳过

      1. 获取候选的增强器（切面里面的通知方法）【List<Advisor> candidateAdvisors】

         每一个封装的通知方法的增强器是 `InstantiationModelAwarePointcutAdvisor`；

         判断每一个增强器是否是 `AspectJPointcutAdvisor` 类型的；返回true

         但是现在定义的增强器都不是这个类型，执行父类的方法

      2. 永远返回false

2. 反射调用构造函数创建 bean 实例

   `postProcessAfterInitialization`；

   `return wrapIfNecessary(bean, beanName, cacheKey);`//如果需要的情况下包装 `bstractAutoProxyCreator#wrapIfNecessary`

   1. 获取当前bean的所有增强器（通知方法）`getAdvicesAndAdvisorsForBean`，`AbstractAdvisorAutoProxyCreator#findEligibleAdvisors` 保存到了 `Object[]  specificInterceptors`

      1. 找到候选的所有的增强器（找哪些通知方法是需要切入当前bean方法的）
      2. 获取到能在bean使用的增强器
      3. 给增强器排序
   2. 保存当前 bean 在 advisedBeans 中
   3. 如果当前 bean 需要增强，创建当前 bean 的代理对象；`AbstractAutoProxyCreator#createProxy`

      1. 获取所有增强器（通知方法）

      2. 保存到 proxyFactory

      3. `DefaultAopProxyFactory#createAopProxy`，创建代理对象：Spring自动决定

         1. `JdkDynamicAopProxy(config)` jdk动态代理 --> `CglibAopProxy#getProxy()`
         2. `ObjenesisCglibAopProxy(config)` cglib的动态代理
   4. 给容器中返回当前组件使用 cglib 增强了的代理对象
   5. 以后容器中获取到的就是这个组件的代理对象，执行目标方法的时候，代理对象就会执行通知方法的流程

3. 目标方法执行，容器中保存了组件的代理对象（cglib增强后的对象），这个对象里面保存了详细信息（比如增强器，目标对象等信息）

   1. 拦截目标方法的执行 `CglibAopProxy$DynamicAdvisedInterceptor#intercept`

   2. 根据 ProxyFactory 对象获取将要执行的目标方法拦截器链

      `List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass)`

      1. `List<Object> interceptorList` 保存所有拦截器：一个默认的 `ExposeInvocationInterceptor `和 自己写的其他增强器

      2. 遍历所有的增强器，将其转为 `Interceptor`，`registry.getInterceptors(advisor)`，转换完成返回 `MethodInterceptor` 数组
   
         1. 将增强器转为 `List<MethodInterceptor>`，
   
         2. 如果是 `MethodInterceptor`，
   
            * 直接加入到集合中
   
             * 				如果不是，使用 `AdvisorAdapter ` 将增强器转为 `MethodInterceptor`
   
   3. 如果没有拦截器链，直接执行目标方法，拦截器链（每一个通知方法又被包装为方法拦截器，利用`MethodInterceptor` 机制）
   
   4. 如果有拦截器链，把需要执行的目标对象，目标方法，拦截器链等信息传入创建一个 `CglibMethodInvocation` 对象，并调用 `Object retVal =  mi.proceed()`
   
   5. 拦截器链的触发过程：`ReflectiveMethodInvocation#proceed` 递归调用，（相当于将链串起来，然后反向执行），拦截器链的机制，保证通知方法与目标方法的执行顺序；
   
      1. 如果没有拦截器执行执行目标方法，或者拦截器的索引和拦截器数组 -1 大小一样（执行到了最后一个拦截器）执行目标方法
      2. 链式获取每一个拦截器，拦截器执行invoke方法，每一个拦截器等待下一个拦截器执行完成返回以后再来执行
   
   
   
   ---
   
   #### AOP 总结
   
   1. @EnableAspectJAutoProxy 开启AOP功能
   2. @EnableAspectJAutoProxy 会给容器中注册一个组件 AnnotationAwareAspectJAutoProxyCreator
   3. AnnotationAwareAspectJAutoProxyCreator是一个后置处理器
   4. 容器的创建流程：
      1. `registerBeanPostProcessors()` 注册后置处理器；创建AnnotationAwareAspectJAutoProxyCreator 对象
      2. `finishBeanFactoryInitialization()` 初始化剩下的单实例bean
         1. 创建业务逻辑组件和切面组件
          * 				AnnotationAwareAspectJAutoProxyCreator 拦截组件的创建过程
          3. 组件创建完之后，判断组件是否需要增强
             * 是：切面的通知方法，包装成增强器（Advisor），然后给业务逻辑组件创建一个代理对象（cglib）；
   5. 执行目标方法：
      1. 代理对象执行目标方法
      2. `CglibAopProxy.intercept()`
         1. 得到目标方法的拦截器链（增强器包装成拦截器 `MethodInterceptor`）
         2. 利用拦截器的链式机制，依次进入每一个拦截器进行执行
         3. 效果：
             * 					正常执行：前置通知---->目标方法---->后置通知---->返回通知
             * 					出现异常：前置通知---->目标方法---->后置通知---->异常通知



### 声明式事务

#### 环境搭建

1. 导入相关依赖：数据源，数据库驱动，Spring-jdbc 模块
2. 配置数据源，`JdbcTemplate`
3. 方法或类上标注 `@Transactional`
4. 开启事务管理功能：`@EnableTransactionManagement`
5. 配置事务管理器 `PlatformTransactionManager` 来控制事务

#### 原理

1. `@EnableTransactionManagement` 利用 `TransactionManagementConfigurationSelector` 向容器中导入组件，默认情况下是导入了两个组件：`AutoProxyRegistrar`，`ProxyTransactionManagementConfiguration`

2. `AutoProxyRegistrar`：给容器中注册一个 `InfrastructureAdvisorAutoProxyCreator` 组件

   1. 作用只是利用后置处理器机制，在对象创建以后，包装对象成一个代理对象（包含增强器），代理对象执行方法利用拦截器链进行调用

   > 注意：`InfrastructureAdvisorAutoProxyCreator`  的 bean id 也是`org.springframework.aop.config.internalAutoProxyCreator`，如果同时 `@EnableAspectJAutoProxy `，`AnnotationAwareAspectJAutoProxyCreator` 最终会占用这个 id
   >
   > 在 IDEA 中一起查看这两个类的继承关系，发现都是继承自 `AbstractAdvisorAutoProxyCreator`

3. `ProxyTransactionManagementConfiguration` -- 这个是一个配置类，而是文档显示： @Configuration class that registers the Spring infrastructure beans necessary to enable proxy-based annotation-driven transaction management.

   1. 给容器中注册事务增强器；
      1. 事务增强器要用事务注解的信息，`AnnotationTransactionAttributeSource` 用于解析事务注解
      2. 事务增强器要用事务拦截器，`TransactionInterceptor` 中保存了事务属性信息，事务管理器，而且它还是一个 `MethodInterceptor`， 在目标方法执行的时候，执行这些拦截器链， `TransactionAspectSupport#invokeWithinTransaction`
         1. 先获取事务相关的信息
         2. 再获取事务管理器（`PlatformTransactionManager`），如果事先没有添加指定任何（`@Transactional(transactionManager = )`），最后会从容器中按照类型获取 `this.beanFactory.getBean(PlatformTransactionManager.class)`
         3. 执行目标方法，
            1. 如果异常，获取到事务管理器，利用事务管理器回滚操作
            2. 如果正常，利用事务管理器，提交事务