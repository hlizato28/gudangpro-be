package co.id.bcafinance.hlbooking.configuration;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 06/05/2024 09:20
@Last Modified 06/05/2024 09:20
Version 1.0
*/

import co.id.bcafinance.hlbooking.model.Role;
import co.id.bcafinance.hlbooking.repo.RoleRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleInitializer implements CommandLineRunner {
    private final RoleRepo roleRepo;

    public RoleInitializer(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        if (roleRepo.count() == 0) {
            Role adminRole = new Role();
            adminRole.setNamaRole("Admin");
            roleRepo.save(adminRole);

            Role pemohonRole = new Role();
            pemohonRole.setNamaRole("Pemohon");
            roleRepo.save(pemohonRole);

            Role picGudangRole = new Role();
            picGudangRole.setNamaRole("PIC Gudang");
            roleRepo.save(picGudangRole);

            Role bohRole = new Role();
            bohRole.setNamaRole("BOH");
            roleRepo.save(bohRole);

            Role gsRole = new Role();
            gsRole.setNamaRole("General Service");
            roleRepo.save(gsRole);
        }
    }
}

