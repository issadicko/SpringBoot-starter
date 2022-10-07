package net.dickode.naima.dataloaders;

import net.dickode.naima.entity.*;
import net.dickode.naima.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import org.jetbrains.annotations.NotNull;

import static net.dickode.naima.entity.AppConfig.DEFAULT_CODE;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {


    boolean alreadySetup = false;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AppConfigRepository configRepository;


    @Override @Transactional
    public void onApplicationEvent(@NotNull ContextRefreshedEvent event) {

        if (alreadySetup) return;

        createDefaultCustomer();
        setupSecurity();

        setUpAppConfigs();

        this.alreadySetup = true;
    }

    private void setUpAppConfigs() {

       AppConfig defaultConfig = this.configRepository.findByCode(DEFAULT_CODE);
       if (defaultConfig == null){

           defaultConfig = new AppConfig();
           defaultConfig.setCode(DEFAULT_CODE);
           defaultConfig.setStrictMode(true);
           this.configRepository.save(defaultConfig);

       }

    }

    private void setupSecurity() {

        // Initialisation des permissions d'utilisateurs
        createPermissionIfNotExist("pos-vendor","vendre des articles");
        createPermissionIfNotExist("manage-user","Gestion des comptes");
        createPermissionIfNotExist("show-products","Voir les articles");
        createPermissionIfNotExist("create-products","Création d'articles");
        createPermissionIfNotExist("delete-products","Suppression d'articles");
        createPermissionIfNotExist("manage-sales","Gestion des events");
        createPermissionIfNotExist("advanced-sales-manager","Gestion avancé des ventes");


        // Initialisation des roles d'utilisateurs
        createRoleIfNotExist("ROLE_CASSIER","Cassier");
        createRoleIfNotExist("ROLE_ADMIN","Adminnistrateur");
        Role superAdminRole  = createRoleIfNotExist("ROLE_SUPER_ADMIN", "Super Administrateur");

        // Attribution de toutes les permission au super admin
        permissionRepository.findAll().forEach(superAdminRole::addPermission);
        this.roleRepository.save(superAdminRole);

        setUpDefaultUser(superAdminRole);
    }

    private void setUpDefaultUser(Role role) {

        if (this.userRepository.findByEmail("hello@dickode.net") == null){

            User defaultSuperAdmin = new User();
            defaultSuperAdmin.setEmail("hello@dickode.net");
            defaultSuperAdmin.setEnabled(true);
            defaultSuperAdmin.setFirstname("SYSTEM");
            defaultSuperAdmin.setLastname("ADMIN");
            defaultSuperAdmin.setPhone("06661228");
            defaultSuperAdmin.setPassword(passwordEncoder.encode("ADMIN@2022"));
            defaultSuperAdmin.addRole(role);
            defaultSuperAdmin.setVisible(false);

            this.userRepository.save(defaultSuperAdmin);

        }

    }

    private void createDefaultCustomer() {

        if (this.customerRepository.findByPhone("INDEFINI") != null) return;

        Customer customer = new Customer();
        customer.setFirstname("CLIENT");
        customer.setLastname("ORDINAIRE");
        customer.setPhone("INDEFINI");

        this.customerRepository.save(customer);

    }

    private void createPermissionIfNotExist(String perm, String name) {

        Permission permission = permissionRepository.findByCode(perm);
        if (permission == null){

            permission = new Permission();
            permission.setCode(perm);
            permission.setName(name);
            this.permissionRepository.save(permission);

        }
    }

    private Role createRoleIfNotExist(String role, String name) {

        Role target = this.roleRepository.findByCode(role);

        if (target == null){
            target = new Role();
            target.setCode(role);
            target.setName(name);
            roleRepository.save(target);
        }

        return target;
    }
}
