package rs.lab.notes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import rs.lab.notes.data.model.Role;
import rs.lab.notes.data.model.RoleEnum;
import rs.lab.notes.data.dao.RoleRepository;
import rs.lab.notes.services.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import rs.lab.notes.services.CategoryService;

// http://127.0.0.1:8086/swagger-ui/index.html
@SpringBootApplication
public class NotesApplication implements ApplicationListener<ContextRefreshedEvent> {

    public static void main(String[] args) {
        SpringApplication.run(NotesApplication.class, args);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        roleSeeder();
        userSeeder();
        categorySeeder();
    }

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    private void roleSeeder() {
        RoleEnum[] roleNames = new RoleEnum[]{RoleEnum.DEFAULT, RoleEnum.USER, RoleEnum.ADMIN};
        Map<RoleEnum, String> roleDescriptionMap = Map.of(
                RoleEnum.DEFAULT, "Default user role",
                RoleEnum.USER, "User role",
                RoleEnum.ADMIN, "Administrator role"
        );

        Arrays.stream(roleNames).forEach((roleName) -> {
            Optional<Role> optionalRole = roleRepository.findByName(roleName);

            optionalRole.ifPresentOrElse(System.out::println, () -> {
                Role roleToCreate = Role.builder()
                        .name(roleName)
                        .description(roleDescriptionMap.get(roleName))
                        .build();

                roleRepository.save(roleToCreate);
            });
        });
    }

    private void userSeeder() {
        {
            var user = userService.getUserByEmail("sam@example.com");
            user.ifPresentOrElse(System.out::println, () -> {
                userService.createUser("Sam Spade", "sam@example.com", "123", Set.of(RoleEnum.USER, RoleEnum.ADMIN));
            });
        }
        {
            var user = userService.getUserByEmail("tom@example.com");
            user.ifPresentOrElse(System.out::println, () -> {
                userService.createUser("Tom Spade", "tom@example.com", "123", Set.of(RoleEnum.USER));
            });
        }
        {
            var user = userService.getUserByEmail("joe@example.com");
            user.ifPresentOrElse(System.out::println, () -> {
                userService.createUser("Joe Spade", "joe@example.com", "123", Set.of(RoleEnum.USER));
            });
        }
    }

    private void categorySeeder() {
        var categories = List.of("Category 1", "Category 2", "Category 3");

        categories.forEach(p -> {
            categoryService.findCategoryByName(p).ifPresentOrElse(System.out::println, () -> {
                categoryService.createCategory(p);
            });
        });

    }
}
