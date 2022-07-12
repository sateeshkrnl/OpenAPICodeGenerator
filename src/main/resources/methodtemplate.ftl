private static final String ${name}_sql=${sql};
private static final Map<String,String> colToProp = Map.of(<#list colstoprop as key,value>"${key}","${value}"<#sep>,</#list>);

public List<${returnType}> query${name}(<#list arguments as arg>${arg.type} ${arg.name}<#sep>,</#list>){
    ${body}
    <#if type="DAO">
    Map paramMap = buildParamFor${name}(${arguments[0].name});
    String sql = buildSQLFor${name}(${name}_sql);
    List<${returnType}> results = jdbcTemplate.query(sql,paramMap,(rs,i)->{
        ${returnType} returnObj = new ${returnType}();
        <#list cols as col>
            returnObj.set${props[col?index]?cap_first}(rs.<#if col.type=="STRING">getString<#elseif col.type=="INTEGER>rs.getInt</#if>("${col.name}"))
        </#list>
        return returnObj;
    });
    return results;
    </#if>
}

private String buildSQLFor${name}(String sql){
    Map<String,String> props = BeanUtils.describe(searchObj);
    String whereClause = props.entrySet().streams()
            .filter(e->e.getValue()!=null && !e.getValue().isBlank())
            .map(e->e.getKey())
            .reduce("WHERE ",(q,k)->String.format("%1$s%2$s = %3$s AND",q,colToProp.get(k),k),String::concat);
    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append(sql).append(whereClause);
    return sqlBuilder.substring(0,sqlBuilder.lastIndexOf("AND"));
}

private Map<String,Object> buildParamFor${name}(${arguments[0].type} searchObj){
    Map<String,String> props = BeanUtils.describe(searchObj);
    return props.entrySet().streams()
        .filter(e->e.getValue()!=null && !e.getValue().isBlank())
        .collect(Collectors.toMap(Functions.identity(),Functions.identity());
}