package com.example.library_management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.library_management.model.Kategori;
import com.example.library_management.model.Yayinevi;
import com.example.library_management.model.Yazar;
import com.example.library_management.repository.KategoriRepository;
import com.example.library_management.repository.YayineviRepository;
import com.example.library_management.repository.YazarRepository;

@Controller
@RequestMapping("/admin/sistem-ayarlari")
public class SistemAyarlariController {

    private final KategoriRepository kategoriRepository;
    private final YazarRepository yazarRepository;
    private final YayineviRepository yayineviRepository;

    public SistemAyarlariController(KategoriRepository kategoriRepository,
                                     YazarRepository yazarRepository,
                                     YayineviRepository yayineviRepository) {
        this.kategoriRepository = kategoriRepository;
        this.yazarRepository = yazarRepository;
        this.yayineviRepository = yayineviRepository;
    }

    @GetMapping
    public String showSettings(Model model) {
        model.addAttribute("kategoriler", kategoriRepository.findAll());
        model.addAttribute("yazarlar", yazarRepository.findAll());
        model.addAttribute("yayinevleri", yayineviRepository.findAll());
        model.addAttribute("yeniKategori", new Kategori());
        model.addAttribute("yeniYazar", new Yazar());
        model.addAttribute("yeniYayinevi", new Yayinevi());
        return "admin/sistem-ayarlari";
    }

    // Kategori CRUD
    @PostMapping("/kategori/save")
    public String saveKategori(@ModelAttribute Kategori kategori) {
        kategoriRepository.save(kategori);
        return "redirect:/admin/sistem-ayarlari#kategoriler";
    }

    @GetMapping("/kategori/delete/{id}")
    public String deleteKategori(@PathVariable int id) {
        kategoriRepository.deleteById(id);
        return "redirect:/admin/sistem-ayarlari#kategoriler";
    }

    @PostMapping("/kategori/update/{id}")
    public String updateKategori(@PathVariable int id, @ModelAttribute Kategori form) {
        Kategori k = kategoriRepository.findById(id).orElse(null);
        if (k != null) {
            k.setAd(form.getAd());
            k.setAciklama(form.getAciklama());
            kategoriRepository.save(k);
        }
        return "redirect:/admin/sistem-ayarlari#kategoriler";
    }

    // Yazar CRUD
    @PostMapping("/yazar/save")
    public String saveYazar(@ModelAttribute Yazar yazar) {
        yazarRepository.save(yazar);
        return "redirect:/admin/sistem-ayarlari#yazarlar";
    }

    @GetMapping("/yazar/delete/{id}")
    public String deleteYazar(@PathVariable int id) {
        yazarRepository.deleteById(id);
        return "redirect:/admin/sistem-ayarlari#yazarlar";
    }

    @PostMapping("/yazar/update/{id}")
    public String updateYazar(@PathVariable int id, @ModelAttribute Yazar form) {
        Yazar y = yazarRepository.findById(id).orElse(null);
        if (y != null) {
            y.setAd(form.getAd());
            y.setSoyad(form.getSoyad());
            y.setBiyografi(form.getBiyografi());
            yazarRepository.save(y);
        }
        return "redirect:/admin/sistem-ayarlari#yazarlar";
    }

    // Yayınevi CRUD
    @PostMapping("/yayinevi/save")
    public String saveYayinevi(@ModelAttribute Yayinevi yayinevi) {
        yayineviRepository.save(yayinevi);
        return "redirect:/admin/sistem-ayarlari#yayinevleri";
    }

    @GetMapping("/yayinevi/delete/{id}")
    public String deleteYayinevi(@PathVariable int id) {
        yayineviRepository.deleteById(id);
        return "redirect:/admin/sistem-ayarlari#yayinevleri";
    }

    @PostMapping("/yayinevi/update/{id}")
    public String updateYayinevi(@PathVariable int id, @ModelAttribute Yayinevi form) {
        Yayinevi yv = yayineviRepository.findById(id).orElse(null);
        if (yv != null) {
            yv.setAd(form.getAd());
            yv.setAdres(form.getAdres());
            yv.setTelefon(form.getTelefon());
            yayineviRepository.save(yv);
        }
        return "redirect:/admin/sistem-ayarlari#yayinevleri";
    }
}
