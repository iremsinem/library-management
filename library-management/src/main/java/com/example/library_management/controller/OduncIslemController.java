package com.example.library_management.controller;

import com.example.library_management.model.OduncIslemi;
import com.example.library_management.repository.OduncIslemiRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/odunc-islemleri")
public class OduncIslemiController {

    private final OduncIslemiRepository repository;

    // Bağımlılık enjeksiyonu için constructor (Hocanın tercih ettiği yöntem) [cite: 1371]
    public OduncIslemiController(OduncIslemiRepository repository) {
        this.repository = repository;
    }

    // Tüm ödünç işlemlerini listeleme metodu [cite: 1372]
    @GetMapping
    public String listOduncIslemleri(Model model) {
        // Veritabanındaki tüm kayıtları çekip "oduncler" ismiyle sayfaya gönderir [cite: 1373, 1374]
        model.addAttribute("oduncler", repository.findAll());

        // Yeni bir boş nesne gönderir (Formda doldurulmak üzere)
        model.addAttribute("yeniOdunc", new OduncIslemi());

        return "admin/odunc"; // "odunc-list" yerine yeni yol
    }

    // Yeni ödünç işlemi kaydetme metodu [cite: 1392]
    @PostMapping("/save")
    public String saveOduncIslemi(@ModelAttribute("yeniOdunc") OduncIslemi oduncIslemi) {
        // Formdan gelen verileri veritabanına kaydeder [cite: 1393, 1394]
        repository.save(oduncIslemi);

        // İşlem bitince tekrar listeleme sayfasına yönlendirir [cite: 1395]
        return "redirect:/odunc-islemleri";
    }

    // Bir ödünç işlemini silme metodu [cite: 1403]
    @GetMapping("/delete/{id}")
    public String deleteOduncIslemi(@PathVariable int id) {
        // Belirtilen ID'ye sahip kaydı siler [cite: 1404, 1405]
        repository.deleteById(id);

        // Listeleme sayfasına geri döner [cite: 1414]
        return "redirect:/odunc-islemleri";
    }

    // Kullanıcıya özel ödünç listeleme sayfası
    @GetMapping("/user")
    public String listUserOduncIslemleri(Model model) {
        model.addAttribute("oduncler", repository.findAll());
        return "user/odunc";
    }
}
