## Структура XML
```xml
<?xml version='1.0' encoding='utf-8'?>
<oreme-configuration>
    <connection-manager-configuration>
        <property name="connection.url"></property>
        <property name="connection.username"></property>
        <property name="connection.password"></property>
        <property name="connection.driver.class"></property>
        <property name="pool.size"></property> <!-- optional, base value "10" -->
        <property name="pool.expansion"></property> <!-- optional, base value "10" -->
    </connection-manager-configuration>
    <initialization-configuration>
        <property name="strategy"></property>
        <property name="query-generate"></property> <!-- optional, base value "false" -->
        <property name="isolation-level"></property> <!-- optional, base value "serializable" -->
    </initialization-configuration>
    <entities>
        <mapping class=""/>
    </entities>
</oreme-configuration>

```
## Для работы необходима зависимость javassist версии 3.29.0-GA
Maven
```xml
    <dependency>
      <groupId>org.javassist</groupId>
      <artifactId>javassist</artifactId>
      <version>3.29.0-GA</version>
    </dependency>
```
Gradle
```xml
dependencies {
    implementation 'org.javassist:javassist:3.29.0-GA'
}
