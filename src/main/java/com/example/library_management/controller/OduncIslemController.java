package com.example.library_management.controller;

import com.example.library_management.model.Kullanici;
import com.example.library_management.model.OduncIslemi;
import com.example.library_management.repository.CezaRepository;
import com.example.library_management.repository.OduncIslemRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/odunc-islemleri")
public class OduncIslemController {

    private final OduncIslemRepository repository;
    private final CezaRepository cezaRepository;

    public OduncIslemController(OduncIslemRepository repository,
                                CezaRepository cezaRepository) {
        this.repository = repository;
        this.cezaRepository = cezaRepository;
    }

    @GetMapping
    public String listOduncIslemleri(Model model) {
        model.addAttribute("oduncler", repository.findAll());
        model.addAttribute("yeniOdunc", new OduncIslemi());

        return "admin/odunc";
    }

    @PostMapping("/save")
    public String saveOduncIslemi(@ModelAttribute("yeniOdunc") OduncIslemi oduncIslemi) {
        repository.save(oduncIslemi);
        return "redirect:/odunc-islemleri";
    }

    @GetMapping("/delete/{id}")
    public String deleteOduncIslemi(@PathVariable int id) {
        repository.deleteById(id);
        return "redirect:/odunc-islemleri";
    }

    @GetMapping("/user")
    public String listUserOduncIslemleri(HttpSession session, Model model) {

        Kullanici aktifKullanici = (Kullanici) session.getAttribute("aktifKullanici");

        if (aktifKullanici == null) {
            return "redirect:/login";
        }

        int kullaniciId = aktifKullanici.getId();

        List<OduncIslemi> devamEdenler = repository.findAll().stream()
                .filter(o -> o.getKullaniciId() == kullaniciId)
                .filter(o -> o.getDurum() != null &&
                        (o.getDurum().equals("DEVAM_EDIYOR") || o.getDurum().equals("GECIKMIS")))
                .toList();

        List<OduncIslemi> gecmisIslemler = repository.findAll().stream()
                .filter(o -> o.getKullaniciId() == kullaniciId)
                .filter(o -> o.getDurum() != null && o.getDurum().equals("TESLIM_EDILDI"))
                .toList();

        var cezalar = cezaRepository.findAll().stream()
                .filter(c -> c.getKullaniciId() == kullaniciId)
                .toList();

        model.addAttribute("devamEdenler", devamEdenler);
        model.addAttribute("gecmisIslemler", gecmisIslemler);
        model.addAttribute("cezalar", cezalar);

        return "user/odunc";
    }
}