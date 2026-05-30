package com.example.library_management.controller;

import com.example.library_management.model.Kullanici;
import com.example.library_management.repository.KullaniciRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/staff/kullanicilar")
public class StaffKullaniciController {

    private final KullaniciRepository kullaniciRepository;

    public StaffKullaniciController(KullaniciRepository kullaniciRepository) {
        this.kullaniciRepository = kullaniciRepository;
    }

    @GetMapping
    public String listKullanicilar(Model model) {
        List<Kullanici> kullanicilar = kullaniciRepository.findAll();
        model.addAttribute("kullanicilar", kullanicilar);
        return "staff/kullanicilar";
    }

    // PERSONEL (STAFF) ÜYE SİLEMEZ.
    // O yüzden delete metodu kaldırıldı.

    @GetMapping("/durum/{id}")
    public String durumDegistir(@PathVariable int id) {
        Kullanici kullanici = kullaniciRepository.findById(id).orElse(null);

        if (kullanici != null) {
            kullanici.setAktif(!kullanici.isAktif());
            kullaniciRepository.save(kullanici);
        }

        return "redirect:/staff/kullanicilar";
    }

    @GetMapping("/duzenle/{id}")
    public String duzenleForm(@PathVariable int id, Model model) {
        Kullanici kullanici = kullaniciRepository.findById(id).orElse(null);

        if (kullanici == null) {
            return "redirect:/staff/kullanicilar";
        }

        model.addAttribute("kullanici", kullanici);
        model.addAttribute("kullanicilar", kullaniciRepository.findAll());
        model.addAttribute("duzenlemeModu", true);

        return "staff/kullanicilar";
    }

    @PostMapping("/update/{id}")
    public String updateKullanici(@PathVariable int id,
                                  @ModelAttribute Kullanici form) {

        Kullanici mevcut = kullaniciRepository.findById(id).orElse(null);

        if (mevcut != null) {
            mevcut.setAd(form.getAd());
            mevcut.setSoyad(form.getSoyad());
            mevcut.setEmail(form.getEmail());
            mevcut.setTelefon(form.getTelefon());
            mevcut.setAdres(form.getAdres());
            mevcut.setRol(form.getRol());

            kullaniciRepository.save(mevcut);
        }

        return "redirect:/staff/kullanicilar";
    }
}
