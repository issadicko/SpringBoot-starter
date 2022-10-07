package bf.orange.authservice.utils.dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class RoleGrantDTO {
    String cuid;
    ArrayList<Integer> roles;
}
