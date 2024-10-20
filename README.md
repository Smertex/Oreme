## ORM
Это библиотека, которая помогает преобразовывать реляционную модель в объектную по средством использования аннотаций. Возможности:
1. Поиск (реализовано всё, за исключением связей "N to many")
2. Обновление (не реализовано)
3. Сохранение (не реализовано)
4. Удаление (не реализовано)

Все CRUD операции работаю в рамках сессий. Сессии отвечают за контроль над объектами, таким образом может существовать только единственный экземпляр конкретной записи

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
        <property name="session-cache"></property>
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
