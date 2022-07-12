List<${returnType}> results = jdbcTemplate.query(${sql},${paramMap},(rs,i)->{
    ${returnType} returnObj = new ${returnType}();
    <#list cols as col>
        returnObj.set${props[col?index]?cap_first}(rs.<#if col.type=="STRING">getString<#elseif col.type=="INTEGER>rs.getInt</#if>("${col.name}"))
    </#list>
    return returnObj;
});