XML структура:

> <?xml version='1.0' encoding='utf-8'?>
<oreme-configuration>
    <connection-manager-configuration>
        <property name="connection.url"></property>
        <property name="connection.username"></property>
        <property name="connection.password"></property>
        <property name="connection.driver.class"></property>
        <property name="pool.size"></property>
        <property name="pool.expansion"></property>
    </connection-manager-configuration>
    <initialization-configuration>
        <property name="strategy"></property>
        <property name="query-generate"></property>
    </initialization-configuration>
    <entities>
        <mapping class=""/>
    </entities>
</oreme-configuration>
