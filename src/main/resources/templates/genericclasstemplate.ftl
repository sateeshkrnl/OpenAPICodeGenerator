package ${packageName}${';'}

<#list imports as import>
import ${import}${';'}${'\n'}
</#list>

<#list annotations as ann>
@${ann}
</#list>
public <#if isInterface>interface<#else>class</#if> ${className} {
    <#list properties as prop>
        <#list prop.annotations as ann>
        @${ann}
        </#list>
        ${prop.access} ${prop.type} ${prop.name};
    </#list>

    <#list methods as m>
        <#list m.annotations as ann>
            @${ann}
        </#list>
       ${m.access} ${m.returnType!void} ${m.name}(<#list m.arguments as arg>${arg.type} ${arg.name}<#sep>,</#list>)<#if isInterface>;<#else>{
            ${m.body}
       }</#if>
    </#list>
}
