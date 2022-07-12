package ${packageName};

public class ${className}{
    <#list properties as prop>
        ${prop.access} ${prop.type} ${prop.name};
    </#list>
    <#list properties as prop>
        public void set${prop.name?cap_first}(${prop.type} ${prop.name}){
            this.${prop.name}=${prop.name};
        }
        public ${prop.type} get${prop.name?cap_first}(){
            return this.${prop.name};
        }
    </#list>
}