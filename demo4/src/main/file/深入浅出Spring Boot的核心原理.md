1. 基于你对springboot的理解描述一下什么是springboot

   ```html
   服务于框架的框架，可以快速构建spring项目。是约定优于配置的最佳实践。
   ```

2. 约定优于配置指的是什么？

   ```html
   是一种软件的设计思想，按照约定的方式进行配置，当没有配置文件时，使用默认的配置文件，提高了程序设计的可读性及规范性。
   ```

3. @SpringBootApplication由哪几个注解组成，这几个注解分别表示什么作用

   ```html
   @Target(ElementType.TYPE) 该类的类型
   @Retention(RetentionPolicy.RUNTIME) 当前类的保存策略
   @Documented 文档
   @Inherited 继承
   @SpringBootConfiguration springboot配置类
   @EnableAutoConfiguration  启用自动装配，支持使用excludeName进行注入的排除
   @ComponentScan 扫描包的范围
   ```

4. springboot自动装配的实现原理

   ```html
   通过enableAutoConfigration扫描带有注解文件的类，并把该类交给spring进行托管
   ```

5. spring中的spi机制的原理是什么？

   ```html
   原理：
   通过SpringFactoriesLoader，根据key加载classpath下的META-INFO目录下的配置文件
   实现：
   通过配置resource目录下的META-INFO目录下的 .properties文件实现扩展
   文件名称必须使用现有的文件名，文件中属性key必须使用对应的key才会被扩展。
   ```

   