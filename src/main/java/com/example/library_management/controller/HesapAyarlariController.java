package com.example.library_management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.library_management.model.Kullanici;
import com.example.library_management.repository.KullaniciRepository;

@Controller
@RequestMapping("/user/hesap-ayarlari")
public class HesapAyarlariController {

    private final KullaniciRepository kullaniciRepository;

    public HesapAyarlariController(KullaniciRepository kullaniciRepository) {
        this.kullaniciRepository = kullaniciRepository;
    }

    @GetMapping
    public String showHesapAyarlari(Model model) {
        // Sabit 1 ID'sini aramak yerine veritabanındaki İLK kullanıcıyı getir, yoksa oluştur.
        Kullanici kullanici = kullaniciRepository.findAll().stream().findFirst().orElseGet(() -> {
            Kullanici yeni = new Kullanici();
            yeni.setAd("Ahmet");
            yeni.setSoyad("Yilmaz");
            yeni.setEmail("ahmet@example.com");
            yeni.setSifre("12345");
            yeni.setRol("USER");
            yeni.setAktif(true);
            return kullaniciRepository.save(yeni);
        });
        model.addAttribute("kullanici", kullanici);
        return "user/hesap-ayarlari";
    }

    @PostMapping("/save")
    public String saveHesapAyarlari(@ModelAttribute Kullanici form, RedirectAttributes redirectAttrs) {
        
        Kullanici k = null;
        // int türündeki ID null olamayacağı için sadece 0'dan büyük mü diye kontrol ediyoruz
        if (form.getId() > 0) {
            k = kullaniciRepository.findById(form.getId()).orElse(null);
        }
        
        // Formdan geçerli bir ID gelmediyse veritabanındaki ilk kullanıcıyı al (Demo modu)
        if (k == null) {
            k = kullaniciRepository.findAll().stream().findFirst().orElse(null);
        }

        if (k != null) {
            k.setAd(form.getAd());
            k.setSoyad(form.getSoyad());
            k.setEmail(form.getEmail());
            k.setTelefon(form.getTelefon());
            k.setAdres(form.getAdres());
            
            // Eğer şifre kutusu doldurulduysa günceller, boşsa eski şifre kalır
            if (form.getSifre() != null && !form.getSifre().isBlank()) {
                k.setSifre(form.getSifre());
            }
            
            kullaniciRepository.save(k);
            redirectAttrs.addFlashAttribute("basari", "Hesap bilgileriniz başarıyla güncellendi.");
        } else {
            redirectAttrs.addFlashAttribute("hata", "Kullanıcı bulunamadı.");
        }
        
        return "redirect:/user/hesap-ayarlari";
    }
}