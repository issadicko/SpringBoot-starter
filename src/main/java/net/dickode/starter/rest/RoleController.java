package bf.orange.authservice.rest;

import bf.orange.authservice.dao.entity.Permission;
import bf.orange.authservice.dao.entity.Role;
import bf.orange.authservice.dao.repository.PermissionRepository;
import bf.orange.authservice.dao.repository.RoleRepository;
import bf.orange.authservice.utils.bean.ApiResponse;
import bf.orange.authservice.utils.dto.RoleDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/api/roles") @AllArgsConstructor
public class RoleController {

    private RoleRepository roleRepository;
    private PermissionRepository permissionRepository;

    @GetMapping
    public List<Role> all(){
        return this.roleRepository.findAll();
    }


    @PostMapping
    public ApiResponse<String> createRole(@RequestBody RoleDTO roleDTO){

        if (roleDTO.getName() == null){
            return new ApiResponse<String>(false, "Valeurs invalides !");
        }

        if (roleDTO.getCode() == null || roleDTO.getCode().isEmpty()){
            roleDTO.setCode(roleDTO.getName().toUpperCase().replaceAll(" ","_"));
            if (!roleDTO.getCode().startsWith("ROLE")){
                roleDTO.setCode("ROLE_"+roleDTO.getCode());
            }
        }

        Role role  = this.roleRepository.findByCode(roleDTO.getCode());
        if (role != null){
            return new ApiResponse<>(false, "Ce role existe déja !");
        }

        role = roleDTO.toRole();
        buildRole(roleDTO, role);
        this.roleRepository.save(role);
        return new ApiResponse<>(true, "OK");
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateRole(@RequestBody RoleDTO roleDTO,@PathVariable Integer id){

        Optional<Role> roleOptional  = this.roleRepository.findById(id);

        if (roleOptional.isEmpty()){
            return ResponseEntity.badRequest().body(new ApiResponse<String>(false, "Role introuvable !"));
        }

        Role role = roleOptional.get();
        role.setName(roleDTO.getName());

        role.getPermissions().clear();

        buildRole(roleDTO, role);
        this.roleRepository.save(role);
        return ResponseEntity.ok(new ApiResponse<>(true, "OK"));
    }

    private void buildRole(RoleDTO roleDTO, Role role) {
        ArrayList<Permission> permissions = new ArrayList<>();
        roleDTO.getPermissions().forEach(thePermission -> {

            Permission permission = this.permissionRepository.findPermissionByCode(thePermission);
            if (permission != null){
                permissions.add(permission);
            }

        });

        role.setPermissions(permissions);
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<String> deleteRole(@PathVariable Integer id){

        Optional<Role> roleOptional  = this.roleRepository.findById(id);

        if (roleOptional.isEmpty()){
            return new ApiResponse<>(false, "Role introuvable !");
        }

        Role role = roleOptional.get();
        this.roleRepository.delete(role);

        return new ApiResponse<>(true, "OK");
    }

}
