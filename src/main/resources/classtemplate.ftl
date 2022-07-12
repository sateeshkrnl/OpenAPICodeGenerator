${packageName}${';'}

<#list imports as import>
import ${import}${';'}${'\n'}
</#list>
<#if type.name()="DAO">
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
</#elseif type.name()="SERVICE">
import org.springframework.stereotype.Service;
</#if>

<#if type.name()="SERVICE">
@Service
<#elseif type.name()="DAO">
@Repository
</#if>
public class ${name} {
    <#if type.name()="DAO">
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    </#if>

    <#list methods as method>
       ${method}
    </#list>
}
