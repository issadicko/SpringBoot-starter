package bf.orange.authservice.utils.dto;

import bf.orange.authservice.dao.entity.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

import java.util.ArrayList;

@JsonInclude(Include.NON_NULL)
public class RoleDTO {

    private String name;
    private String code;
    private ArrayList<String> permissions = new ArrayList<>();


    public Role toRole(){
        Role role = new Role();
        role.setCode(code);
        role.setName(name);

        return role;
    }

    public ArrayList<String> getPermissions(){
        ArrayList<String> uniqlist = new ArrayList<>();
        for(String val: permissions) {
            if(!uniqlist.contains(val)) {
                uniqlist.add(val);
            }
        }

        return uniqlist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setPermissions(ArrayList<String> permissions) {
        this.permissions = permissions;
    }
}
