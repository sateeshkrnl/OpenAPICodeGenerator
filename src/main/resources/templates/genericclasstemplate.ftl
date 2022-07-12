package ${packageName}${';'}

<#list imports as import>
import ${import}${';'}${'\n'}
</#list>

public class ${className} {
    <#list properties as prop>
        ${prop.access} ${prop.type} ${prop.name};
    </#list>

    <#list methods as m>
        <#list m.annotations as ann>
            @${ann}
        </#list>
       ${m.access} ${m.returnType!void} ${m.name}(<#list m.arguments as arg>${arg.type} ${arg.name}<#sep>,</#list>){
            ${m.body}
       }
    </#list>
}
